package com.github.hazork.mysouls.data.config;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.YamlFile;

public class ConfigFile extends YamlFile {

    private static ConfigFile instance = null;
    public static final int FILE_VERSION = 1;

    protected ConfigFile(MySouls plugin) {
	super(plugin, plugin.getDataFolder(), "config");
    }

    public static ConfigFile get() {
	return (instance == null) ? (instance = new ConfigFile(MySouls.get())) : (instance);
    }

    public static String getText(Config config) {
	return get().config.getString(config.getPath());
    }

    public static int getInteger(Config config) {
	return get().config.getInt(config.getPath());
    }
}
