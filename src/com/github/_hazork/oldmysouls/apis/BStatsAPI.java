package com.github._hazork.oldmysouls.apis;

import com.github._hazork.oldmysouls.MySouls;
import java.util.function.Consumer;
import org.bstats.bukkit.Metrics;

public class BStatsAPI {

  public static final int pluginId = 9601;
  private Metrics metrics = null;

  public BStatsAPI() {
    this.metrics = new Metrics(MySouls.get(), BStatsAPI.pluginId);
  }

  public void metrics(final Consumer<Metrics> metrics) {
    metrics.accept(this.metrics);
  }
}
