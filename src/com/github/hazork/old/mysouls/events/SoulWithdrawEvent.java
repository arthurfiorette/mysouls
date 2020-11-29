package com.github.hazork.old.mysouls.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.hazork.old.mysouls.events.abstracts.SoulEvent;
import com.github.hazork.old.mysouls.events.abstracts.WalletEvent;
import com.github.hazork.old.mysouls.souls.SoulWallet;

/**
 * Represents a soul event in which a soul is withdrawn from a wallet.
 * 
 * @author github.com/Hazork
 *
 * @see {@link SoulWallet}
 * @see {@link WalletEvent}
 * @see {@link SoulEvent}
 */
public class SoulWithdrawEvent extends Event implements WalletEvent, SoulEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final SoulWallet wallet;
    private final UUID soul;

    /**
     * Create a new SoulWithdrawEvent.
     * 
     * @param soul   the soul involved in the event.
     * @param wallet the wallet involved in the event.
     */
    public SoulWithdrawEvent(UUID soul, SoulWallet wallet) {
	this.soul = soul;
	this.wallet = wallet;
    }

    @Override
    public SoulWallet getWallet() {
	return wallet;
    }

    @Override
    public UUID getSoul() {
	return soul;
    }

    @Override
    public HandlerList getHandlers() {
	return HANDLERS;
    }

    public HandlerList getHandlerList() {
	return HANDLERS;
    }
}
