package com.github.hazork.mysouls.data.lang;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.YamlFile;

import me.clip.placeholderapi.PlaceholderAPI;

public class LangFile extends YamlFile {

    private static LangFile instance = null;

    protected LangFile(MySouls plugin) {
	super(plugin, plugin.getDataFolder(), "lang"); // TODO get the
	replace(null, false);
    }

    public static LangFile get() {
	return (instance == null) ? (instance = new LangFile(MySouls.get())) : (instance);
    }

    public static String getString(Lang lang) {
	return get().config.getString(lang.getPath());
    }

    public static String getStringFormat(Lang lang, Map<String, String> placeholders) {
	return setInternalPlaceholders(getString(lang), placeholders);
    }

    public static String getStringFormat(Lang lang, Player player) {
	return setPAPIPlaceholders(getString(lang), player);
    }

    public static String getStringFormat(Lang lang, Map<String, String> placeholders, Player player) {
	return setPlaceholders(getString(lang), placeholders, player);
    }

    public static List<String> getList(Lang lang) {
	return get().config.getStringList(lang.getPath());
    }

    public static List<String> getListFormat(Lang lang, Map<String, String> placeholders) {
	return mapStringList(getList(lang), str -> setInternalPlaceholders(str, placeholders));
    }

    public static List<String> getListFormat(Lang lang, Player player) {
	return mapStringList(getList(lang), str -> setPAPIPlaceholders(str, player));
    }

    public static List<String> getListFormat(Lang lang, Map<String, String> placeholders, Player player) {
	return mapStringList(getList(lang), str -> setPlaceholders(str, placeholders, player));
    }

    /* PRIVATE */

    private static List<String> mapStringList(List<String> list, Function<String, String> mapper) {
	return list.stream().map(mapper).collect(Collectors.toList());
    }

    private static String setPlaceholders(String text, Map<String, String> placeholders, Player player) {
	return setPAPIPlaceholders(setInternalPlaceholders(text, placeholders), player);
    }

    private static String setPAPIPlaceholders(String text, Player player) {
	return PlaceholderAPI.setPlaceholders(player, text);
    }

    private static String setInternalPlaceholders(String text, Map<String, String> placeholders) {
	if (placeholders.isEmpty()) return text;
	placeholders.entrySet().stream().filter(e -> text.contains(e.getKey()))
		.forEach(e -> text.replaceAll(e.getKey(), e.getValue()));
	return text;
    }

}
