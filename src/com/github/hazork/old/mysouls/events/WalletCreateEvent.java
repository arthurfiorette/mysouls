package com.github.hazork.old.mysouls.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.hazork.old.mysouls.events.abstracts.WalletEvent;
import com.github.hazork.old.mysouls.souls.SoulWallet;

/**
 * Represents a wallet creation event.
 * 
 * @author github.com/Hazork
 *
 * @see {@link WalletEvent}
 * @see {@link SoulWallet}
 */
public class WalletCreateEvent extends Event implements WalletEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final SoulWallet wallet;

    /**
     * Create a new WalletCreateEvent.
     * 
     * @param wallet the wallet involved in the event.
     */
    public WalletCreateEvent(SoulWallet wallet) {
	this.wallet = wallet;
    }

    @Override
    public SoulWallet getWallet() {
	return wallet;
    }

    @Override
    public HandlerList getHandlers() {
	return HANDLERS;
    }

    public HandlerList getHandlerList() {
	return HANDLERS;
    }

}
