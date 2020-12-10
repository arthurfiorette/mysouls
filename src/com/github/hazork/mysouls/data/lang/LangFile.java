package com.github.hazork.mysouls.data.lang;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.YamlFile;
import com.github.hazork.mysouls.utils.Utils;
import com.google.common.collect.Lists;

public class LangFile extends YamlFile {

    private static final String UNKNOWN_VALUE = "§7Unknown Text.";

    private static LangFile instance = null;

    protected LangFile(MySouls plugin) {
	super(plugin, plugin.getDataFolder(), "lang");
	replace(null, false);
    }

    public static LangFile get() {
	return (instance == null) ? (instance = new LangFile(MySouls.get())) : (instance);
    }

    public static String getString(Lang lang) {
	String value = get().config.getString(lang.getPath());
	if (value == null) {
	    warnNPE(lang);
	    return UNKNOWN_VALUE;
	}
	return setColorful(value);
    }

    public static List<String> getList(Lang lang) {
	List<String> value = get().config.getStringList(lang.getPath());
	if (value == null) {
	    warnNPE(lang);
	    return Lists.newArrayList(UNKNOWN_VALUE);
	}
	return Utils.listMapper(value, LangFile::setColorful);
    }

    public static String getString(Lang lang, String key, String value) {
	return getString(lang).replace(key, value);
    }

    public static String getString(Lang lang, Map<String, String> placeholders) {
	return setInternalPlaceholders(getString(lang), placeholders);
    }

    public static List<String> getList(Lang lang, String key, String value) {
	return Utils.listMapper(getList(lang), str -> str.replace(key, value));
    }

    public static List<String> getList(Lang lang, Map<String, String> placeholders) {
	return Utils.listMapper(getList(lang), str -> setInternalPlaceholders(str, placeholders));
    }

    /* PRIVATE */

    private static String setColorful(String text) {
	return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static String setInternalPlaceholders(String text, Map<String, String> placeholders) {
	for (Entry<String, String> entry : placeholders.entrySet()) {
	    if (text.contains(entry.getKey())) {
		text = text.replace(entry.getKey(), entry.getValue());
	    }
	}
	return text;
    }

    private static void warnNPE(Lang cause) {
	MySouls.treatException(Lang.class, String.format("%s is returning null, see it on lang.yml", cause.getPath()),
		null);
    }
}
