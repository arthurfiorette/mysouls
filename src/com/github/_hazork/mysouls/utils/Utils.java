package com.github._hazork.mysouls.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Utils {

  public static void playSound(final Sound sound, final Player... players) {
    for(final Player p: players) {
      p.playSound(p.getLocation(), sound, 3.0F, 0.5F);
    }
  }

}
