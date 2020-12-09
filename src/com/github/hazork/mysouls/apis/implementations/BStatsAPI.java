package com.github.hazork.mysouls.apis.implementations;

import org.bstats.bukkit.Metrics;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.apis.MySoulsAPI;

public class BStatsAPI implements MySoulsAPI {

    public static final int PluginID = 9601;

    private Metrics metrics = null;

    @Override
    public boolean canRegister() {
	try {
	    Class<?> clazz = Class.forName("org.bstats.bukkit.Metrics");
	    return clazz != null;
	} catch (Exception e) {
	    return false;
	}
    }

    @Override
    public boolean register() {
	if (isRegistered()) return false;
	metrics = new Metrics(MySouls.get(), PluginID);
	System.out.println("BSTATS: " + isRegistered());
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
