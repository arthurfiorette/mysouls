package com.github.hazork.mysouls.guis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.github.hazork.mysouls.guis.implementations.GeneralGui;

public class GuiContainer {

    private final UUID ownerId;
    private Map<String, Gui> guiMap = new HashMap<>();

    public GuiContainer(UUID ownerId) {
	this.ownerId = ownerId;
	addGuis(new GeneralGui(ownerId));
    }

    private void addGuis(Gui... guis) {
	for(Gui gui: guis) {
	    guiMap.put(gui.getName(), gui);
	}
    }

    public List<Inventory> getInventories() {
	return guiMap.values().stream().map(Gui::getInventory).collect(Collectors.toList());
    }

    @Nullable
    public Optional<Gui> getGui(Inventory inv) {
	for(Gui gui: guiMap.values()) {
	    if (gui.getInventory().equals(inv)) {
		return Optional.of(gui);
	    }
	}
	return Optional.empty();
    }

    @Nullable
    public Optional<Gui> getGui(String name) {
	return Optional.ofNullable(guiMap.get(name));
    }

    public void open(String name) {
	getPlayer().ifPresent(player -> {
	    getGui(name).ifPresent(gui -> gui.open(true));
	    player.updateInventory();
	});
    }

    public UUID getOwnerId() {
	return ownerId;
    }

    public Optional<Player> getPlayer() {
	return Optional.ofNullable(Bukkit.getPlayer(getOwnerId()));
    }
}
