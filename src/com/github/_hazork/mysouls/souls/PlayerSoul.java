package com.github._hazork.mysouls.souls;

import java.util.UUID;

public class PlayerSoul {

  private final UUID owner;
  private int amount;

  public PlayerSoul(final UUID owner) {
    this(owner, 0);
  }

  public PlayerSoul(final UUID owner, final int amount) {
    this.owner = owner;
    this.amount = amount;
  }

  public void send(final PlayerSoul other, final int amount) {
    if (this.canSend(other, amount)) {
      this.amount -= amount;
      other.amount += amount;
    }
  }

  public boolean canSend(final PlayerSoul soul, final int amount) {
    return this.canIncrement(-amount) && soul.canIncrement(amount);
  }

  public void increment(final int amount) {
    if (this.canIncrement(amount)) {
      this.amount += amount;
    }
  }

  public boolean canIncrement(final int amount) {
    return ((this.amount + amount) <= 64) && ((this.amount + amount) >= 0);
  }

  public int amount() {
    return this.amount;
  }

  public UUID getOwnerId() {
    return this.owner;
  }

}
