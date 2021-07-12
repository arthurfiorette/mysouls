package com.github._hazork.oldmysouls.souls;

import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.data.config.Config;
import com.github._hazork.oldmysouls.data.lang.Lang;
import com.github._hazork.oldmysouls.utils.ItemBuilder;
import com.github._hazork.oldmysouls.utils.Nbts;
import com.github._hazork.oldmysouls.utils.Utils;

public class SoulComunicator {

  private final UUID ownerId;

  static SoulComunicator of(final UUID ownerId) {
    return new SoulComunicator(ownerId);
  }

  public static SoulComunicator of(final Player player) {
    return SoulComunicator.of(player.getUniqueId());
  }

  private SoulComunicator(final UUID ownerId) {
    this.ownerId = ownerId;
  }

  public void reportDeath(final Player killer) {
    if (this.isOnline()) {
      final SoulWallet wallet = this.getWallet();
      String kMessage = Lang.KILL_MESSAGE_FAIL.getText();
      String eMessage = Lang.DEATH_MESSAGE_FAIL.getText();
      final SoulWallet kWallet = MySouls.getDB().from(killer);
      if (wallet.reportDeath(kWallet)) {
        kMessage = Lang.KILL_MESSAGE.getText("{player}", this.getPlayer().getName());
        eMessage = Lang.DEATH_MESSAGE.getText("{player}", killer.getName());
      }
      this.sendMessage(eMessage);
      SoulComunicator.of(killer).sendMessage(kMessage);
    }
  }

  public void withdrawCoins(final int amount) {
    if (this.isOnline()) {
      Lang message = null;
      final SoulWallet wallet = this.getWallet();
      if (Utils.hasEmptySlot(this.getPlayer())) {
        if (wallet.canRemoveSoul(null, amount)) {
          wallet.removeSoul(null, amount);
          this.getPlayer().getInventory().addItem(SoulComunicator.coinToItem(amount));
          message = Lang.SOUL_REMOVED;
        } else {
          message = Lang.DONT_HAVE_SOULS;
        }
      } else {
        message = Lang.INVENTORY_FULL;
      }
      this.sendMessage(message.getText());
    }
  }

  public void collectSouls(final ItemStack coin) {
    if ((Nbts.isNbtsItem(coin) && this.isOnline())
        && Nbts.getIdValue(coin).equals(SoulWallet.SOUL_ID)) {
      Lang message = null;
      final SoulWallet wallet = this.getWallet();
      final UUID soul = UUID.fromString(Nbts.getValue(coin));
      final int amount = coin.getAmount();
      if (wallet.canAddSoul(soul, 1)) {
        wallet.addSoul(soul, 1);
        if (amount > 1) {
          coin.setAmount(amount - 1);
        } else {
          this.getPlayer().setItemInHand(null);
        }
        message = Lang.SOUL_ADDED;
      } else {
        message = Lang.SOUL_64_LIMIT;
      }
      this.sendMessage(message.getText());
    }
  }

  public void withdrawSoul(final UUID soul) {
    if (this.isOnline()) {
      Lang message = null;
      final SoulWallet wallet = this.getWallet();
      if (Utils.hasEmptySlot(this.getPlayer())) {
        if (wallet.canRemoveSoul(soul, 1)) {
          final UUID realSoul = wallet.removeSoul(soul, 1);
          this.getPlayer().getInventory().addItem(SoulComunicator.soulToItem(realSoul));
          message = Lang.SOUL_REMOVED;
        } else {
          message = Lang.DONT_HAVE_SOULS;
        }
      } else {
        message = Lang.INVENTORY_FULL;
      }
      this.sendMessage(message.getText());
    }
  }

  public void sendMessage(final String message) {
    this.getPlayer().sendMessage(message);
    Utils.playSound(Sound.ORB_PICKUP, this.getPlayer());
  }

  public SoulWallet getWallet() {
    return MySouls.getDB().from(this.ownerId);
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

  private static ItemStack coin = ItemBuilder.ofHeadUrl(Config.COIN_HEAD_URL.getText(), true)
      .setName(Lang.COIN_NAME.getText()).setLore(Lang.COIN_LORE.getList()).build();

  public static ItemStack coinToItem(final int amount) {
    SoulComunicator.coin.setAmount(amount);
    return Nbts.saveValue(SoulComunicator.coin, SoulWallet.COIN_ID, null);
  }

  public static ItemStack soulToItem(final UUID soul) {
    final ItemBuilder builder = ItemBuilder.ofHead(Bukkit.getOfflinePlayer(soul), true);
    builder.setName(Lang.SOUL_NAME.getText("{player}", Bukkit.getOfflinePlayer(soul).getName()));
    builder.setLore(Lang.SOUL_LORE.getList());
    return Nbts.saveValue(builder.build(), SoulWallet.SOUL_ID, soul.toString());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.ownerId);
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof SoulComunicator)) {
      return false;
    }
    final SoulComunicator other = (SoulComunicator) obj;
    if (!Objects.equals(this.ownerId, other.ownerId)) {
      return false;
    }
    return true;
  }

}
