package com.github._hazork.oldmysouls.guis;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.souls.SoulWallet;
import com.github._hazork.oldmysouls.souls.SoulsDB;

public abstract class Gui {

  private static SoulsDB soulsDb = MySouls.getDB();

  private final UUID ownerId;

  protected Inventory inventory;

  protected Gui(final UUID ownerId, final int lines, final String title) {
    this.ownerId = ownerId;
    this.inventory = this.createInventory(lines, title);
  }

  protected abstract String getName();

  protected abstract void load();

  public void onClick(final InventoryClickEvent event) {
    event.setCancelled(true);
    this.load();
  }

  public void open(final boolean load) {
    if (load) {
      this.load();
    }
    this.getPlayer().openInventory(this.getInventory());
  }

  protected Inventory createInventory(final int lines, final String title) {
    return Bukkit.createInventory(null, lines * 9, title);
  }

  public Inventory getInventory() {
    return this.inventory;
  }

  public SoulWallet getWallet() {
    return Gui.soulsDb.from(this.getOwnerId());
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }

  public OfflinePlayer getOfflinePlayer() {
    return Bukkit.getOfflinePlayer(this.getOwnerId());
  }

  @Nullable
  public Player getPlayer() {
    return Bukkit.getPlayer(this.getOwnerId());
  }
}
