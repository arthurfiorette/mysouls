package com.github.hazork.mysouls.apis.implementations;

import org.bstats.bukkit.Metrics;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.apis.MySoulsAPI;

public class BStatsAPI implements MySoulsAPI {

    public static final int pluginId = 9601;
    private Metrics metrics = null;

    @Override
    public boolean canRegister() {
	try {
	    return (Class.forName("org.bstats.bukkit.Metrics") != null);
	} catch (ClassNotFoundException cannotRegister) {
	    return false;
	}
    }

    @Override
    public boolean register() {
	if (!isRegistered()) {
	    metrics = new Metrics(MySouls.get(), pluginId);
	}
	return isRegistered();
    }

    @Override
    public boolean unregister() {
	return false;
    }

    @Override
    public String getName() {
	return "bStats";
    }

    @Override
    public boolean isRegistered() {
	return metrics != null && metrics.isEnabled();
    }
}
