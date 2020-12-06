package com.github.hazork.mysouls.guis;

import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.hazork.mysouls.utils.db.HashMapDB;

public final class GuiDB extends HashMapDB<UUID, GuiContainer> {

    public GuiContainer from(Player player) {
	return super.from(player.getUniqueId());
    }

    @Override
    protected UUID keyFunction(GuiContainer value) {
	return value.getOwnerId();
    }

    @Override
    protected void load(UUID uuid) {
	putValue(new GuiContainer(uuid));
    }

    /**
     * Returns a gui container optional if the player is in the map or
     * {@link Optional#empty} if the uuid isn't on the map.
     */
    public Optional<GuiContainer> fromCache(UUID uuid) {
	if (getMap().containsKey(uuid)) return Optional.of(getMap().get(uuid));
	else return Optional.empty();
    }

    public void close() {
	for (Entry<UUID, GuiContainer> entry : getMap().entrySet()) {
	    OfflinePlayer op = Bukkit.getOfflinePlayer(entry.getKey());
	    if (op != null && op.isOnline()) {
		Player player = (Player) op;
		Inventory top = player.getOpenInventory().getTopInventory();
		if (top != null) {
		    GuiContainer gc = entry.getValue();
		    if (gc.getInventories().contains(top)) {
			player.closeInventory();
			player.sendMessage("§cInventário fechado pois o banco de dados de menus foi encerrado.");
		    }
		}
	    }
	}
    }
}
