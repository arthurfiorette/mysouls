package com.github.hazork.mysouls.data.lang;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.YamlFile;
import com.google.common.collect.Lists;

import me.clip.placeholderapi.PlaceholderAPI;

public class LangFile extends YamlFile {

    private static LangFile instance = null;

    protected LangFile(MySouls plugin) {
	super(plugin, plugin.getDataFolder(), "lang");
	replace(null, false);
    }

    public static LangFile get() {
	return (instance == null) ? (instance = new LangFile(MySouls.get())) : (instance);
    }

    public static String getString(Lang lang) {
	try {
	    return applyColor(get().config.getString(lang.getPath()));
	} catch (NullPointerException e) {
	    MySouls.treatException(Lang.class,
		    String.format("%s está retornando nulo, veja na lang.yml", lang.getPath()), e);
	    return "";
	}
    }

    public static String getStringFormat(Lang lang, Map<String, String> placeholders) {
	return setInternalPlaceholders(getString(lang), placeholders);
    }

    public static String getStringFormat(Lang lang, OfflinePlayer player) {
	return setPAPIPlaceholders(getString(lang), player);
    }

    public static String getStringFormat(Lang lang, Map<String, String> placeholders, OfflinePlayer player) {
	return setPlaceholders(getString(lang), placeholders, player);
    }

    public static List<String> getList(Lang lang) {
	try {
	    return mapStringList(get().config.getStringList(lang.getPath()), LangFile::applyColor);
	} catch (NullPointerException e) {
	    MySouls.treatException(Lang.class,
		    String.format("%s está retornando nulo, veja na lang.yml", lang.getPath()), e);
	    return Lists.newArrayList();
	}
    }

    public static List<String> getListFormat(Lang lang, Map<String, String> placeholders) {
	return mapStringList(getList(lang), str -> setInternalPlaceholders(str, placeholders));
    }

    public static List<String> getListFormat(Lang lang, OfflinePlayer player) {
	return mapStringList(getList(lang), str -> setPAPIPlaceholders(str, player));
    }

    public static List<String> getListFormat(Lang lang, Map<String, String> placeholders, OfflinePlayer player) {
	return mapStringList(getList(lang), str -> setPlaceholders(str, placeholders, player));
    }

    /* PRIVATE */

    private static String applyColor(String text) {
	return ChatColor.translateAlternateColorCodes('&', text);
    }

    private static List<String> mapStringList(List<String> list, Function<String, String> mapper) {
	return list.stream().map(mapper).collect(Collectors.toList());
    }

    private static String setPlaceholders(String text, Map<String, String> placeholders, OfflinePlayer player) {
	return setPAPIPlaceholders(setInternalPlaceholders(text, placeholders), player);
    }

    private static String setPAPIPlaceholders(String text, OfflinePlayer player) {
	return PlaceholderAPI.setPlaceholders(player, text);
    }

    private static String setInternalPlaceholders(String text, Map<String, String> placeholders) {
	if (placeholders.isEmpty()) return text;
	for (Entry<String, String> entry : placeholders.entrySet())
	    if (text.contains(entry.getKey())) text = text.replace(entry.getKey(), entry.getValue());
	return text;
    }

}
