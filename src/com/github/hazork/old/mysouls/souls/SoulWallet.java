package com.github.hazork.old.mysouls.souls;

import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;

import javax.persistence.Transient;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.old.mysouls.events.SoulChangeEvent;
import com.github.hazork.old.mysouls.events.SoulWithdrawEvent;
import com.github.hazork.old.mysouls.utils.ItemStacks;
import com.github.hazork.old.mysouls.utils.Utils;

public class SoulWallet {

    @Transient
    private final UUID uuid;
    private LinkedList<UUID> souls = new LinkedList<>();

    public SoulWallet(UUID uuid) {
	this.uuid = uuid;
	
	Collections.addAll(souls, uuid, uuid);
    }

    public boolean kill(SoulWallet other) {
	if (canLoseSoul()) {
	    int index = Utils.getRandom(souls.size());
	    UUID soul = souls.get(index);
	    Bukkit.getPluginManager().callEvent(new SoulChangeEvent(soul, this, other));
	    other.souls.add(uuid);
	    this.souls.remove(index);
	    return true;
	}
	return false;
    }

    public ItemStack withdraw() {
	if (canLoseSoul() && isOwnerOnline()) {
	    UUID soul = pollSoul();
	    Bukkit.getPluginManager().callEvent(new SoulWithdrawEvent(soul, this));
	    Utils.sendItem(getPlayer(), ItemStacks.createSoul(soul));
	}
	return null;
    }

    public boolean canLoseSoul() {
	return getAmount() > 0;
    }

    public boolean isOwnerOnline() {
	return getPlayer() != null;
    }

    public Player getPlayer() {
	return Bukkit.getPlayer(uuid);
    }

    public int getAmount() {
	return this.souls.size();
    }

    public UUID getUUID() {
	return uuid;
    }

    private UUID pollSoul() {
	int index = Utils.getRandom(souls.size());
	UUID soul = souls.get(index);
	souls.remove(index);
	return soul;
    }
}
