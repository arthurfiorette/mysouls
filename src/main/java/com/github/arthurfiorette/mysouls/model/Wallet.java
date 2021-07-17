package com.github.arthurfiorette.mysouls.model;

import com.github.arthurfiorette.sinklibrary.interfaces.Identifiable;
import com.github.arthurfiorette.sinklibrary.services.SpigotService;
import com.google.gson.annotations.Expose;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

// TODO: Refactor old wallet code.
@ToString
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Wallet implements Identifiable {

  @Expose
  private final UUID uniqueId;

  @Expose
  private final Map<UUID, Integer> souls = new HashMap<>();

  public boolean canAddSoul(final UUID soul, final int amount) {
    if (soul == null || !SpigotService.isMinecraftPack(amount)) {
      return false;
    }

    final Integer existing = this.souls.get(soul);

    if (existing == null) {
      return true;
    }

    return existing + amount <= 64;
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

    return this.sizeOf(soul) >= amount;
  }

  public UUID removeSoul(final UUID soul, final int amount) {
    if (this.canRemoveSoul(soul, amount)) {
      this.souls.compute(soul, (k, v) -> v <= amount ? null : v - amount);
      return soul;
    }

    return null;
  }

  public UUID getCommonSoul(final Wallet wallet, final int amount) {
    for (final UUID uuid : this.souls.keySet()) {
      if (this.canSendSoul(wallet, uuid, amount)) {
        return uuid;
      }
    }

    return null;
  }

  public boolean canSendSoul(final Wallet wallet) {
    return this.canSendSoul(wallet, this.getCommonSoul(wallet, 1), 1);
  }

  public boolean canSendSoul(final Wallet wallet, final UUID soul, final int amount) {
    return this.canRemoveSoul(soul, amount) && wallet.canAddSoul(soul, amount);
  }

  /**
   * @return the total amount of souls, reducing the souls map.
   */
  public int size() {
    return this.souls.values().stream().reduce(0, Integer::sum);
  }

  public int sizeOf(final UUID uuid) {
    final Integer size = this.souls.get(uuid);
    return size == null ? 0 : size;
  }
}
