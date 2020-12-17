package com.github.hazork.mysouls.souls;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
    public void onInteract(PlayerInteractEvent event) {
	if (event.hasItem()) {
	    switch (Nbts.getIdValue(event.getItem())) {
		case SoulWallet.COIN_ID:
		    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			event.getPlayer().sendMessage(Lang.CANNOT_USE.getText());
			event.setCancelled(true);
		    }
		    break;

		case SoulWallet.SOUL_ID:
		    SoulComunicator.of(event.getPlayer()).collectSouls(event.getItem());
		    break;
	    }
	}
    }
}
