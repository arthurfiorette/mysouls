package com.github.hazork.mysouls.souls;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.events.SoulChangeEvent;
import com.github.hazork.mysouls.util.Spigots;

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
    public void onSoulChange(SoulChangeEvent event) {
	System.out.println("SOUL CHANGE: " + event);
    }

    public void register() {
	Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
