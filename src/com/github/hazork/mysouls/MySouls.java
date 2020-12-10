package com.github.hazork.mysouls;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.github.hazork.mysouls.apis.APICoordinator;
import com.github.hazork.mysouls.apis.implementations.BStatsAPI;
import com.github.hazork.mysouls.apis.implementations.PlaceholderAPI;
import com.github.hazork.mysouls.commands.CommandHandler;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.guis.GuiDB;
import com.github.hazork.mysouls.guis.GuiListener;
import com.github.hazork.mysouls.souls.SoulListener;
import com.github.hazork.mysouls.souls.SoulsDB;
import com.github.hazork.mysouls.utils.Utils;

public class MySouls extends JavaPlugin {

    /**
     * A constant of the name used as a key to identify things from the plugin
     * or not.
     *
     * @see {@link Utils.ItemStacks#createNBT(org.bukkit.inventory.ItemStack, String)}
     */
    public static final String NAME = "mysouls";

    private static MySouls instance = null;

    private SoulsDB soulsdb;
    private GuiDB guidb;
    private CommandHandler command;
    private SoulListener soulListener;
    private GuiListener guiListener;
    private APICoordinator apiCoordinator;

    public MySouls() {
	if (instance != null) {
	    throw new RuntimeException("Plugin instance already running");
	} else {
	    instance = this;
	}
	soulsdb = new SoulsDB(this);
	guidb = new GuiDB();
	command = new CommandHandler("mysouls", Lang.MENU_COMMAND.getText());
	soulListener = new SoulListener(this);
	guiListener = new GuiListener(this);
	apiCoordinator = new APICoordinator();
	apiCoordinator.registerApi(new BStatsAPI(), new PlaceholderAPI());
    }

    @Override
    public void onEnable() {
	saveDefaultConfig();
	soulsdb.open();
	guidb.close();
	command.registerFor(this);
	soulListener.register();
	guiListener.register();
	apiCoordinator.enable();
	log(Level.INFO, "Plugin habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
	apiCoordinator.disable();
	guidb.close();
	soulsdb.close();
    }

    public static MySouls get() {
	return instance;
    }

    public static SoulsDB getDB() {
	return get().soulsdb;
    }

    public static GuiDB getGuiDB() {
	return get().guidb;
    }

    public static String getVersion() {
	return get().getDescription().getVersion();
    }

    public static void disable() {
	get().getPluginLoader().disablePlugin(get());
    }

    public static void log(Level level, String message) {
	get().getLogger().log(level, message);
    }

    public static void treatException(Class<?> clazz, String message, Throwable throwable) {
	log(Level.SEVERE, String.format("Oops! Ocorreu um erro na classe %s. Veja o aviso: \n%s", clazz.getSimpleName(),
		message));
	if (throwable != null) {
	    throwable.printStackTrace();
	}
    }
}
