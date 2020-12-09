package com.github.hazork.mysouls.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static <E> E poll(List<E> list) {
	int index = getRandom(list.size());
	E element = list.get(index);
	list.remove(index);
	return element;
    }

    public static int getRandom(int range) {
	return new Random().nextInt(range);
    }

    public static <O> List<O> multiply(O source, int mult) {
	if (mult <= 0) return new ArrayList<>();
	else {
	    List<O> list = new ArrayList<>();
	    for (int i = 0; i < mult; i++) list.add(source);
	    return list;
	}
    }

    public static boolean isMinecraftPack(int amount) {
	return (amount <= 64 && amount >= 1);
    }

    public static <R> List<R> listMapper(List<String> list, Function<String, R> mapper) {
	return list.stream().map(mapper).collect(Collectors.toList());
    }

    public static <R> List<R> listMapperFilter(List<String> list, Function<String, R> mapper, Predicate<R> predicate) {
	return list.stream().map(mapper).filter(predicate).collect(Collectors.toList());
    }

    public static void sendMessageFormat(Player player, String message) {
	player.sendMessage(String.format("§5[MySouls] §7%s", message));
    }

    public static void sendMessageFormat(Player player, String message, Object... args) {
	sendMessageFormat(player, String.format(message, args));
    }

    public static void playSound(Player player, Sound sound) {
	player.playSound(player.getLocation(), sound, 3.0F, 0.5F);
    }

    public static class Spigots {

	public static boolean hasPlugin(String name) {
	    return (Bukkit.getPluginManager().getPlugin(name) != null);
	}

	public static boolean hasEmptySlot(Player player) {
	    return player.getInventory().firstEmpty() != -1;
	}

	public static boolean hasEmptySpace(Player player, int amount) {
	    if (!hasEmptySlot(player)) return false;
	    return ((getEmptySlots(player) * 64) >= amount);
	}

	public static int getEmptySlots(Player player) {
	    int space = 0;
	    for (ItemStack is : player.getInventory().getContents())
		if (is == null || is.getType() == Material.AIR) space += 64;
	    return space;
	}

	public static boolean isSelfKill(EntityDeathEvent event) {
	    return event.getEntity().equals(event.getEntity().getKiller());
	}

	public static void callEvent(Event event) {
	    Bukkit.getPluginManager().callEvent(event);
	}

	public static boolean isPlayer(Object... objs) {
	    for (Object obj : objs) if (!isPlayer(obj)) return false;
	    return true;
	}

	public static boolean isPlayer(Object obj) {
	    return obj instanceof Player;
	}
    }
}
