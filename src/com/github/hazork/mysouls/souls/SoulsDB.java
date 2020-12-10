package com.github.hazork.mysouls.souls;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.utils.db.CacheDB;

public final class SoulsDB extends CacheDB<UUID, SoulWallet> {

    private final File file;
    private final String table = "CREATE TABLE IF NOT EXISTS 'wallets' ('uuid' TEXT UNIQUE NOT NULL, 'wallet' BLOB)";

    private Connection connection;

    public SoulsDB(JavaPlugin plugin) {
	super(100, 15);
	file = new File(plugin.getDataFolder().getPath(), "database.db");
    }

    public SoulWallet from(Player player) {
	return super.from(player.getUniqueId());
    }

    @Override
    protected UUID keyFunction(SoulWallet sw) {
	return sw.getOwnerId();
    }

    @Override
    public boolean open() {
	try {
	    if (Objects.isNull(connection) || connection.isClosed()) {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
		MySouls.log(Level.INFO, "Conexão com a database aberta.");
		PreparedStatement ps = connection.prepareStatement(table);
		ps.execute();
		ps.close();
		MySouls.log(Level.INFO, "Conexão com a tabela concluída.");
		return true;
	    }
	} catch (Exception exc) {
	    treatException(exc);
	}
	return false;
    }

    @Override
    protected void save(SoulWallet sw) {
	try {
	    String sql = "INSERT OR REPLACE INTO 'wallets' (uuid, wallet) VALUES (?, ?)";
	    PreparedStatement ps = connection.prepareStatement(sql);
	    ps.setString(1, sw.ownerId.toString());
	    ps.setString(2, JSoulWallet.from(sw).getJson());
	    ps.executeUpdate();
	    ps.close();
	} catch (Exception exc) {
	    treatException(exc);
	}
    }

    @Override
    protected void load(UUID uuid) {
	try {
	    String sql = "SELECT * FROM 'wallets' WHERE uuid = ?";
	    PreparedStatement ps = connection.prepareStatement(sql);
	    ps.setString(1, uuid.toString());
	    ResultSet rs = ps.executeQuery();
	    SoulWallet wallet = null;
	    if (rs.next()) {
		wallet = JSoulWallet.from(rs.getString("wallet")).getWallet();
	    } else {
		wallet = new SoulWallet(uuid);
	    }
	    putValue(wallet);
	} catch (Exception exception) {
	    treatException(exception);
	}
    }

    @Override
    public boolean close() {
	try {
	    if (Objects.nonNull(connection)) {
		connection.close();
		MySouls.log(Level.INFO, "Conexão com a database fachada.");
		return true;
	    }
	} catch (Exception exception) {
	    treatException(exception);
	}
	return false;
    }

    private void treatException(Exception exc) {
	MySouls.treatException(getClass(), "Ocorreu um erro com a conectividade da WalletDB", exc);
    }
}
