package com.github._hazork.oldmysouls.guis;

import com.github._hazork.oldmysouls.guis.implementations.GeneralGui;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.Nullable;

public class GuiContainer {

  private final UUID ownerId;
  private final Map<String, Gui> guiMap = new HashMap<>();

  public GuiContainer(final UUID ownerId) {
    this.ownerId = ownerId;
    this.addGuis(new GeneralGui(ownerId));
  }

  private void addGuis(final Gui... guis) {
    for (final Gui gui : guis) {
      this.guiMap.put(gui.getName(), gui);
    }
  }

  public List<Inventory> getInventories() {
    return this.guiMap.values().stream().map(Gui::getInventory).collect(Collectors.toList());
  }

  @Nullable
  public Optional<Gui> getGui(final Inventory inv) {
    for (final Gui gui : this.guiMap.values()) {
      if (gui.getInventory().equals(inv)) {
        return Optional.of(gui);
      }
    }
    return Optional.empty();
  }

  @Nullable
  public Optional<Gui> getGui(final String name) {
    return Optional.ofNullable(this.guiMap.get(name));
  }

  public void open(final String name) {
    this.getPlayer()
      .ifPresent(
        player -> {
          this.getGui(name).ifPresent(gui -> gui.open(false));
        }
      );
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }

  public Optional<Player> getPlayer() {
    return Optional.ofNullable(Bukkit.getPlayer(this.getOwnerId()));
  }
}
