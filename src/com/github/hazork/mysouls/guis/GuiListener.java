package com.github.hazork.mysouls.guis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.github.hazork.mysouls.MySouls;

public class GuiListener implements Listener {

    private static Map<UUID, Consumer<String>> actionsMap = new HashMap<>();

    private final MySouls plugin;

    public GuiListener(MySouls plugin) {
	this.plugin = plugin;
    }

    public static void setChatAction(UUID uuid, Consumer<String> action) {
	actionsMap.put(uuid, action);
    }

    public void register() {
	Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent event) {
	UUID uuid = event.getPlayer().getUniqueId();
	if (actionsMap.containsKey(uuid)) {
	    actionsMap.remove(uuid).accept(event.getMessage());
	    event.setCancelled(true);
	}
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventory(InventoryClickEvent event) {
	GuiDB guiDb = MySouls.getGuiDB();
	Player player = (Player) event.getWhoClicked();
	guiDb.fromCache(player.getUniqueId())
		.ifPresent(container -> container.getGui(event.getInventory()).ifPresent(gui -> gui.onClick(event)));
    }
}
