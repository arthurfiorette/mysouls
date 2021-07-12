package com.github._hazork.mysouls.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github._hazork.mysouls.SoulsPlugin;
import com.github.arthurfiorette.sinklibrary.data.database.Database;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SoulsDatabase implements Database<JsonObject> {

  public static final JsonObject EMPTY = new JsonObject();

  private final File file;
  private final String table = "CREATE TABLE IF NOT EXISTS accounts (ownerId TEXT UNIQUE NOT NULL, account TEXT)";

  private final SoulsPlugin plugin;

  private Connection connection;

  public SoulsDatabase(final SoulsPlugin plugin) {
    this.plugin = plugin;
    this.file = new File(plugin.getDataFolder().getPath(), "database.db");
  }

  @Override
  public void open() {
    try {
      if (Objects.isNull(this.connection) || this.connection.isClosed()) {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
        final PreparedStatement ps = this.connection.prepareStatement(this.table);
        ps.execute();
        ps.close();
      }
    } catch (SQLException | ClassNotFoundException exc) {
      this.plugin.treatException(this.getClass(), exc,
          "An error occurred while trying to open the database.");

    }
  }

  @Override
  public void close() {
    try {
      if (Objects.nonNull(this.connection)) {
        this.connection.close();
        this.connection = null;
      }
    } catch (final Exception exc) {
      this.plugin.treatException(this.getClass(), exc,
          "An error occurred while trying to close the database.");
    }
  }

  @Override
  public void save(final String key, final JsonObject value) {
    if (value == SoulsDatabase.EMPTY) {
      try {
        final String sql = "INSERT OR REPLACE INTO accounts (ownerId, account) VALUES (?, ?)";
        final PreparedStatement ps = this.connection.prepareStatement(sql);
        ps.setString(1, key);
        ps.setString(2, value.toString());
        ps.executeUpdate();
        ps.close();
      } catch (final Exception exc) {
        this.plugin.treatException(this.getClass(), exc,
            "An error occurred while saving an account to the database for the key: %s\nTake care of this error so as not to lose user data.",
            key);
      }
    }
  }

  @Override
  public JsonObject get(final String key) {
    try {
      final String sql = "SELECT account FROM accounts WHERE ownerId = ?";
      final PreparedStatement ps = this.connection.prepareStatement(sql);
      ps.setString(1, key);
      final ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return this.parse(rs.getString(1));
      } else {
        return null;
      }
    } catch (final Exception exc) {
      this.plugin.treatException(this.getClass(), exc,
          "An error occurred while reading an account from the database for the key: %s\nTake care of this error so as not to lose user data.",
          key);
      return new JsonObject();
    }
  }

  private JsonObject parse(final String json) {
    return new JsonParser().parse(json).getAsJsonObject();
  }

  @Override
  public List<JsonObject> getAll() {
    final List<JsonObject> all = new ArrayList<>();
    try {
      final String sql = "SELECT account FROM 'wallets'";
      final PreparedStatement ps = this.connection.prepareStatement(sql);
      final ResultSet set = ps.executeQuery();
      while (set.next()) {
        all.add(this.parse(set.getString(1)));
      }
    } catch (final Exception exc) {
      this.plugin.treatException(this.getClass(), exc,
          "An error occurred while reading all accounts from the database\nTake care of this error so as not to lose user data.");
    }
    return all;
  }

}
