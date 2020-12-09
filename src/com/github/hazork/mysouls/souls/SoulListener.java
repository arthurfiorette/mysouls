package com.github.hazork.mysouls.souls;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.Nbts;

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
	if (wallet.reportDeath(soulsDb.from(killer))) {
	    killer.sendMessage(Lang.KILL_MESSAGE.getText("{player}", event.getEntity().getName()));
	    event.getEntity().sendMessage(Lang.DEATH_MESSAGE.getText("{player}", killer.getName()));
	} else {
	    killer.sendMessage(Lang.KILL_MESSAGE_FAIL.getText());
	    event.getEntity().sendMessage(Lang.DEATH_MESSAGE_FAIL.getText());
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
	if (event.hasItem()) {
	    ItemStack item = event.getItem();
	    Lang message = null;
	    switch (Nbts.getIdValue(item)) {
		case SoulWallet.COIN_ID:
		    switch (event.getAction()) {
			case RIGHT_CLICK_BLOCK:
			    message = Lang.CANNOT_USE;
			    event.setCancelled(true);
			default:
			    break;
		    }
		    break;

		case SoulWallet.SOUL_ID:
		    Player player = event.getPlayer();
		    int amount = item.getAmount();
		    SoulWallet sw = soulsDb.from(event.getPlayer());
		    UUID soul = UUID.fromString(Nbts.getValue(item));

		    switch (event.getAction()) {
			case RIGHT_CLICK_AIR:
			    if (player.isSneaking()) {
				if (!sw.canAddSoul(soul, amount)) message = Lang.SOUL_64_LIMIT;
				else {
				    for (int i = 0; i < amount; i++) sw.addSoul(soul);
				    message = Lang.SOULS_ADDED;
				    player.setItemInHand(null);
				}
			    } else {
				if (!sw.canAddSoul(soul, 1)) message = Lang.SOUL_64_LIMIT;
				else {
				    sw.addSoul(soul);
				    if (amount > 1) item.setAmount(amount - 1);
				    else player.setItemInHand(null);
				    message = Lang.SOUL_ADDED;
				}
			    }
			    break;

			default:
			    break;
		    }
	    }
	    if (message != null) event.getPlayer().sendMessage(message.getText());
	}
    }
}
