package com.github.hazork.mysouls.souls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.github.hazork.mysouls.data.config.Config;
import com.github.hazork.mysouls.utils.Utils;

public class SoulWallet {

    public static final String SOUL_ID = "soulsId";
    public static final String COIN_ID = "coinsId";

    private final UUID ownerId;
    Map<UUID, Integer> souls = new HashMap<>();

    public SoulWallet(UUID ownerId) {
	this.ownerId = ownerId;
	souls.put(ownerId, Config.INITIAL_SOULS.getInt());
    }

    public boolean canAddSoul(UUID soul, int amount) {
	if (soul == null || !Utils.isMinecraftPack(amount)) {
	    return false;
	} else if (!souls.containsKey(soul)) {
	    return true;
	} else {
	    return souls.get(soul) + amount <= 64;
	}
    }

    public void addSoul(UUID soul, int amount) {
	if (canAddSoul(soul, amount)) {
	    souls.compute(soul, (k, v) -> v == null ? amount : v + amount);
	}
    }

    public boolean canRemoveSoul(UUID soul, int amount) {
	if (!Utils.isMinecraftPack(amount)) {
	    return false;
	} else if (souls.size() == 0) {
	    return true;
	} else if (soul == null) {
	    return biggestEntry().getValue() >= amount;
	} else {
	    return soulsCount(soul) >= amount;
	}
    }

    public UUID removeSoul(UUID soul, int amount) {
	if (canRemoveSoul(soul, amount)) {
	    if (soul == null) {
		soul = getRandom();
	    }
	    souls.compute(soul, (k, v) -> v <= amount ? null : v - amount);
	    return soul;
	} else {
	    return null;
	}
    }

    public UUID getRemoveableSoul(SoulWallet winner, int amount) {
	for(UUID uuid: souls.keySet()) {
	    if (canChangeSoul(winner, uuid, amount)) {
		return uuid;
	    }
	}
	return null;
    }

    public boolean canChangeSoul(SoulWallet winner) {
	return canChangeSoul(winner, getRemoveableSoul(winner, 1), 1);
    }

    public boolean canChangeSoul(SoulWallet winner, UUID soul, int amount) {
	return canRemoveSoul(soul, amount) && winner.canAddSoul(soul, amount);
    }

    public boolean reportDeath(SoulWallet killer) {
	UUID soul = getRemoveableSoul(killer, 1);
	if (canChangeSoul(killer, soul, 1)) {
	    killer.addSoul(removeSoul(soul, 1), 1);
	    return true;
	} else {
	    return false;
	}
    }

    public UUID getOwnerId() {
	return ownerId;
    }

    public Set<UUID> getSouls() {
	return souls.keySet();
    }

    public int playerCount() {
	return souls.size();
    }

    public Entry<UUID, Integer> biggestEntry() {
	if (souls.size() == 0) {
	    return null;
	}
	return Collections.max(souls.entrySet(), Map.Entry.comparingByValue());
    }

    public int soulsCount() {
	return soulsCount(null);
    }

    public OfflinePlayer asPlayer() {
	return Bukkit.getOfflinePlayer(getOwnerId());
    }

    public boolean isOnline() {
	return asPlayer().isOnline();
    }

    public int soulsCount(@Nullable UUID uuid) {
	if (uuid == null) {
	    return souls.values().stream().reduce(0, Integer::sum);
	}
	return souls.containsKey(uuid) ? souls.get(uuid) : 0;
    }

    public double soulsRatio() {
	return (double) soulsCount() / playerCount();
    }

    private UUID getRandom() {
	return Utils.getRandom(new ArrayList<>(souls.keySet()));
    }

}
