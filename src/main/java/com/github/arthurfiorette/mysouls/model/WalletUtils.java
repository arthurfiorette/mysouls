package com.github.arthurfiorette.mysouls.model;

import com.github.arthurfiorette.mysouls.lang.Lang;

import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class WalletUtils {

  public Entry<UUID, Integer> biggestEntry(final Wallet wallet) {
    if (wallet.getSouls().size() == 0) {
      return null;
    }

    return Collections.max(wallet.getSouls().entrySet(),
        (a, b) -> a.getValue().compareTo(b.getValue()));
  }

  public double soulsRatio(final Wallet wallet) {
    return ((double) wallet.getSouls().size()) / wallet.size();
  }

  public Lang withdrawSoul(final Wallet wallet, final UUID soul) {
    final Player player = Bukkit.getPlayer(wallet.getUniqueId());

    if (player == null) {
      return Lang.UNKNOWN_ERROR;
    }

    final int slot = player.getInventory().firstEmpty();

    if (slot == -1) {
      return Lang.INVENTORY_FULL;
    }

    final UUID realSoul = wallet.removeSoul(soul, 1);

    if (realSoul == null) {
      return Lang.DONT_HAVE_SOULS;
    }

    player.getInventory().addItem(WalletUtils.soulToItem(realSoul));
    return Lang.SOUL_REMOVED;
  }

  public Lang withdrawCoins(final Wallet wallet, final int amount) {
    final Player player = Bukkit.getPlayer(wallet.getUniqueId());

    if (player == null) {
      return Lang.UNKNOWN_ERROR;
    }

    final int slot = player.getInventory().firstEmpty();

    if (slot == -1) {
      return Lang.INVENTORY_FULL;
    }

    final Entry<UUID, Integer> entry = WalletUtils.biggestEntry(wallet);

    if ((entry == null) || (entry.getValue() < amount)) {
      return Lang.DONT_HAVE_SOULS;
    }

    final UUID removed = wallet.removeSoul(entry.getKey(), amount);

    if (removed == null) {
      return Lang.DONT_HAVE_SOULS;
    }

    return Lang.COINS_REMOVED;
  }

  // TODO: Soul to item
  public ItemStack soulToItem(final UUID soul) {
    return new ItemStack(Material.STONE);
  }
}
