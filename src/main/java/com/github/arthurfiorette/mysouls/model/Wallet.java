package com.github.arthurfiorette.mysouls.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.github.arthurfiorette.sinklibrary.interfaces.Identifiable;
import com.github.arthurfiorette.sinklibrary.services.SpigotService;
import com.google.gson.annotations.Expose;

// TODO: Refactor old wallet code.
public class Wallet implements Identifiable {

  @Expose
  private final UUID uuid;

  @Expose
  private final Map<UUID, Integer> souls;

  public Wallet(final UUID uuid) {
    this.uuid = uuid;
    this.souls = new HashMap<>();
  }

  @Override
  public UUID getUniqueId() {
    return this.uuid;
  }

  public boolean canAddSoul(final UUID soul, final int amount) {
    if ((soul == null) || !SpigotService.isMinecraftPack(amount)) {
      return false;
    }

    final Integer existing = this.souls.get(soul);

    if (existing == null) {
      return true;
    }

    return (existing + amount) <= 64;
  }

  public void addSoul(final UUID soul, final int amount) {
    if (this.canAddSoul(soul, amount)) {
      this.souls.compute(soul, (k, v) -> v == null ? amount : v + amount);
    }
  }

  public boolean canRemoveSoul(final UUID soul, final int amount) {
    if (!SpigotService.isMinecraftPack(amount)) {
      return false;
    }

    if (soul == null) {
      return WalletUtils.biggestEntry(this).getValue() >= amount;
    }

    if (this.souls.size() == 0) {
      return false;
    }

    return this.getSoulCount(soul) >= amount;
  }

  public UUID removeSoul(final UUID soul, final int amount) {
    if (this.canRemoveSoul(soul, amount)) {
      this.souls.compute(soul, (k, v) -> v <= amount ? null : v - amount);
      return soul;
    }

    return null;
  }

  public UUID getRemoveableSoul(final Wallet wallet, final int amount) {
    for(final UUID uuid: this.souls.keySet()) {
      if (this.canSendSoul(wallet, uuid, amount)) {
        return uuid;
      }
    }

    return null;
  }

  public boolean canSendSoul(final Wallet wallet) {
    return this.canSendSoul(wallet, this.getRemoveableSoul(wallet, 1), 1);
  }

  public boolean canSendSoul(final Wallet wallet, final UUID soul, final int amount) {
    return this.canRemoveSoul(soul, amount) && wallet.canAddSoul(soul, amount);
  }

  public Set<UUID> getKeySet() {
    return this.souls.keySet();
  }

  public Map<UUID, Integer> getSouls() {
    return this.souls;
  }

  public int getPlayerCount() {
    return this.souls.size();
  }

  public int getSoulCount() {
    return this.souls.values().stream().reduce(0, Integer::sum);
  }

  public int getSoulCount(final UUID uuid) {
    return this.souls.containsKey(uuid) ? this.souls.get(uuid) : 0;
  }
}
