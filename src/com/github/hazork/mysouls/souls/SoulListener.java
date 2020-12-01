package com.github.hazork.mysouls.souls;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.events.SoulChangeEvent;
import com.github.hazork.mysouls.util.ItemStacks;
import com.github.hazork.mysouls.util.Spigots;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class SoulListener implements Listener {

    private final MySouls plugin;
    private final WalletDB walletDB;

    public SoulListener(MySouls plugin) {
	this.plugin = plugin;
	walletDB = this.plugin.getWallets();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
	if (!Spigots.isSelfKill(event) && event.getEntity().getKiller() != null) {
	    Player dead = event.getEntity();
	    SoulWallet dWallet = walletDB.from(dead);
	    SoulWallet kWallet = walletDB.from(dead.getKiller());
	    dWallet.reportDeath(kWallet);
	}
    }

    @EventHandler
    public void onItem(BlockPlaceEvent event) {
	if (event.getItemInHand().getType().equals(Material.SKULL_ITEM)) {
	    ItemStack skull = event.getItemInHand();
	    NBTTagCompound nbt = ItemStacks.getNBT(skull);
	    if (nbt.getLong("svuid") == SoulWallet.serialVersionUID) {
		event.setCancelled(true);
		event.getPlayer().sendMessage("§cVocê não pode colocar almas no chão");
	    }
	}
    }

    @EventHandler
    public void onSoulChange(SoulChangeEvent event) {
	System.out.println("SOUL CHANGE: " + event);
    }

    public void register() {
	Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
