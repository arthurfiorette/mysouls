package com.github.hazork.mysouls.souls;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.data.Config;
import com.github.hazork.mysouls.events.SoulChangeEvent;
import com.github.hazork.mysouls.events.SoulWithdrawEvent;
import com.github.hazork.mysouls.util.ItemStacks;
import com.github.hazork.mysouls.util.Spigots;
import com.github.hazork.mysouls.util.Utils;
import com.google.common.collect.Lists;

public class SoulWallet {

    final UUID ownerId;
    List<UUID> souls;

    SoulWallet(UUID ownerId) {
	this.ownerId = ownerId;
	souls = Lists.newArrayList(Utils.multiply(ownerId, Config.get(Integer.class, 2, "config.initial-souls")));
    }

    public void reportDeath(SoulWallet killer) {
	if (canLoseSoul()) {
	    UUID soul = Utils.poll(souls);
	    killer.souls.add(soul);
	    Spigots.callEvent(new SoulChangeEvent(this, killer, soul));
	}
    }

    public ItemStack withdraw() {
	if (canLoseSoul()) {
	    UUID soul = Utils.poll(souls);
	    OfflinePlayer player = Bukkit.getOfflinePlayer(soul);
	    Spigots.callEvent(new SoulWithdrawEvent(soul, this));
	    return ItemStacks.set(ItemStacks.getHead(player.getName()), "§5§lAlma do jogador: §f" + player.getName(),
		    "§7Esta alma estava na carteira", "§7do jogador: §f" + getOwner().getName(),
		    "§7antes de ser retirada.", "", "§cNão pode ser convertida de volta.");
	} else return null;
    }

    public boolean canLoseSoul() {
	return souls.size() > 1;
    }

    public int getValue() {
	return souls.size();
    }

    public OfflinePlayer getOwner() {
	return Bukkit.getOfflinePlayer(getOwnerId());
    }

    public UUID getOwnerId() {
	return ownerId;
    }
}
