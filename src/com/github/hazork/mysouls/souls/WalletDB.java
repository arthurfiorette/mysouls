package com.github.hazork.mysouls.souls;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.util.CacheDB;

public class WalletDB extends CacheDB<UUID, SoulWallet> {

    private final File file;
    private final String table = "CREATE TABLE IF NOT EXISTS 'wallets' ('uuid' TEXT UNIQUE NOT NULL, 'wallet' BLOB)";

    private Connection connection;

    public WalletDB(JavaPlugin plugin) {
	super(20, 15);
	file = new File(plugin.getDataFolder().getPath(), "database.db");
    }

    public SoulWallet from(Player player) {
	return super.from(player.getUniqueId());
    }

    @Override
    protected UUID keyFunction(SoulWallet value) {
	return value.getOwnerId();
    }

    @Override
    public boolean open() {
	try {
	    if (Objects.isNull(connection)) {
		Class.forName("org.sqlite.JDBC");
		connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
		MySouls.log(Level.INFO, "Conexão com a database aberta.");
		PreparedStatement ps = connection.prepareStatement(table);
		ps.execute();
		ps.close();
		MySouls.log(Level.INFO, "Conexão com a tabela concluída.");
		return true;
	    }
	} catch (SQLException exception) {
	    treatException(exception);
	} catch (ClassNotFoundException exception) {
	    MySouls.log(Level.SEVERE, "SQLite driver não foi encontrado.");
	    exception.printStackTrace();
	}
	return false;
    }

    @Override
    protected void save(SoulWallet wallet) {
	System.out.println(wallet.souls.size());
	try {
	    String sql = "INSERT OR REPLACE INTO 'wallets' (uuid, wallet) VALUES (?, ?)";
	    PreparedStatement ps = connection.prepareStatement(sql);
	    ps.setString(1, wallet.ownerId.toString());
	    ps.setString(2, JSoulWallet.from(wallet).getJson());
	    ps.executeUpdate();
	    ps.close();
	} catch (SQLException exception) {
	    treatException(exception);
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
	    if (rs.next()) wallet = JSoulWallet.from(rs.getString("wallet")).getWallet();
	    else wallet = new SoulWallet(uuid);
	    putCache(wallet);
	} catch (SQLException exception) {
	    treatException(exception);
	}
    }

    @Override
    public boolean close() {
	try {
	    if (Objects.nonNull(connection)) {
		connection.close();
		connection = null;
		MySouls.log(Level.INFO, "Conexão com a database fachada.");
	    }
	} catch (SQLException exception) {
	    treatException(exception);
	}
	return false;
    }

    private void treatException(SQLException sql) {
	MySouls.log(Level.SEVERE, "Ocorreu um erro com a conectividade da WalletDB. (" + sql.getMessage() + ")");
	sql.printStackTrace();
    }
}
