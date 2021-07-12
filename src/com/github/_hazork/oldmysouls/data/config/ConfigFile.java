package com.github._hazork.oldmysouls.data.config;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.data.YamlFile;

public class ConfigFile extends YamlFile {

  private static ConfigFile instance = null;
  public static final int FILE_VERSION = 1;

  protected ConfigFile(final MySouls plugin) {
    super(plugin, plugin.getDataFolder(), "config");
  }

  public static ConfigFile get() {
    return (ConfigFile.instance == null) ? (ConfigFile.instance = new ConfigFile(MySouls.get()))
        : (ConfigFile.instance);
  }

  public static String getText(final Config config) {
    return ConfigFile.get().config.getString(config.getPath());
  }

  public static int getInteger(final Config config) {
    return ConfigFile.get().config.getInt(config.getPath());
  }
}
