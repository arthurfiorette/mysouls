package com.github._hazork.oldmysouls.souls;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.utils.db.CacheDB;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SoulsDB extends CacheDB<UUID, SoulWallet> {

  private final File file;
  private final String table =
    "CREATE TABLE IF NOT EXISTS 'wallets' ('uuid' TEXT UNIQUE NOT NULL, 'wallet' BLOB)";

  private Connection connection;

  public SoulsDB(final JavaPlugin plugin) {
    super(100, 15);
    this.file = new File(plugin.getDataFolder().getPath(), "database.db");
  }

  public SoulWallet from(final Player player) {
    return super.from(player.getUniqueId());
  }

  @Override
  protected UUID keyFunction(final SoulWallet sw) {
    return sw.getOwnerId();
  }

  @Override
  public boolean open() {
    try {
      if (Objects.isNull(this.connection) || this.connection.isClosed()) {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getAbsolutePath());
        final PreparedStatement ps = this.connection.prepareStatement(this.table);
        ps.execute();
        ps.close();
        return true;
      }
    } catch (final Exception exc) {
      this.treatException(exc);
    }
    return false;
  }

  @Override
  protected void save(final SoulWallet sw) {
    try {
      final String sql = "INSERT OR REPLACE INTO 'wallets' (uuid, wallet) VALUES (?, ?)";
      final PreparedStatement ps = this.connection.prepareStatement(sql);
      ps.setString(1, sw.getOwnerId().toString());
      ps.setString(2, JSoulWallet.from(sw).getJson());
      ps.executeUpdate();
      ps.close();
    } catch (final Exception exc) {
      MySouls.treatException(
        this.getClass(),
        "An error occurred while saving the user " +
        sw.asPlayer().getName() +
        " in the database and it was returned to the cache.",
        exc
      );
      Bukkit
        .getScheduler()
        .scheduleSyncDelayedTask(MySouls.get(), () -> this.getMap().put(sw.getOwnerId(), sw));
    }
  }

  @Override
  protected void load(final UUID uuid) {
    try {
      final String sql = "SELECT * FROM 'wallets' WHERE uuid = ?";
      final PreparedStatement ps = this.connection.prepareStatement(sql);
      ps.setString(1, uuid.toString());
      final ResultSet rs = ps.executeQuery();
      SoulWallet wallet = null;
      if (rs.next()) {
        wallet = JSoulWallet.from(rs.getString("wallet")).getWallet();
      } else {
        wallet = new SoulWallet(uuid);
      }
      this.putValue(wallet);
    } catch (final Exception exception) {
      this.treatException(exception);
    }
  }

  @Override
  public boolean close() {
    try {
      if (Objects.nonNull(this.connection)) {
        this.getMap().close();
        this.connection.close();
        MySouls.log(Level.INFO, "Conexão com a database fachada.");
        return true;
      }
    } catch (final Exception exception) {
      this.treatException(exception);
    }
    return false;
  }

  private void treatException(final Exception exc) {
    MySouls.treatException(
      this.getClass(),
      "Ocorreu um erro com a conectividade da WalletDB: " + exc.getMessage(),
      exc
    );
  }
}
