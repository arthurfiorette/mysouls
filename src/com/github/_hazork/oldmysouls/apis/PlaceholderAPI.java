package com.github._hazork.oldmysouls.apis;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.souls.SoulWallet;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPI extends PlaceholderExpansion {

  @Override
  public String onPlaceholderRequest(final Player player, @NotNull final String params) {
    if (player == null) {
      return null;
    }

    Object result = null;
    final String[] arr = params.split("_");
    final String[] args = Arrays.copyOfRange(arr, 1, arr.length);
    final SoulWallet wallet = MySouls.getDB().from(player);

    switch (arr[0]) {
      case "soulscount":
        if (args.length >= 1) {
          @SuppressWarnings("deprecation")
          final OfflinePlayer of = Bukkit.getOfflinePlayer(args[0]);
          result = wallet.soulsCount(of.getUniqueId());
          break;
        }
        result = wallet.soulsCount();
        break;

      case "playercount":
        result = wallet.playerCount();
        break;

      case "soulsratio":
        result = wallet.soulsRatio();
        break;

      default:
        return null;
    }
    return result.toString();
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public String getAuthor() {
    return MySouls.get().getDescription().getAuthors().toString();
  }

  @Override
  public String getIdentifier() {
    return this.getRequiredPlugin().toLowerCase();
  }

  @Override
  public String getVersion() {
    return MySouls.getVersion();
  }

  @Override
  public @Nullable String getRequiredPlugin() {
    return MySouls.get().getName();
  }
}
