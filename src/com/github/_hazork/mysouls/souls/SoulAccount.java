package com.github._hazork.mysouls.souls;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;

public class SoulAccount {

  public static final String SOUL_ID = "soulsId";
  public static final String COIN_ID = "coinsId";

  private final UUID ownerId;
  Map<UUID, PlayerSoul> wallet = new ConcurrentHashMap<>();

  SoulAccount(final UUID uuid) {
    this.ownerId = uuid;
    this.wallet.put(uuid, new PlayerSoul(uuid, 2));
  }

  public PlayerSoul getSoul(final UUID uuid) {
    if (!this.containsSoul(uuid)) {
      this.wallet.put(uuid, new PlayerSoul(uuid));
    }
    return this.wallet.get(uuid);
  }

  public boolean containsSoul(final UUID uuid) {
    return this.wallet.containsKey(uuid);
  }

  public Optional<UUID> getTradeableUuid(final SoulAccount other, final int amount) {
    for (final PlayerSoul soul : this.wallet.values()) {
      final UUID uuid = soul.getOwnerId();
      if (other.containsSoul(uuid)) {
        final PlayerSoul othersoul = other.getSoul(uuid);
        if (soul.canSend(othersoul, amount)) {
          return Optional.of(uuid);
        }
      }
    }
    return Optional.empty();
  }

  public boolean tradeSoul(final SoulAccount killer) {
    final Optional<UUID> soul = this.getTradeableUuid(killer, 1);
    if (soul.isPresent()) {
      final UUID uuid = soul.get();
      final PlayerSoul thisSoul = this.getSoul(uuid);
      final PlayerSoul killerSoul = killer.getSoul(uuid);
      thisSoul.send(killerSoul, 1);
      return true;
    } else {
      return false;
    }
  }

  public boolean canIncrement(final UUID soul, final int amount) {
    if (this.containsSoul(soul)) {
      return this.getSoul(soul).canIncrement(amount);
    } else {
      return amount > 0;
    }
  }

  public boolean increment(final UUID soul, final int amount) {
    if (this.canIncrement(soul, amount)) {
      this.getSoul(soul).increment(amount);
      return true;
    } else {
      return false;
    }
  }

  public void clean() {
    final Set<Entry<UUID, PlayerSoul>> set = this.wallet.entrySet();
    for (final Entry<UUID, PlayerSoul> entry : set) {
      if (entry.getValue().amount() == 0) {
        set.remove(entry);
      }
    }
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }

  public Collection<PlayerSoul> getSouls() {
    return this.wallet.values();
  }

  public int playerCount() {
    return this.wallet.size();
  }

  public PlayerSoul biggestSoul() {
    if (this.wallet.isEmpty()) {
      return null;
    }
    PlayerSoul biggest = this.wallet.values().iterator().next();
    for (final PlayerSoul soul : this.wallet.values()) {
      if ((soul != biggest) && (soul.amount() > biggest.amount())) {
        biggest = soul;
      }
    }
    return biggest;
  }

  public OfflinePlayer asPlayer() {
    return Bukkit.getOfflinePlayer(this.getOwnerId());
  }

  public boolean isOnline() {
    return this.asPlayer().isOnline();
  }

  public int soulsCount(@Nullable final UUID uuid) {
    if (uuid == null) {
      return this.wallet.values().stream().map(PlayerSoul::amount).reduce(0, Integer::sum);
    }
    return this.getSoul(uuid).amount();
  }

  public double soulsRatio() {
    return (double) this.soulsCount(null) / this.playerCount();
  }
}
