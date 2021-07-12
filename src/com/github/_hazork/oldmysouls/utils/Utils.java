package com.github._hazork.oldmysouls.utils;

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

import com.github._hazork.oldmysouls.MySouls;

public class Utils {

  public static <E> E getRandom(final List<E> list) {
    return list.get(Utils.random(list.size()));
  }

  public static int random(final int range) {
    return new Random().nextInt(range);
  }

  public static boolean isMinecraftPack(final int amount) {
    return ((amount <= 64) && (amount >= 1));
  }

  public static void closeInventory(final HumanEntity entity) {
    Bukkit.getScheduler().runTask(MySouls.get(), () -> entity.closeInventory());
  }

  public static <R> List<R> listMapper(final List<String> list, final Function<String, R> mapper) {
    return list.stream().map(mapper).collect(Collectors.toList());
  }

  public static <R> List<R> listMapperFilter(final List<String> list,
      final Function<String, R> mapper, final Predicate<R> predicate) {
    return list.stream().map(mapper).filter(predicate).collect(Collectors.toList());
  }

  public static void playSound(final Sound sound, final Player... players) {
    for(final Player p: players) {
      p.playSound(p.getLocation(), sound, 3.0F, 0.5F);
    }
  }

  public static boolean nonNull(final Object... objects) {
    return Arrays.stream(objects).filter(Objects::isNull).count() == 0;
  }

  public static boolean hasPlugin(final String name) {
    return Bukkit.getPluginManager().getPlugin(name) != null;
  }

  public static boolean hasEmptySlot(final Player player) {
    return player.getInventory().firstEmpty() != -1;
  }
}
