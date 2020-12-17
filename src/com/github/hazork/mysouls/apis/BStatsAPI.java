package com.github.hazork.mysouls.apis;

import java.util.function.Consumer;

import org.bstats.bukkit.Metrics;

import com.github.hazork.mysouls.MySouls;

public class BStatsAPI {

    public static final int pluginId = 9601;
    private Metrics metrics = null;

    public BStatsAPI() {
	metrics = new Metrics(MySouls.get(), pluginId);
    }

    public void metrics(Consumer<Metrics> metrics) {
	metrics.accept(this.metrics);
    }

}
