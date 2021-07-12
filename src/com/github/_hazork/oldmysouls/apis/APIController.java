package com.github._hazork.oldmysouls.apis;

import com.github._hazork.oldmysouls.utils.Utils;

public class APIController {

  private BStatsAPI bstats = new BStatsAPI();
  private PlaceholderAPI papi = null;

  public void enable() {
    this.bstats = new BStatsAPI();
    if (Utils.hasPlugin("PlaceholderAPI")) {
      this.papi = new PlaceholderAPI();
      this.papi.register();
    }
  }

  public void disable() {}

  public PlaceholderAPI getPlaceholderAPI() {
    return this.papi;
  }

  public BStatsAPI getBStatsAPI() {
    return this.bstats;
  }
}
