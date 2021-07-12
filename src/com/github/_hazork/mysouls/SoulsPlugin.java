package com.github._hazork.mysouls;

import com.github._hazork.mysouls.commands.InfoArgument;
import com.github._hazork.mysouls.data.SoulsDatabase;
import com.github._hazork.mysouls.menu.MenuDatabase;
import com.github._hazork.mysouls.papi.SoulsPlaceholderAPI;
import com.github._hazork.mysouls.souls.SoulListener;
import com.github._hazork.mysouls.souls.SoulsStorage;
import com.github._hazork.oldmysouls.utils.Utils;
import com.github.arthurfiorette.sinklibrary.components.SinkPlugin;
import java.util.logging.Level;
import org.bstats.bukkit.Metrics;

public class SoulsPlugin extends SinkPlugin {

  private static SoulsPlugin instance = null;
  public static final int BSTATS_ID = 9601;

  private Metrics metrics;
  private SoulsPlaceholderAPI papi;
  private SoulsStorage storage;
  private SoulListener soulListener;
  private CommandBase command;
  private MenuDatabase menuDatabase;

  public SoulsPlugin() {
    if (SoulsPlugin.get() != null) {
      throw new RuntimeException("Instance already exists");
    }
    SoulsPlugin.instance = this;
  }

  @Override
  public void onEnable() {
    this.storage = new SoulsStorage(new SoulsDatabase(this));
    this.soulListener = new SoulListener(this);
    this.menuDatabase = new MenuDatabase();

    this.setupCommands();
    this.setupApis();

    addRegistrables(this.soulListener);
  }

  @Override
  public void onDisable() {}

  private void setupCommands() {
    this.command = new CommandBase(this, "mysouls");
    this.command.addArguments(new InfoArgument());
    this.command.register();
  }

  private void setupApis() {
    this.metrics = new Metrics(this, SoulsPlugin.BSTATS_ID);

    final boolean hasPapi = Utils.hasPlugin("PlaceholderAPI");
    if (hasPapi) {
      this.papi = new SoulsPlaceholderAPI();
      this.papi.register();
    }

    log(Level.INFO, "Apis: bStats - %s, Papi - %s", this.metrics.isEnabled(), hasPapi);
  }

  public void disable() {
    this.getPluginLoader().disablePlugin(this);
  }

  public SoulsStorage getStorage() {
    return this.storage;
  }

  public MenuDatabase getMenuDatabase() {
    return this.menuDatabase;
  }

  public static String getVersion() {
    return SoulsPlugin.get().getDescription().getVersion();
  }

  public static SoulsPlugin get() {
    return SoulsPlugin.instance;
  }
}
