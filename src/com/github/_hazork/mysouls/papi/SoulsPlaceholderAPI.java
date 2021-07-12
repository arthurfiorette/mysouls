package com.github._hazork.mysouls.papi;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.github._hazork.mysouls.SoulsPlugin;
import com.github._hazork.mysouls.souls.SoulAccount;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class SoulsPlaceholderAPI extends PlaceholderExpansion {

  @Override
  public String onPlaceholderRequest(final Player player, @NotNull final String params) {
    if (player == null) {
      return null;
    }

    Object result = null;
    final String[] arr = params.split("_");
    final String[] args = Arrays.copyOfRange(arr, 1, arr.length);
    final SoulAccount account = SoulsPlugin.get().getStorage().get(player.getUniqueId());

    switch (arr[0]) {
      case "soulscount":
        if (args.length >= 1) {
          @SuppressWarnings("deprecation")
          final OfflinePlayer of = Bukkit.getOfflinePlayer(args[0]);
          result = account.soulsCount(of.getUniqueId());
          break;
        }
        result = account.soulsCount(null);
        break;

      case "playercount":
        result = account.playerCount();
        break;

      case "soulsratio":
        result = account.soulsRatio();
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
    return SoulsPlugin.get().getDescription().getAuthors().toString();
  }

  @Override
  public String getIdentifier() {
    return this.getRequiredPlugin().toLowerCase();
  }

  @Override
  public String getVersion() {
    return SoulsPlugin.getVersion();
  }

  @Override
  public @Nullable String getRequiredPlugin() {
    return SoulsPlugin.get().getName();
  }
}
