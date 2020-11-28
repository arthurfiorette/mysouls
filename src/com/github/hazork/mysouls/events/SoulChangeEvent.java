package com.github.hazork.mysouls.events;

import java.util.UUID;

import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.utils.Event;

public class SoulChangeEvent extends Event {

    private final UUID soul;
    private SoulWallet changer;
    private SoulWallet reviever;

    public SoulChangeEvent(UUID soul, SoulWallet changer, SoulWallet reviever) {
	this.soul = soul;
	this.changer = changer;
	this.reviever = reviever;
    }

    public UUID getSoul() {
	return soul;
    }

    public SoulWallet getChanger() {
	return changer;
    }

    public SoulWallet getReviever() {
	return reviever;
    }
}
