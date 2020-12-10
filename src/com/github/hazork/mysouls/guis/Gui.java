package com.github.hazork.mysouls.guis;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.souls.SoulsDB;

public abstract class Gui {

    private static SoulsDB soulsDb = MySouls.getDB();

    private final UUID ownerId;

    protected Inventory inventory;

    protected Gui(UUID ownerId, int lines, String title) {
	this.ownerId = ownerId;
	inventory = createInventory(lines, title);
    }

    protected abstract String getName();

    protected abstract void load();

    public void onClick(InventoryClickEvent event) {
	event.setCancelled(true);
    }

    public void open(boolean load) {
	if (load) {
	    load();
	}
	getPlayer().openInventory(getInventory());
    }

    protected Inventory createInventory(int lines, String title) {
	return Bukkit.createInventory(null, lines * 9, title);
    }

    public Inventory getInventory() {
	return inventory;
    }

    public SoulWallet getWallet() {
	return soulsDb.from(getOwnerId());
    }

    public UUID getOwnerId() {
	return ownerId;
    }

    public OfflinePlayer getOfflinePlayer() {
	return Bukkit.getOfflinePlayer(getOwnerId());
    }

    @Nullable
    public Player getPlayer() {
	return Bukkit.getPlayer(getOwnerId());
    }
}
