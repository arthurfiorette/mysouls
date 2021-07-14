package com.github.arthurfiorette.mysouls.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.sinklibrary.data.database.Database;
import com.github.arthurfiorette.sinklibrary.executor.v2.TaskContext;
import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.uuid.FastUuid;

public class WalletDatabase implements Database<UUID, String> {

  private final File file;
  private Connection connection;
  private final MySouls plugin;

  public WalletDatabase(final MySouls plugin) {
    this.file = new File(plugin.getDataFolder().getPath(), "database.db");
    this.plugin = plugin;
  }

  @Override
  public void enable() throws Exception {
    if ((this.connection == null) || this.connection.isClosed()) {
      Class.forName("org.sqlite.JDBC");
      this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
      final PreparedStatement ps = this.connection.prepareStatement(SqlList.CREATE_TABLE);
      ps.executeUpdate();
      ps.close();
    }
  }

  @Override
  public void disable() throws Exception {
    if (this.connection != null) {
      this.connection.close();
    }
  }

  @Override
  public void save(final UUID key, final String value) {
    final String id = FastUuid.toString(key);
    try {
      final PreparedStatement ps = this.connection.prepareStatement(SqlList.INSERT);
      ps.setString(1, id);
      ps.setString(2, value);
      ps.executeUpdate();
      ps.close();
    } catch (final SQLException exc) {
      this.plugin.treatThrowable(this.getClass(), exc,
          "An error occurred while saving the id: '%s', returning it to the cache...", id);
      TaskContext.BUKKIT.runLater(this.plugin, () -> {
        this.plugin.getComponent(WalletStorage.class).setJson(key, value);
      }, 1);
    }
  }

  @Override
  public String get(final UUID key) {
    final String id = FastUuid.toString(key);

    try {
      final PreparedStatement ps = this.connection.prepareStatement(SqlList.SELECT);
      ps.setString(1, id);
      final ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        return rs.getString("wallet");
      }

      rs.close();
    } catch (final SQLException e) {
      this.plugin.treatThrowable(this.getClass(), e,
          "An error occurred while fetching the id: '%s', returning a new entity...", id);
    }

    return null;
  }

  @Override
  public Collection<String> getMany(final Collection<UUID> keys) {
    final String sql = SqlList.selectMany(keys.stream().map(k -> "?").toArray(String[]::new));
    final List<String> list = new ArrayList<>();

    try {
      final PreparedStatement ps = this.connection.prepareStatement(sql);
      int index = 1;
      for(final UUID id: keys) {
        ps.setString(index++, FastUuid.toString(id));
      }

      final ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        list.add(rs.getString("wallet"));
      }

      rs.close();
    } catch (final SQLException e) {
      final String keyArr = String.join(",",
          keys.stream().map(FastUuid::toString).toArray(String[]::new));
      this.plugin.treatThrowable(this.getClass(), e,
          "An error occurred while fetching the id list: '%s'.", keyArr);
    }

    return list;
  }

  public Collection<String> getAll() {
    final String sql = SqlList.SELECT_ALL;
    final List<String> list = new ArrayList<>();

    try {
      final PreparedStatement ps = this.connection.prepareStatement(sql);
      final ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        list.add(rs.getString("wallet"));
      }

      rs.close();
    } catch (final SQLException e) {
      this.plugin.treatThrowable(this.getClass(), e,
          "An error occurred while fetching all accounts: '%s'.", e.getMessage());
    }

    return list;
  }

  @Override
  public BasePlugin getBasePlugin() {
    return this.plugin;
  }
}
