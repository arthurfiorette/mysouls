package com.github.hazork.mysouls.events;

import java.util.UUID;

import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.utils.Event;

public class SoulWithdrawEvent extends Event {

    private final UUID soul;
    private SoulWallet wallet;

    public SoulWithdrawEvent(UUID soul, SoulWallet wallet) {
	this.soul = soul;
	this.wallet = wallet;
    }

    public UUID getSoul() {
	return soul;
    }

    public SoulWallet getWallet() {
	return wallet;
    }
}
