package com.github.hazork.mysouls.souls;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.Nbts;
import com.github.hazork.mysouls.utils.Utils;

public class SoulListener implements Listener {

    private final MySouls plugin;

    public SoulListener(MySouls plugin) {
	this.plugin = plugin;
    }

    public void register() {
	Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
	Player entity = event.getEntity();
	Player killer = entity.getKiller();
	if (Utils.nonNull(killer, entity)) {
	    SoulComunicator.of(entity).reportDeath(killer);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
	if (event.hasItem()) {
	    ItemStack item = event.getItem();
	    if (Nbts.isNbtsItem(item)) {
		if (Nbts.getIdValue(item).equals(SoulWallet.SOUL_ID)) {
		    event.setCancelled(true);
		    SoulComunicator.of(event.getPlayer()).collectSouls(item);
		}
	    }
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
	ItemStack item = event.getItemInHand();
	if (Nbts.isNbtsItem(item)) {
	    if (Nbts.getIdValue(item).equals(SoulWallet.COIN_ID)) {
		event.setCancelled(true);
		event.getPlayer().sendMessage(Lang.CANNOT_USE.getText());
	    }
	}
    }
}
