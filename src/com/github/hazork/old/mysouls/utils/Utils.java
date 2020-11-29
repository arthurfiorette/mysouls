package com.github.hazork.old.mysouls.utils;

import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

    public static <E> E poll(List<E> list) {
	E element = list.get(getRandom(list.size()));
	list.remove(element);
	return element;
    }

    public static int getRandom(int range) {
	return new Random().nextInt(range);
    }

    public static void sendItem(Player player, ItemStack item) {
	if (player.getInventory().firstEmpty() != -1) {
	    player.getInventory().addItem(item);
	}
    }

}
