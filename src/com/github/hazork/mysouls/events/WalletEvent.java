package com.github.hazork.mysouls.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.hazork.mysouls.souls.SoulWallet;

/**
 * Represents a event in which a wallet is involved.
 * 
 * @author github.com/Hazork
 */
public interface WalletEvent {

    /**
     * Returns the {@link SoulWallet} involved in the event.
     * 
     * @return the wallet
     */
    SoulWallet getWallet();

    /**
     * Return the {@link Player} who owns the wallet.
     * 
     * @return the wallet's owner or null if it's not online.
     */
    default Player getOwner() {
	return Bukkit.getPlayer(getWallet().getOwnerId());
    }

}
