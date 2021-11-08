package com.github.arthurfiorette.mysouls.extensions;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.mysouls.storage.SqliteDatabase;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.sinklibrary.component.Service;
import com.github.arthurfiorette.sinklibrary.component.providers.ComponentProvider.State;
import java.util.Collection;
import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;

@RequiredArgsConstructor
public class BStatsService implements Service {

  private static final int PLUGIN_ID = 9601;
  private static final String CHART_ID = "average_souls_per_player";

  @Getter
  private final MySouls basePlugin;

  @Getter
  private Metrics metrics = null;

  @Override
  public void enable() throws Exception {
    this.metrics = new Metrics(this.basePlugin, BStatsService.PLUGIN_ID);

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

  private boolean isReady() {
    return (
      this.basePlugin != null &&
      this.basePlugin.getProvider().state() == State.ENABLED &&
      this.basePlugin.isEnabled()
    );
  }

  private Callable<Integer> singleLineChartCallable() {
    return () -> {
      // Plugin isn't ready
      if (!this.isReady()) {
        return -1;
      }

      final WalletStorage storage = this.basePlugin.get(WalletStorage.class);
      final Collection<Wallet> wallets = storage.operationSync(d ->
        ((SqliteDatabase) d).getAll()
      );
      return wallets
        .parallelStream()
        .reduce(0, (acc, wallet) -> acc + wallet.size(), Integer::sum);
    };
  }
}
