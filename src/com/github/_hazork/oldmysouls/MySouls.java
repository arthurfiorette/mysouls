package com.github._hazork.oldmysouls;

import com.github._hazork.oldmysouls.apis.APIController;
import com.github._hazork.oldmysouls.commands.CommandHandler;
import com.github._hazork.oldmysouls.data.lang.Lang;
import com.github._hazork.oldmysouls.guis.GuiDB;
import com.github._hazork.oldmysouls.guis.GuiListener;
import com.github._hazork.oldmysouls.souls.SoulListener;
import com.github._hazork.oldmysouls.souls.SoulsDB;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public class MySouls extends JavaPlugin {

  private static MySouls instance = null;

  private final SoulsDB soulsdb;
  private final GuiDB guidb;
  private final CommandHandler command;
  private final SoulListener soulListener;
  private final GuiListener guiListener;
  private final APIController apiController;

  public MySouls() {
    if (MySouls.instance != null) {
      throw new RuntimeException("Plugin instance already running");
    } else {
      MySouls.instance = this;
    }
    this.soulsdb = new SoulsDB(this);
    this.guidb = new GuiDB();
    this.command = new CommandHandler("mysouls", Lang.MENU_COMMAND.getText());
    this.soulListener = new SoulListener(this);
    this.guiListener = new GuiListener(this);
    this.apiController = new APIController();
  }

  @Override
  public void onEnable() {
    this.saveDefaultConfig();
    this.soulsdb.open();
    this.guidb.close();
    this.command.registerFor(this);
    this.soulListener.register();
    this.guiListener.register();
    this.apiController.enable();
  }

  @Override
  public void onDisable() {
    this.guidb.close();
    this.soulsdb.close();
  }

  public static MySouls get() {
    return MySouls.instance;
  }

  public static SoulsDB getDB() {
    return MySouls.get().soulsdb;
  }

  public static GuiDB getGuiDB() {
    return MySouls.get().guidb;
  }

  public static String getVersion() {
    return MySouls.get().getDescription().getVersion();
  }

  public static void disable() {
    MySouls.get().getPluginLoader().disablePlugin(MySouls.get());
  }

  public static void log(final Level level, final String message) {
    MySouls.get().getLogger().log(level, message);
  }

  public static void treatException(
    final Class<?> clazz,
    final String message,
    final Throwable throwable
  ) {
    MySouls.log(
      Level.SEVERE,
      String.format(
        "Oops! Ocorreu um erro na classe %s. Veja o aviso: \n%s",
        clazz.getSimpleName(),
        message
      )
    );
    if (throwable != null) {
      throwable.printStackTrace();
    }
  }
}
