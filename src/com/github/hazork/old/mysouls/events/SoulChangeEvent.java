package com.github.hazork.old.mysouls.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.github.hazork.old.mysouls.events.abstracts.WalletEvent;
import com.github.hazork.old.mysouls.souls.SoulWallet;

/**
 * Represents a soul event in which a soul is exchanged for a wallet.
 * 
 * @author github.com/Hazork
 *
 * @see {@link SoulWithdrawEvent}
 * @see {@link SoulWallet}
 */
public class SoulChangeEvent extends SoulWithdrawEvent {

    private final SoulWallet payer;

    /**
     * Create a new SoulWithdrawEvent.
     * 
     * @param soul   the soul involved in the event.
     * @param wallet the wallet involved in the event.
     * @param payer  the wallet payer involved in the event.
     */
    public SoulChangeEvent(UUID soul, SoulWallet wallet, SoulWallet payer) {
	super(soul, wallet);
	this.payer = payer;
    }

    /**
     * Returns the {@link SoulWallet} involved in the event as the <b>receiver</b>.
     * 
     * @return the wallet.
     */
    @Override
    public SoulWallet getWallet() {
	return super.getWallet();
    }

    /**
     * Clarity method for getting the receiver wallet. Not really needed except for
     * reasons of clarity.
     *
     * @see {@link SoulWithdrawEvent#getWallet()}
     *
     * @return the wallet.
     */
    public SoulWallet getReceiverWallet() {
	return super.getWallet();
    }

    /**
     * Returns the {@link SoulWallet} involved in the event as the <b>payer</b>.
     * 
     * @return the wallet.
     */
    public SoulWallet getPayerWallet() {
	return payer;
    }

    /**
     * Clarity method for getting the receiver wallet owner. Not really needed
     * except for reasons of clarity.
     *
     * @return player or null if it's not online.
     *
     * @see {@link WalletEvent#getOwner()}
     */
    public Player getReceiver() {
	return super.getOwner();
    }

    /**
     * Return the {@link Player} who owns the payer wallet.
     * 
     * @return player or null if it's not online.
     */
    public Player getPayer() {
	return Bukkit.getPlayer(getPayerWallet().getUUID());
    }
}
