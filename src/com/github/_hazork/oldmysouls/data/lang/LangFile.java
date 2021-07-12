package com.github._hazork.oldmysouls.data.lang;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.data.YamlFile;
import com.github._hazork.oldmysouls.utils.Utils;
import com.google.common.collect.Lists;

public class LangFile extends YamlFile {

  private static final String UNKNOWN_VALUE = "§7Unknown Text.";

  private static LangFile instance = null;

  protected LangFile(final MySouls plugin) {
    super(plugin, plugin.getDataFolder(), "lang");
    this.replace(null, false);
  }

  public static LangFile get() {
    return (LangFile.instance == null) ? (LangFile.instance = new LangFile(MySouls.get()))
        : (LangFile.instance);
  }

  public static String getString(final Lang lang) {
    final String value = LangFile.get().config.getString(lang.getPath());
    if (value == null) {
      LangFile.warnNPE(lang);
      return LangFile.UNKNOWN_VALUE;
    }
    return LangFile.setColorful(value);
  }

  public static List<String> getList(final Lang lang) {
    final List<String> value = LangFile.get().config.getStringList(lang.getPath());
    if (value == null) {
      LangFile.warnNPE(lang);
      return Lists.newArrayList(LangFile.UNKNOWN_VALUE);
    }
    return Utils.listMapper(value, LangFile::setColorful);
  }

  public static String getString(final Lang lang, final String key, final String value) {
    return LangFile.getString(lang).replace(key, value);
  }

  public static String getString(final Lang lang, final Map<String, String> placeholders) {
    return LangFile.setInternalPlaceholders(LangFile.getString(lang), placeholders);
  }

  public static List<String> getList(final Lang lang, final String key, final String value) {
    return Utils.listMapper(LangFile.getList(lang), str -> str.replace(key, value));
  }

  public static List<String> getList(final Lang lang, final Map<String, String> placeholders) {
    return Utils.listMapper(LangFile.getList(lang),
        str -> LangFile.setInternalPlaceholders(str, placeholders));
  }

  /* PRIVATE */

  private static String setColorful(final String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }

  private static String setInternalPlaceholders(String text,
      final Map<String, String> placeholders) {
    for(final Entry<String, String> entry: placeholders.entrySet()) {
      if (text.contains(entry.getKey())) {
        text = text.replace(entry.getKey(), entry.getValue());
      }
    }
    return text;
  }

  private static void warnNPE(final Lang cause) {
    MySouls.treatException(Lang.class,
        String.format("%s is returning null, see it on lang.yml", cause.getPath()), null);
  }
}
