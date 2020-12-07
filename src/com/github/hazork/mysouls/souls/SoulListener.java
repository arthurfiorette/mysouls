package com.github.hazork.mysouls.souls;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.Utils.ItemStacks;

public class SoulListener implements Listener {

    private final MySouls plugin;
    private SoulsDB soulsDb = MySouls.getDB();

    public SoulListener(MySouls plugin) {
	this.plugin = plugin;
    }

    public void register() {
	Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
	Player killer = event.getEntity().getKiller();
	SoulWallet wallet = soulsDb.from(event.getEntity());
	wallet.reportDeath(killer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
	if (event.hasItem()) {
	    ItemStack item = event.getItem();
	    ItemStacks.getNBTPattern(item).ifPresent(value -> {
		switch (value) {
		    case SoulWallet.COIN_VALUE:
			switch (event.getAction()) {
			    case LEFT_CLICK_AIR:
			    case LEFT_CLICK_BLOCK:
				event.getPlayer().sendMessage(Lang.CANNOT_USE.getText());
				event.setCancelled(true);
				break;

			    default:
				break;
			}
			break;

		    case SoulWallet.SOUL_VALUE:
			Player player = event.getPlayer();
			int amount = item.getAmount();
			SoulWallet sw = soulsDb.from(event.getPlayer());
			UUID soul = UUID.fromString(ItemStacks.getNBT(item).getString(MySouls.NAME + ".uuid"));
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR) {
			    if (player.isSneaking()) {
				if (sw.canAddSouls(soul, amount)) {
				    for (int i = 0; i < amount; i++) sw.addSoul(soul);
				    player.sendMessage(Lang.SOULS_ADDED.getText());
				    player.setItemInHand(null);
				} else {
				    player.sendMessage(Lang.SOUL_64_LIMIT.getText());
				}
			    } else {
				if (sw.canAddSoul(soul)) {
				    sw.addSoul(soul);
				    player.sendMessage(Lang.SOUL_ADDED.getText());
				    if (item.getAmount() > 1) item.setAmount(item.getAmount() - 1);
				    else player.setItemInHand(null);
				} else {
				    player.sendMessage(Lang.SOUL_64_LIMIT.getText());
				}
			    }
			}
			break;
		}
	    });
	}
    }
}
