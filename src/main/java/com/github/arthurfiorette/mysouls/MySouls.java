package com.github.arthurfiorette.mysouls;

import com.github.arthurfiorette.mysouls.commands.Commands;
import com.github.arthurfiorette.mysouls.config.ConfigFile;
import com.github.arthurfiorette.mysouls.extensions.BStatsService;
import com.github.arthurfiorette.mysouls.extensions.PapiService;
import com.github.arthurfiorette.mysouls.lang.LangFile;
import com.github.arthurfiorette.mysouls.listeners.ChatListener;
import com.github.arthurfiorette.mysouls.listeners.DeathListener;
import com.github.arthurfiorette.mysouls.listeners.ItemListener;
import com.github.arthurfiorette.mysouls.menu.MenusStorage;
import com.github.arthurfiorette.mysouls.storage.WalletDatabase;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.mysouls.util.SoulsExecutor;
import com.github.arthurfiorette.sinklibrary.components.SinkPlugin;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseComponent;

public class MySouls extends SinkPlugin {

  public static final String VERSION = "v2.0.0";

  @Override
  public void enable() throws Exception {}

  @Override
  public void disable() throws Exception {}

  @Override
  protected BaseComponent[] components() {
    return new BaseComponent[] {
        // Configuration files
        new LangFile(this), new ConfigFile(this),

        // Async executor
        new SoulsExecutor(this),

        // Wallet persistence
        new WalletDatabase(this), new WalletStorage(this),

        // Menus and commands storage
        new MenusStorage(this), new Commands(this),

        // Listeners
        new ItemListener(this), new DeathListener(this), new ChatListener(this),

        // Extensions
        new BStatsService(this), new PapiService(this), };
  }
}
