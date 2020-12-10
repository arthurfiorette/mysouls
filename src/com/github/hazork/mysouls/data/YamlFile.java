package com.github.hazork.mysouls.data;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.github.hazork.mysouls.MySouls;

public abstract class YamlFile {

    protected final MySouls plugin;
    protected final String name;
    protected final File file;
    protected FileConfiguration config;

    protected YamlFile(MySouls plugin, File folder, String name) {
	this.plugin = plugin;
	this.name = (name.endsWith(".yml") ? name : name + ".yml");
	file = new File(folder, this.name);
	load();
    }

    public void load() {
	config = YamlConfiguration.loadConfiguration(getFile());
    }

    public void replace(String path, boolean replace) {
	plugin.saveResource(path == null ? name : path, replace);
	load();
    }

    public FileConfiguration getConfig(boolean reload) {
	if (reload) {
	    load();
	}
	return config;
    }

    public File getFile() {
	return file;
    }

    public String getName() {
	return name;
    }

    public MySouls getPlugin() {
	return plugin;
    }
}
