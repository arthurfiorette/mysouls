package com.github._hazork.mysouls.souls;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github._hazork.mysouls.data.lang.LangEnum;
import com.github._hazork.mysouls.utils.Nbts;
import com.github.arthurfiorette.sinklibrary.components.SinkPlugin;
import com.github.arthurfiorette.sinklibrary.listener.SinkListener;

public class SoulListener extends SinkListener {

  public SoulListener(final SinkPlugin owner) {
    super(owner);
  }

  @Override
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerDeath(final PlayerDeathEvent event) {
    final Player dead = event.getEntity();
    final Player killer = dead.getKiller();
    if (JavaServices.nonNull(dead, killer)) {
      SoulCommunicator.get(dead).reportDeath(killer);
    }
  }

  @Override
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    if (event.hasItem()) {
      final ItemStack item = event.getItem();
      if (Nbts.isNbtsItem(item) && Nbts.getIdValue(item).equals(SoulAccount.SOUL_ID)) {
        event.setCancelled(true);
        SoulCommunicator.get(event.getPlayer()).collectSouls(item);
      }
    }
  }

  @Override
  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPlace(final BlockPlaceEvent event) {
    final ItemStack item = event.getItemInHand();
    if (Nbts.isNbtsItem(item) && Nbts.getIdValue(item).equals(SoulAccount.COIN_ID)) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(LangEnum.CANNOT_USE.getText());
    }
  }

}
