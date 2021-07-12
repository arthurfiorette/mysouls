package com.github._hazork.oldmysouls.guis;

import com.github._hazork.oldmysouls.MySouls;
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

public class GuiListener implements Listener {

  private static Map<UUID, Consumer<String>> actionsMap = new HashMap<>();

  private final MySouls plugin;

  public GuiListener(final MySouls plugin) {
    this.plugin = plugin;
  }

  public static void setChatAction(final UUID uuid, final Consumer<String> action) {
    GuiListener.actionsMap.put(uuid, action);
  }

  public void register() {
    Bukkit.getPluginManager().registerEvents(this, this.plugin);
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onChat(final AsyncPlayerChatEvent event) {
    final UUID uuid = event.getPlayer().getUniqueId();
    if (GuiListener.actionsMap.containsKey(uuid)) {
      GuiListener.actionsMap.remove(uuid).accept(event.getMessage());
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.LOWEST)
  public void onInventory(final InventoryClickEvent event) {
    final GuiDB guiDb = MySouls.getGuiDB();
    final Player player = (Player) event.getWhoClicked();
    guiDb
      .fromCache(player.getUniqueId())
      .ifPresent(
        container -> container.getGui(event.getInventory()).ifPresent(gui -> gui.onClick(event))
      );
  }
}
