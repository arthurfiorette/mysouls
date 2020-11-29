package com.github.hazork.old.mysouls.events.abstracts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.hazork.old.mysouls.souls.SoulWallet;

/**
 * Represents a soul event in which a wallet is involved.
 * 
 * @author github.com/Hazork
 *
 * @see {@link SoulWallet}
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
	return Bukkit.getPlayer(getWallet().getUUID());
    }

}
