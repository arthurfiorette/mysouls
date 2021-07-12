package com.github._hazork.mysouls.souls;

import com.github._hazork.mysouls.SoulsPlugin;
import com.github._hazork.mysouls.data.lang.LangEnum;
import com.github._hazork.mysouls.utils.Nbts;
import com.github._hazork.mysouls.utils.Utils;
import com.github._hazork.oldmysouls.data.config.Config;
import com.github.arthurfiorette.sinklibrary.item.ItemBuilders;
import com.github.arthurfiorette.sinklibrary.replacer.Replacer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SoulCommunicator {

  private final UUID ownerId;

  private SoulCommunicator(final UUID ownerId) {
    this.ownerId = ownerId;
  }

  public static SoulCommunicator get(final OfflinePlayer player) {
    return SoulCommunicator.get(player.getUniqueId());
  }

  public static SoulCommunicator get(final UUID ownerId) {
    return new SoulCommunicator(ownerId);
  }

  public void reportDeath(final Player killer) {
    if (this.isOnline()) {
      final SoulAccount acc = this.getAccount();
      String kMessage = LangEnum.KILL_MESSAGE_FAIL.getText();
      String eMessage = LangEnum.DEATH_MESSAGE_FAIL.getText();
      final SoulAccount kAcc = SoulsPlugin.get().getStorage().get(killer.getUniqueId());
      if (acc.tradeSoul(kAcc)) {
        kMessage =
          LangEnum.KILL_MESSAGE.getText(new Replacer().add("{player}", this.getPlayer().getName()));
        eMessage = LangEnum.DEATH_MESSAGE.getText(new Replacer().add("{player}", killer.getName()));
      }
      this.sendMessage(eMessage);
      SoulCommunicator.get(killer.getUniqueId()).sendMessage(kMessage);
    }
  }

  public void withdrawCoins(final int amount) {
    if (this.isOnline()) {
      LangEnum message = null;
      final SoulAccount acc = this.getAccount();
      if (SpigotServices.hasEmptySlot(this.getPlayer())) {
        if (acc.canIncrement(null, -amount)) {
          acc.increment(null, -amount);
          SpigotServices.giveItens(this.getPlayer(), SoulCommunicator.coinToItem(amount));
          message = LangEnum.SOUL_REMOVED;
        } else {
          message = LangEnum.DONT_HAVE_SOULS;
        }
      } else {
        message = LangEnum.INVENTORY_FULL;
      }
      this.sendMessage(message.getText());
    }
  }

  public void collectSouls(final ItemStack soul) {
    if (
      (Nbts.isNbtsItem(soul) && this.isOnline()) &&
      Nbts.getIdValue(soul).equals(SoulAccount.SOUL_ID)
    ) {}
  }

  public void withdrawSoul(final UUID soul) {}

  private static ItemStack coin = ItemBuilders
    .ofHeadUrl(Config.COIN_HEAD_URL.getText())
    .setName(LangEnum.COIN_NAME.getText())
    .setLore(LangEnum.COIN_LORE.getList())
    .build();

  public static ItemStack coinToItem(final int amount) {
    SoulCommunicator.coin.setAmount(amount);
    return Nbts.saveValue(SoulCommunicator.coin, SoulAccount.COIN_ID, null);
  }

  public static ItemStack soulToItem(final UUID soul) {
    final ItemBuilder builder = ItemBuilders.ofHead(Bukkit.getOfflinePlayer(soul));
    builder.setName(
      LangEnum.SOUL_NAME.getText(
        new Replacer().add("{player}", Bukkit.getOfflinePlayer(soul).getName())
      )
    );
    builder.setLore(LangEnum.SOUL_LORE.getList());
    return Nbts.saveValue(builder.build(), SoulAccount.SOUL_ID, soul.toString());
  }

  public void sendMessage(final String message) {
    this.getPlayer().sendMessage(message);
    Utils.playSound(Sound.ORB_PICKUP, this.getPlayer());
  }

  public SoulAccount getAccount() {
    return SoulsPlugin.get().getStorage().get(this.ownerId);
  }

  public UUID getOwnerId() {
    return this.ownerId;
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(this.ownerId);
  }

  public boolean isOnline() {
    return Bukkit.getOfflinePlayer(this.ownerId).isOnline();
  }
}
