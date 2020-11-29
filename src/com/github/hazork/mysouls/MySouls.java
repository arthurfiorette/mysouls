package com.github.hazork.mysouls;

import java.util.Objects;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.hazork.mysouls.commands.CommandHandler;
import com.github.hazork.mysouls.data.Config;
import com.github.hazork.mysouls.souls.SoulListener;
import com.github.hazork.mysouls.souls.WalletDB;

public class MySouls extends JavaPlugin {

    private static MySouls instance = null;

    private WalletDB wallet = new WalletDB(this);
    private Config config = new Config(this);
    private CommandHandler command = new CommandHandler("mysouls", "info");
    private SoulListener soulListener = new SoulListener(this);

    public MySouls() {
	if (Objects.nonNull(instance)) throw new UnsupportedOperationException(
		"A instance of" + this.getClass().getSimpleName() + "was already constructed");
	else instance = this;
    }

    @Override
    public void onEnable() {
	saveDefaultConfig();
	config.load();
	wallet.open();
	command.registerFor(this);
	soulListener.register();
    }

    @Override
    public void onDisable() {
	config.close();
	wallet.close();
    }

    public static String getVersion() {
	return get().getDescription().getVersion();
    }

    public static void log(Level level, String msg) {
	get().getLogger().log(level, msg);
    }

    public static void disable() {
	get().getPluginLoader().disablePlugin(get());
    }

    public static MySouls get() {
	return instance;
    }

    public WalletDB getWallets() {
	return this.wallet;
    }

    public static WalletDB getWalletDB() {
	return get().wallet;
    }

    public static Config getConfiguration() {
	return get().config;
    }

}
