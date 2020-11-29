package com.github.hazork.mysouls.events;

import java.util.UUID;

import com.github.hazork.mysouls.souls.SoulWallet;

public class SoulChangeEvent extends SoulWithdrawEvent {

    private final SoulWallet receiver;

    public SoulChangeEvent(SoulWallet payer, SoulWallet receiver, UUID soul) {
	super(soul, payer);
	this.receiver = receiver;
    }

    public SoulWallet getReceiver() {
	return receiver;
    }

}
