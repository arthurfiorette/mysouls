package com.github.hazork.mysouls.souls;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.config.Config;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.ItemBuilder;
import com.github.hazork.mysouls.utils.Nbts;
import com.github.hazork.mysouls.utils.Utils;

public class SoulComunicator {

    private final UUID ownerId;

    static SoulComunicator of(UUID ownerId) {
	return new SoulComunicator(ownerId);
    }

    public static SoulComunicator of(Player player) {
	return of(player.getUniqueId());
    }

    private SoulComunicator(UUID ownerId) {
	this.ownerId = ownerId;
    }

    public void reportDeath(Player killer) {
	if (isOnline()) {
	    SoulWallet wallet = getWallet();
	    String kMessage = Lang.KILL_MESSAGE_FAIL.getText();
	    String eMessage = Lang.DEATH_MESSAGE_FAIL.getText();
	    SoulWallet kWallet = MySouls.getDB().from(killer);
	    if (wallet.reportDeath(kWallet)) {
		kMessage = Lang.KILL_MESSAGE.getText("{player}", getPlayer().getName());
		eMessage = Lang.DEATH_MESSAGE.getText("{player}", killer.getName());
	    }
	    sendMessage(eMessage);
	    of(killer).sendMessage(kMessage);
	}
    }

    public void withdrawCoins(int amount) {
	if (isOnline()) {
	    Lang message = null;
	    SoulWallet wallet = getWallet();
	    if (Utils.hasEmptySlot(getPlayer())) {
		if (wallet.canRemoveSoul(null, amount)) {
		    wallet.removeSoul(null, amount);
		    getPlayer().getInventory().addItem(coinToItem(amount));
		    message = Lang.SOUL_REMOVED;
		} else {
		    message = Lang.DONT_HAVE_SOULS;
		}
	    } else {
		message = Lang.INVENTORY_FULL;
	    }
	    sendMessage(message.getText());
	}
    }

    public void collectSouls(ItemStack coin) {
	if (Nbts.isNbtsItem(coin)) {
	    if (isOnline()) {
		if (Nbts.getIdValue(coin).equals(SoulWallet.SOUL_ID)) {
		    Lang message = null;
		    SoulWallet wallet = getWallet();
		    UUID soul = UUID.fromString(Nbts.getValue(coin));
		    int amount = coin.getAmount();
		    if (wallet.canAddSoul(soul, 1)) {
			wallet.addSoul(soul, 1);
			if (amount > 1) {
			    coin.setAmount(amount - 1);
			} else {
			    getPlayer().setItemInHand(null);
			}
			message = Lang.SOUL_ADDED;
		    } else {
			message = Lang.SOUL_64_LIMIT;
		    }
		    sendMessage(message.getText());
		}
	    }
	}
    }

    public void withdrawSoul(UUID soul) {
	if (isOnline()) {
	    Lang message = null;
	    SoulWallet wallet = getWallet();
	    if (Utils.hasEmptySlot(getPlayer())) {
		if (wallet.canRemoveSoul(soul, 1)) {
		    UUID realSoul = wallet.removeSoul(soul, 1);
		    getPlayer().getInventory().addItem(soulToItem(realSoul));
		    message = Lang.SOUL_REMOVED;
		} else {
		    message = Lang.DONT_HAVE_SOULS;
		}
	    } else {
		message = Lang.INVENTORY_FULL;
	    }
	    sendMessage(message.getText());
	}
    }

    public void sendMessage(String message) {
	getPlayer().sendMessage(message);
	Utils.playSound(Sound.ORB_PICKUP, getPlayer());
    }

    public SoulWallet getWallet() {
	return MySouls.getDB().from(ownerId);
    }

    public UUID getOwnerId() {
	return ownerId;
    }

    public Player getPlayer() {
	return Bukkit.getPlayer(ownerId);
    }

    public boolean isOnline() {
	return Bukkit.getOfflinePlayer(ownerId).isOnline();
    }

    private static ItemStack coin = ItemBuilder.ofHeadUrl(Config.COIN_HEAD_URL.getText(), true)
	    .setName(Lang.COIN_NAME.getText()).setLore(Lang.COIN_LORE.getList()).build();

    public synchronized static ItemStack coinToItem(int amount) {
	coin.setAmount(amount);
	return Nbts.saveValue(coin, SoulWallet.COIN_ID, null);
    }

    public static ItemStack soulToItem(UUID soul) {
	ItemBuilder builder = ItemBuilder.ofHead(Bukkit.getOfflinePlayer(soul), true);
	builder.setName(Lang.SOUL_NAME.getText("{player}", Bukkit.getOfflinePlayer(soul).getName()));
	builder.setLore(Lang.SOUL_LORE.getList());
	return Nbts.saveValue(builder.build(), SoulWallet.SOUL_ID, soul.toString());
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (!(obj instanceof SoulComunicator)) {
	    return false;
	}
	SoulComunicator other = (SoulComunicator) obj;
	if (ownerId == null) {
	    if (other.ownerId != null) {
		return false;
	    }
	} else if (!ownerId.equals(other.ownerId)) {
	    return false;
	}
	return true;
    }

}
