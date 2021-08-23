package com.github.arthurfiorette.mysouls.storage;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.mysouls.config.Config;
import com.github.arthurfiorette.mysouls.config.ConfigFile;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import com.github.arthurfiorette.sinklibrary.data.database.Database;
import com.github.arthurfiorette.sinklibrary.exception.sink.ComponentNotFoundException;
import com.github.arthurfiorette.sinklibrary.uuid.FastUuid;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.NonNull;
import org.sqlite.SQLiteDataSource;

public class SqliteDatabase implements Database<UUID, String> {

  @Getter
  @NonNull
  private final File file;

  @Getter
  @NonNull
  private final BasePlugin basePlugin;

  @Getter
  @NonNull
  private final SqlGenerator sqlGenerator;

  @Getter
  private final SQLiteDataSource dataSource;

  public SqliteDatabase(final MySouls plugin) {
    this.basePlugin = plugin;
    this.sqlGenerator = new SqlGenerator(plugin);
    final ConfigFile config = plugin.get(ConfigFile.class);
    this.file =
      new File(plugin.getDataFolder().getPath(), config.getString(Config.DATABASE_FILENAME));

    this.dataSource = new SQLiteDataSource();
    this.dataSource.setUrl("jdbc:sqlite:" + this.file.getAbsolutePath());
  }

  @Override
  public void enable() throws Exception {
    try (
      Connection connection = this.dataSource.getConnection();
      PreparedStatement ps = connection.prepareStatement(this.sqlGenerator.createTableSql())
    ) {
      ps.executeUpdate();
    }
  }

  @Override
  public void disable() throws Exception {}

  @Override
  public void save(final UUID key, final String value) {
    final String id = FastUuid.toString(key);
    final String sql = this.sqlGenerator.insertWalletSql();

    try (
      Connection connection = this.dataSource.getConnection();
      PreparedStatement ps = connection.prepareStatement(sql)
    ) {
      ps.setString(1, id);
      ps.setString(2, value);

      ps.executeUpdate();
    } catch (final SQLException exc) {
      this.basePlugin.getExceptionHandler()
        .handle(
          this.getClass(),
          exc,
          "An error occurred while saving the id: '%s', returning it to the cache...",
          id
        );

      // Return it back to the cache.
      try {
        final WalletStorage storage = this.basePlugin.get(WalletStorage.class);
        storage.loadJson(key, value);
        // When the component isn't registered or this manager is disabled.
      } catch (final ComponentNotFoundException e) {
        this.basePlugin.getExceptionHandler()
          .handle(this.getClass(), e, "Could not return the key %s back to the cache.", id);
      }
    }
  }

  @Override
  public String get(final UUID key) {
    final String id = FastUuid.toString(key);
    final String sql = this.sqlGenerator.selectSql();

    try (
      Connection connection = this.dataSource.getConnection();
      PreparedStatement ps = connection.prepareStatement(sql)
    ) {
      ps.setString(1, id);

      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          return rs.getString(this.sqlGenerator.walletColumnName());
        }
      }
    } catch (final SQLException ex) {
      this.basePlugin.getExceptionHandler()
        .handle(
          this.getClass(),
          ex,
          "An error occurred while fetching the id: '%s', returning a new entity...",
          id
        );
    }

    // The storage create a new key for the specified key when returning null.
    return null;
  }

  @Override
  public Collection<String> getMany(final Collection<UUID> keys) {
    final String sql =
      this.sqlGenerator.selectManySql(keys.stream().map(k -> "?").toArray(String[]::new));

    final String[] uidKeys = keys.stream().map(FastUuid::toString).toArray(String[]::new);

    final List<String> result = new ArrayList<>();

    try (
      Connection connection = this.dataSource.getConnection();
      PreparedStatement ps = connection.prepareStatement(sql)
    ) {
      int index = 1;
      for (final String id : uidKeys) {
        ps.setString(index++, id);
      }

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          result.add(rs.getString(this.sqlGenerator.walletColumnName()));
        }
      }
    } catch (final SQLException e) {
      this.basePlugin.getExceptionHandler()
        .handle(
          this.getClass(),
          e,
          "An error occurred while fetching the id list: '%s'. Returning an empty list",
          String.join(",", uidKeys)
        );
    }

    return result;
  }

  public Collection<String> getAll() {
    final String sql = this.sqlGenerator.selectAllSql();
    final List<String> result = new ArrayList<>();

    try (
      Connection connection = this.dataSource.getConnection();
      PreparedStatement ps = connection.prepareStatement(sql);
      ResultSet rs = ps.executeQuery()
    ) {
      while (rs.next()) {
        result.add(rs.getString(this.sqlGenerator.walletColumnName()));
      }
    } catch (final SQLException e) {
      this.basePlugin.getExceptionHandler()
        .handle(
          this.getClass(),
          e,
          "An error occurred while fetching all accounts: '%s'.",
          e.getMessage()
        );
    }

    return result;
  }
}
