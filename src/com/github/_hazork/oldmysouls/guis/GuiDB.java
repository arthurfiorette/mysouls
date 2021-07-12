package com.github._hazork.oldmysouls.guis;

import com.github._hazork.oldmysouls.data.lang.Lang;
import com.github._hazork.oldmysouls.utils.db.HashMapDB;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public final class GuiDB extends HashMapDB<UUID, GuiContainer> {

  public GuiContainer from(final Player player) {
    return super.from(player.getUniqueId());
  }

  @Override
  protected UUID keyFunction(final GuiContainer value) {
    return value.getOwnerId();
  }

  @Override
  protected void load(final UUID uuid) {
    this.putValue(new GuiContainer(uuid));
  }

  /**
   * Returns a gui container optional if the player is in the map or
   * {@link Optional#empty} if the uuid isn't on the map.
   */
  public Optional<GuiContainer> fromCache(final UUID uuid) {
    if (this.getMap().containsKey(uuid)) {
      return Optional.of(this.getMap().get(uuid));
    } else {
      return Optional.empty();
    }
  }

  public void close() {
    for (final Entry<UUID, GuiContainer> entry : this.getMap().entrySet()) {
      final OfflinePlayer op = Bukkit.getOfflinePlayer(entry.getKey());
      if ((op != null) && op.isOnline()) {
        final Player player = (Player) op;
        final Inventory top = player.getOpenInventory().getTopInventory();
        if (top != null) {
          final GuiContainer gc = entry.getValue();
          if (gc.getInventories().contains(top)) {
            player.closeInventory();
            player.sendMessage(Lang.INVENTORY_DATABASE_CLOSED.getText());
          }
        }
      }
    }
  }
}
