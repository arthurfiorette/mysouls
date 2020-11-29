package com.github.hazork.mysouls.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.github.hazork.mysouls.souls.SoulWallet;

public class SoulWithdrawEvent extends Event implements SoulEvent, WalletEvent {

    private static final HandlerList handlers = new HandlerList();

    private UUID soul;
    private SoulWallet wallet;

    public SoulWithdrawEvent(UUID soul, SoulWallet wallet) {
	this.soul = soul;
	this.wallet = wallet;
    }

    @Override
    public UUID getSoul() {
	return soul;
    }

    @Override
    public SoulWallet getWallet() {
	return wallet;
    }

    @Override
    public HandlerList getHandlers() {
	return handlers;
    }

    public static HandlerList getHandlerList() {
	return handlers;
    }

}
