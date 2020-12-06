package com.github.hazork.mysouls.data.config;

import java.util.List;
import java.util.stream.Collectors;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.YamlFile;

public class ConfigFile extends YamlFile {

    private static ConfigFile instance = null;

    protected ConfigFile(MySouls plugin) {
	super(plugin, plugin.getDataFolder(), "config");
    }

    public static ConfigFile get() {
	return (instance == null) ? (instance = new ConfigFile(MySouls.get())) : (instance);
    }

    public static Object get(Config config) {
	return get().config.get(config.getPath());
    }

    public static <O> O get(Class<O> clazz, O def, Config config) {
	Object obj = get(config);
	return (clazz.isInstance(obj)) ? clazz.cast(obj) : def;
    }

    public static <O> O get(Class<O> clazz, Config config) {
	return get(clazz, null, config);
    }

    public static List<?> getList(Config config) {
	return get().config.getList(config.getPath());
    }

    public static <O> List<O> getList(Class<O> clazz, List<O> def, Config config) {
	List<O> list = getList(config).stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
	return list.isEmpty() ? def : list;
    }

    public static <O> List<O> getList(Class<O> clazz, Config config) {
	return getList(clazz, null, config);
    }
}
