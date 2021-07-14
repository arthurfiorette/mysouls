package com.github.arthurfiorette.mysouls.extensions;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.mysouls.storage.WalletDatabase;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.sinklibrary.components.ManagerState;
import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseService;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;

public class BStatsService implements BaseService {

  private static final int PLUGIN_ID = 9601;
  private static final String CHART_ID = "average_souls_per_player";

  private final MySouls plugin;

  private Metrics metrics = null;

  public BStatsService(final MySouls plugin) {
    this.plugin = plugin;
  }

  @Override
  public void enable() throws Exception {
    this.metrics = new Metrics(this.plugin, BStatsService.PLUGIN_ID);

    final SingleLineChart chart = new SingleLineChart(
      BStatsService.CHART_ID,
      this.singleLineChartCallable()
    );

    this.metrics.addCustomChart(chart);
  }

  @Override
  public void disable() throws Exception {
    this.metrics = null;
  }

  @Override
  public BasePlugin getBasePlugin() {
    return this.plugin;
  }

  private Callable<Integer> singleLineChartCallable() {
    return () -> {
      // Plugin isn't ready
      if (
        (this.plugin == null) ||
        (this.plugin.getManager().getState() != ManagerState.ENABLED) ||
        !this.plugin.isEnabled()
      ) {
        return -1;
      }

      final WalletStorage storage = this.plugin.getComponent(WalletStorage.class);

      // We can use sync operations because this whole execution is
      // asynchronous.
      final Collection<Wallet> wallets = storage.operationSync(d -> ((WalletDatabase) d).getAll());

      return wallets
        .parallelStream()
        .reduce(0, (acc, wallet) -> acc + wallet.getSoulCount(), Integer::sum);
    };
  }
}
