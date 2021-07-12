package com.github.arthurfiorette.mysouls.listeners;

import com.github.arthurfiorette.mysouls.lang.Lang;
import com.github.arthurfiorette.mysouls.lang.LangFile;
import com.github.arthurfiorette.mysouls.model.Wallet;
import com.github.arthurfiorette.mysouls.storage.WalletStorage;
import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.listener.SinkListener;
import com.github.arthurfiorette.sinklibrary.replacer.ReplacerFunction;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener extends SinkListener {

  private WalletStorage storage;
  private LangFile lang;

  public DeathListener(final BasePlugin basePlugin) {
    super(basePlugin);
  }

  @Override
  public void enable() {
    super.enable();
    this.storage = basePlugin.getComponent(WalletStorage.class);
    this.lang = basePlugin.getComponent(LangFile.class);
  }

  @Override
  @EventHandler(priority = EventPriority.MONITOR)
  public void onPlayerDeath(final PlayerDeathEvent event) {
    final Player victim = event.getEntity();
    final Player killer = victim.getKiller();

    if ((victim != null) && (killer != null)) {
      final Wallet victimWallet = this.storage.getSync(victim);
      final Wallet killerWallet = this.storage.getSync(killer);

      // Find a common soul
      final UUID soul = victimWallet.getRemoveableSoul(killerWallet, 1);

      // Init this message with the error one
      Lang victimMessage = Lang.DEATH_MESSAGE_FAIL;
      Lang killerMessage = Lang.KILL_MESSAGE_FAIL;

      // Soul found
      if (soul != null) {
        killerWallet.addSoul(victimWallet.removeSoul(soul, 1), 1);

        victimMessage = Lang.DEATH_MESSAGE;
        killerMessage = Lang.KILL_MESSAGE;
      }

      final ReplacerFunction replacer = r -> r.add("{victim}", victim.getName()).add("{killer}",
          killer.getName());

      victim.sendMessage(this.lang.getString(victimMessage, replacer));
      killer.sendMessage(this.lang.getString(killerMessage, replacer));
    }
  }
}
