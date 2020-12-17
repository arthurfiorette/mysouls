package com.github.hazork.mysouls.souls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.data.config.Config;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.ItemBuilder;
import com.github.hazork.mysouls.utils.Nbts;
import com.github.hazork.mysouls.utils.Utils;

public class SoulWallet {

    /**
     * Represents any soul to be used in class parameters and for reasons of
     * redability. Same as {@code null}
     */
    public static final UUID ANY = null;

    public static final String SOUL_ID = "soulsId";
    public static final String COIN_ID = "coinsId";

    final UUID ownerId;
    Map<UUID, Integer> souls = new HashMap<>();

    public SoulWallet(UUID ownerId) {
	this.ownerId = ownerId;
	souls.put(ownerId, Config.INITIAL_SOULS.getInt());
    }

    public boolean canAddSoul(UUID soul, int amount) {
	if ((soul == null) || !Utils.isMinecraftPack(amount)) {
	    return false;
	} else if (souls.containsKey(soul)) {
	    return souls.get(soul) + amount <= 64;
	} else {
	    return true;
	}
    }

    boolean addSoul(UUID soul) {
	if (canAddSoul(soul, 1)) {
	    souls.compute(soul, (k, v) -> v == null ? 1 : v + 1);
	    return true;
	} else {
	    return false;
	}
    }

    public boolean canRemoveSoul(@Nullable UUID soul, int amount) {
	return Utils.isMinecraftPack(amount) && soulsCount(soul) >= amount;
    }

    private UUID removeSoul(@Nullable UUID soul) {
	if (canRemoveSoul(soul, 1)) {
	    if (soul == null) {
		soul = Utils.getRandom(new ArrayList<>(souls.keySet()));
	    }
	    souls.compute(soul, (k, v) -> v <= 1 ? null : v - 1);
	    return soul;
	}
	return null;
    }

    public boolean removeSouls(@Nullable UUID soul, int amount) {
	if (canRemoveSoul(soul, amount)) {
	    for (int souls = 0; souls < amount; souls++) {
		removeSoul(soul);
	    }
	    return true;
	}
	return false;
    }

    public boolean reportDeath(SoulWallet killer) {
	if (canRemoveSoul(ANY, 1)) {
	    UUID soul = removeSoul(ANY);
	    killer.addSoul(soul);
	    return true;
	}
	return false;
    }

    public ItemStack withdrawSoul(UUID soul) {
	if (canRemoveSoul(soul, 1)) {
	    soul = removeSoul(soul);
	    ItemBuilder builder = ItemBuilder.ofHead(Bukkit.getOfflinePlayer(soul), true);
	    builder.setName(Lang.SOUL_NAME.getText("{player}", Bukkit.getOfflinePlayer(soul).getName()));
	    builder.setLore(Lang.SOUL_LORE.getList("{wallet}", getPlayer().getName()));
	    return Nbts.saveValue(builder.build(), SOUL_ID, soul.toString());
	}
	return null;
    }

    public ItemStack withdrawCoins(int amount) {
	if (canRemoveSoul(ANY, amount)) {
	    removeSouls(ANY, amount);
	    ItemBuilder builder = ItemBuilder.ofHeadUrl(Config.COIN_HEAD_URL.getText(), true);
	    builder.setName(Lang.COIN_NAME.getText());
	    builder.setLore(Lang.COIN_LORE.getList());
	    builder.setAmount(amount);
	    return Nbts.saveValue(builder.build(), COIN_ID, null);
	}
	return null;
    }

    public Set<UUID> getSouls() {
	return souls.keySet();
    }

    public int playerCount() {
	return souls.size();
    }

    public int soulsCount() {
	return soulsCount(ANY);
    }

    public int soulsCount(@Nullable UUID uuid) {
	if (uuid == null) {
	    return souls.values().stream().reduce(0, Integer::sum);
	}
	return souls.containsKey(uuid) ? souls.get(uuid) : 0;
    }

    public double soulsRatio() {
	return (double) soulsCount() / playerCount();
    }

    public <R> R entrySet(Function<Set<Map.Entry<UUID, Integer>>, R> function) {
	return function.apply(souls.entrySet());
    }

    public OfflinePlayer getPlayer() {
	return Bukkit.getOfflinePlayer(getOwnerId());
    }

    public UUID getOwnerId() {
	return ownerId;
    }
}
