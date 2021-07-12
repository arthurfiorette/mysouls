package com.github.arthurfiorette.mysouls.config;

import com.github.arthurfiorette.sinklibrary.config.PluginConfig;
import com.github.arthurfiorette.sinklibrary.config.addons.EnumConfig;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;

public class ConfigFile extends PluginConfig implements EnumConfig<Config> {

  public ConfigFile(final BasePlugin plugin) {
    super(plugin);
  }

}
