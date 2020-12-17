package com.github.hazork.mysouls.apis;

import com.github.hazork.mysouls.utils.Utils;

public class APIController {

    private BStatsAPI bstats = new BStatsAPI();
    private PlaceholderAPI papi = null;

    public void enable() {
	bstats = new BStatsAPI();
	if (Utils.hasPlugin("PlaceholderAPI")) {
	    papi = new PlaceholderAPI();
	    papi.register();
	}
    }

    public void disable() {}

    public PlaceholderAPI getPlaceholderAPI() {
	return papi;
    }

    public BStatsAPI getBStatsAPI() {
	return bstats;
    }

}
