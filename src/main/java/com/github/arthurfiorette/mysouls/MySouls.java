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
import com.github.arthurfiorette.mysouls.storage.SqliteDatabase;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.sinklibrary.component.loaders.ComponentLoader;
import com.github.arthurfiorette.sinklibrary.core.SinkOptions.Consumer;
import com.github.arthurfiorette.sinklibrary.core.SinkPlugin;
import com.github.arthurfiorette.sinklibrary.logging.FilteredConsoleLogger;
import com.github.arthurfiorette.sinklibrary.logging.Level;

public class MySouls extends SinkPlugin {

  public static final String VERSION = "v2.0.0";

  @Override
  public ComponentLoader[] components() {
    return ComponentLoader.reflect(this,
        // Configuration files
        LangFile.class, ConfigFile.class,
        // Wallet persistence
        SqliteDatabase.class, WalletStorage.class,
        // Menus and commands storage
        MenusStorage.class, Commands.class,
        // Listeners
        ItemListener.class, DeathListener.class, ChatListener.class,
        // Extensions
        BStatsService.class, PapiService.class);
  }

  @Override
  public Consumer options() {
    return (builder) -> {
      builder.baseLogger(new FilteredConsoleLogger(this, Level.ALL));
    };
  }

}
