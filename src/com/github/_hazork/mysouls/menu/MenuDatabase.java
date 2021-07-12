package com.github._hazork.mysouls.menu;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.github.arthurfiorette.sinklibrary.data.database.MemoryDatabase;

public class MenuDatabase extends MemoryDatabase<SoulsMenu> {

  public SoulsMenu get(final Player player) {
    return this.get(player.getUniqueId());
  }

  public SoulsMenu get(final UUID uuid) {
    return this.get(FastUUID.toString(uuid));
  }
}
