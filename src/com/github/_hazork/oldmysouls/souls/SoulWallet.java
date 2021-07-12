package com.github._hazork.oldmysouls.souls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

import com.github._hazork.oldmysouls.data.config.Config;
import com.github._hazork.oldmysouls.utils.Utils;

public class SoulWallet {

  public static final String SOUL_ID = "soulsId";
  public static final String COIN_ID = "coinsId";

  private final UUID ownerId;
  Map<UUID, Integer> souls = new HashMap<>();

  public SoulWallet(final UUID ownerId) {
    this.ownerId = ownerId;
    this.souls.put(ownerId, Config.INITIAL_SOULS.getInt());
  }

  public boolean canAddSoul(final UUID soul, final int amount) {
    if ((soul == null) || !Utils.isMinecraftPack(amount)) {
      return false;
    } else if (!this.souls.containsKey(soul)) {
      return true;
    } else {
      return (this.souls.get(soul) + amount) <= 64;
    }
  }

  public void addSoul(final UUID soul, final int amount) {
    if (this.canAddSoul(soul, amount)) {
      this.souls.compute(soul, (k, v) -> v == null ? amount : v + amount);
    }
  }

  public boolean canRemoveSoul(final UUID soul, final int amount) {
    if (!Utils.isMinecraftPack(amount)) {
      return false;
    } else if (this.souls.size() == 0) {
      return true;
    } else if (soul == null) {
      return this.biggestEntry().getValue() >= amount;
    } else {
      return this.soulsCount(soul) >= amount;
    }
  }

  public UUID removeSoul(UUID soul, final int amount) {
    if (this.canRemoveSoul(soul, amount)) {
      if (soul == null) {
        soul = this.getRandom();
      }
      this.souls.compute(soul, (k, v) -> v <= amount ? null : v - amount);
      return soul;
    } else {
      return null;
    }
  }

  public UUID getRemoveableSoul(final SoulWallet winner, final int amount) {
    for(final UUID uuid: this.souls.keySet()) {
      if (this.canChangeSoul(winner, uuid, amount)) {
        return uuid;
      }
    }
    return null;
  }

  public boolean canChangeSoul(final SoulWallet winner) {
    return this.canChangeSoul(winner, this.getRemoveableSoul(winner, 1), 1);
  }

  public boolean canChangeSoul(final SoulWallet winner, final UUID soul, final int amount) {
    return this.canRemoveSoul(soul, amount) && winner.canAddSoul(soul, amount);
  }

  public boolean reportDeath(final SoulWallet killer) {
    final UUID soul = this.getRemoveableSoul(killer, 1);
    if (this.canChangeSoul(killer, soul, 1)) {
      killer.addSoul(this.removeSoul(soul, 1), 1);
      return true;
    } else {
      return false;
    }
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }

  public Set<UUID> getSouls() {
    return this.souls.keySet();
  }

  public int playerCount() {
    return this.souls.size();
  }

  public Entry<UUID, Integer> biggestEntry() {
    if (this.souls.size() == 0) {
      return null;
    }
    return Collections.max(this.souls.entrySet(), Map.Entry.comparingByValue());
  }

  public int soulsCount() {
    return this.soulsCount(null);
  }

  public OfflinePlayer asPlayer() {
    return Bukkit.getOfflinePlayer(this.getOwnerId());
  }

  public boolean isOnline() {
    return this.asPlayer().isOnline();
  }

  public int soulsCount(@Nullable final UUID uuid) {
    if (uuid == null) {
      return this.souls.values().stream().reduce(0, Integer::sum);
    }
    return this.souls.containsKey(uuid) ? this.souls.get(uuid) : 0;
  }

  public double soulsRatio() {
    return (double) this.soulsCount() / this.playerCount();
  }

  private UUID getRandom() {
    return Utils.getRandom(new ArrayList<>(this.souls.keySet()));
  }

}
