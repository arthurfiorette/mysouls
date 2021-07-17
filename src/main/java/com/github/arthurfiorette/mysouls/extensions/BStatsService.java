package com.github.arthurfiorette.mysouls.extensions;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.mysouls.storage.WalletDatabase;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.sinklibrary.components.ManagerState;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BStatsService implements BaseService {

  private static final int PLUGIN_ID = 9601;
  private static final String CHART_ID = "average_souls_per_player";

  @Getter
  private final MySouls basePlugin;

  @Getter
  private Metrics metrics = null;

  @Override
  public void enable() throws Exception {
    this.metrics = new Metrics(this.basePlugin, BStatsService.PLUGIN_ID);

    final SingleLineChart chart = new SingleLineChart(BStatsService.CHART_ID,
        this.singleLineChartCallable());

    this.metrics.addCustomChart(chart);
  }

  @Override
  public void disable() throws Exception {
    this.metrics = null;
  }

  private boolean isReady() {
    return !(this.basePlugin == null
        || (this.basePlugin.getManager().getState() != ManagerState.ENABLED)
        || !this.basePlugin.isEnabled());
  }

  private Callable<Integer> singleLineChartCallable() {
    return () -> {
      // Plugin isn't ready
      if (!isReady()) return -1;

      final WalletStorage storage = this.basePlugin.getComponent(WalletStorage.class);
      final Collection<Wallet> wallets = storage.operationSync(d -> ((WalletDatabase) d).getAll());
      return wallets.parallelStream().reduce(0, (acc, wallet) -> acc + wallet.size(), Integer::sum);
    };
  }
}
