package com.github._hazork.oldmysouls.souls;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.data.lang.Lang;
import com.github._hazork.oldmysouls.utils.Nbts;
import com.github._hazork.oldmysouls.utils.Utils;

public class SoulListener implements Listener {

  private final MySouls plugin;

  public SoulListener(final MySouls plugin) {
    this.plugin = plugin;
  }

  public void register() {
    Bukkit.getPluginManager().registerEvents(this, this.plugin);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerDeath(final PlayerDeathEvent event) {
    final Player entity = event.getEntity();
    final Player killer = entity.getKiller();
    if (Utils.nonNull(killer, entity)) {
      SoulComunicator.of(entity).reportDeath(killer);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    if (event.hasItem()) {
      final ItemStack item = event.getItem();
      if (Nbts.isNbtsItem(item) && Nbts.getIdValue(item).equals(SoulWallet.SOUL_ID)) {
        event.setCancelled(true);
        SoulComunicator.of(event.getPlayer()).collectSouls(item);
      }
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPlace(final BlockPlaceEvent event) {
    final ItemStack item = event.getItemInHand();
    if (Nbts.isNbtsItem(item) && Nbts.getIdValue(item).equals(SoulWallet.COIN_ID)) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(Lang.CANNOT_USE.getText());
    }
  }
}
