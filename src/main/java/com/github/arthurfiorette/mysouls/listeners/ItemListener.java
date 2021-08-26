package com.github.arthurfiorette.mysouls.listeners;

import java.util.UUID;

import com.github.arthurfiorette.mysouls.lang.Lang;
import com.github.arthurfiorette.mysouls.lang.LangFile;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.mysouls.nbt.NbtKey;
import com.github.arthurfiorette.mysouls.nbt.NbtReader;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import com.github.arthurfiorette.sinklibrary.events.SinkListener;
import com.github.arthurfiorette.sinklibrary.uuid.FastUuid;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ItemListener extends SinkListener {

  private final WalletStorage storage;
  private final LangFile lang;

  public ItemListener(final BasePlugin basePlugin) {
    super(basePlugin);
    this.storage = basePlugin.get(WalletStorage.class);
    this.lang = basePlugin.get(LangFile.class);
  }

  @Override
  @EventHandler(priority = EventPriority.LOWEST)
  public void onPlayerInteract(final PlayerInteractEvent event) {
    final ItemStack item = event.getItem();

    if (!event.hasItem() || item.getAmount() < 1) {
      return;
    }

    final String id = NbtReader.getString(item, NbtKey.SOUL);

    // Isn't a soul head.
    if (id == null || id.length() != 36) {
      return;
    }

    event.setCancelled(true);

    final Player player = event.getPlayer();
    final UUID itemId = FastUuid.parseUUID(id);
    final Wallet wallet = this.storage.getSync(player);
    final int itemAmount = item.getAmount();
    final boolean sneaking = player.isSneaking();

    // If the player is holding shift, collect all souls instead of a single
    // one.
    final int coinsAmount = sneaking ? itemAmount : 1;

    // If it cant add another souls, warn him and return
    if (!wallet.canAddSoul(itemId, coinsAmount)) {
      player.sendMessage(this.lang.getString(Lang.SOUL_64_LIMIT));
      return;
    }

    if (sneaking || itemAmount <= 1) {
      player.setItemInHand(null);
    } else {
      item.setAmount(itemAmount - 1);
    }

    wallet.addSoul(itemId, coinsAmount);

    final Lang message = sneaking ? Lang.SOULS_ADDED : Lang.SOULS_ADDED;
    player.sendMessage(this.lang.getString(message));

    // If it isn't sneaking, add only one soul.
    if (!player.isSneaking()) {
      if (itemAmount > 1) {
        item.setAmount(itemAmount - 1);
      } else {
        player.setItemInHand(null);
      }

      player.sendMessage(this.lang.getString(Lang.SOUL_ADDED));
      return;
    }

    // Add all souls.
    wallet.addSoul(itemId, itemAmount);
    player.setItemInHand(null);
    player.sendMessage(this.lang.getString(Lang.SOULS_ADDED));
  }

  @Override
  @EventHandler(priority = EventPriority.LOWEST)
  public void onBlockPlace(final BlockPlaceEvent event) {
    final ItemStack item = event.getItemInHand();

    final boolean isCoin = NbtReader.getBoolean(item, NbtKey.COIN);

    if (isCoin) {
      event.setCancelled(true);
      event.getPlayer().sendMessage(this.lang.getString(Lang.CANNOT_USE));
    }
  }
}
