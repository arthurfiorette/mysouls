package com.github.hazork.old.mysouls.events.abstracts;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public interface SoulEvent {

    /**
     * Returns the uuid representative of the soul
     * 
     * @return the uuid.
     */
    UUID getSoul();

    /**
     * Returns the soul's player.
     * 
     * @return the player, or {@code null} if not online.
     */
    default Player getPlayer() {
	return Bukkit.getPlayer(getSoul());
    }

}
