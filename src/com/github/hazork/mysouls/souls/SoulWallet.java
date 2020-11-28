package com.github.hazork.mysouls.souls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.github.hazork.mysouls.events.SoulChangeEvent;
import com.github.hazork.mysouls.utils.Utils;

public class SoulWallet {

    private final UUID uuid;
    private ArrayList<UUID> souls = new ArrayList<>();

    public SoulWallet(UUID uuid) {
	this.uuid = uuid;
	Collections.addAll(souls, uuid, uuid);
    }

    public boolean kill(SoulWallet other) {
	if (canLose()) {
	    int index = Utils.getRandom(souls.size());
	    UUID soul = souls.get(index);
	    SoulChangeEvent event = new SoulChangeEvent(soul, this, other);
	    Bukkit.getPluginManager().callEvent(event);
	    if (!event.isCancelled()) {
		other.souls.add(uuid);
		this.souls.remove(index);
		return true;
	    }
	}
	return false;
    }

//    public ItemStack withdraw() {
//	
//	return null;
//    }

    public boolean canLose() {
	return getAmount() > 0;
    }

    public int getAmount() {
	return this.souls.size();
    }

    public UUID getUUID() {
	return uuid;
    }

}
