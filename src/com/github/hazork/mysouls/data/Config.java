package com.github.hazork.mysouls.data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;

import com.github.hazork.mysouls.MySouls;

public class Config {

    private final MySouls mysouls;

    private FileConfiguration config = null;

    public Config(MySouls mysouls) {
	this.mysouls = mysouls;
    }

    public void load() {
	if (Objects.nonNull(config)) mysouls.reloadConfig();
	config = mysouls.getConfig();
    }

    public void close() {
	config = null;
    }

    public static Object get(String path) {
	return MySouls.getConfiguration().config.get(path);
    }

    public static List<?> getList(String path) {
	return MySouls.getConfiguration().config.getList(path);
    }

    public static <O> O get(Class<O> clazz, O def, String path) {
	Object obj = get(path);
	return (clazz.isInstance(obj)) ? clazz.cast(obj) : def;
    }

    public static <O> O get(Class<O> clazz, String path) {
	return get(clazz, null, path);
    }

    public static <O> List<O> getList(Class<O> clazz, List<O> def, String path) {
	List<O> list = getList(path).stream().filter(clazz::isInstance).map(clazz::cast).collect(Collectors.toList());
	return list.isEmpty() ? def : list;
    }

    public static <O> List<O> getList(Class<O> clazz, String path) {
	return getList(clazz, null, path);
    }

}
