package com.github.hazork.mysouls.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;

public class Utils {

    public static <E> E getRandom(List<E> list) {
	return list.get(getRandom(list.size()));
    }

    public static int getRandom(int range) {
	return new Random().nextInt(range);
    }

    public static boolean isMinecraftPack(int amount) {
	return (amount <= 64 && amount >= 1);
    }

    public static void closeInventory(HumanEntity entity) {
	Bukkit.getScheduler().runTask(MySouls.get(), () -> entity.closeInventory());
    }

    public static <R> List<R> listMapper(List<String> list, Function<String, R> mapper) {
	return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <R> List<R> listMapperFilter(List<String> list, Function<String, R> mapper, Predicate<R> predicate) {
	return list.stream().map(mapper).filter(predicate).collect(Collectors.toList());
    }

    public static void playSound(Sound sound, Player... players) {
	for (Player p : players) p.playSound(p.getLocation(), sound, 3.0F, 0.5F);
    }

    public static boolean nonNull(Object... objects) {
	return Arrays.stream(objects).filter(Objects::isNull).count() == 0;
    }

    public static boolean hasPlugin(String name) {
	return (Bukkit.getPluginManager().getPlugin(name) != null);
    }

    public static boolean hasEmptySlot(Player player) {
	return player.getInventory().firstEmpty() != -1;
    }
}
