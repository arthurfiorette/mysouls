package com.github.hazork.mysouls.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

public class Spigots {

    public static boolean hasPlugin(String name) {
	return (Bukkit.getPluginManager().getPlugin(name) != null);
    }

    public static boolean isSelfKill(EntityDeathEvent event) {
	return event.getEntity().equals(event.getEntity().getKiller());
    }

    public static void callEvent(Event event) {
	Bukkit.getPluginManager().callEvent(event);
    }

    public static boolean isPlayer(Object obj) {
	return obj instanceof Player;
    }

}
