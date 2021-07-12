package com.github._hazork.oldmysouls.data;

import com.github._hazork.oldmysouls.MySouls;
import java.io.File;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class YamlFile {

  protected final MySouls plugin;
  protected final String name;
  protected final File file;
  protected FileConfiguration config;

  protected YamlFile(final MySouls plugin, final File folder, final String name) {
    this.plugin = plugin;
    this.name = (name.endsWith(".yml") ? name : name + ".yml");
    this.file = new File(folder, this.name);
    this.load();
  }

  public void load() {
    this.config = YamlConfiguration.loadConfiguration(this.getFile());
  }

  public void replace(final String path, final boolean replace) {
    this.plugin.saveResource(path == null ? this.name : path, replace);
    this.load();
  }

  public FileConfiguration getConfig(final boolean reload) {
    if (reload) {
      this.load();
    }
    return this.config;
  }

  public File getFile() {
    return this.file;
  }

  public String getName() {
    return this.name;
  }

  public MySouls getPlugin() {
    return this.plugin;
  }
}
