package com.github.hazork.mysouls.souls;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.ItemBuilder;
import com.github.hazork.mysouls.utils.Nbts;
import com.github.hazork.mysouls.utils.Utils;
import com.google.common.base.Verify;

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
	souls.put(ownerId, 2);
    }

    public boolean canAddSoul(UUID soul, int amount) {
	if (soul == null) return false;
	else if (!Utils.isMinecraftPack(amount)) return false;
	else if (souls.containsKey(soul)) return souls.get(soul) + amount <= 64;
	else return true;
    }

    boolean addSoul(UUID soul) {
	if (canAddSoul(soul, 1)) {
	    souls.compute(soul, (k, v) -> v == null ? 1 : v + 1);
	    return true;
	} else return false;
    }

    public boolean canRemoveSoul(@Nullable UUID soul, int amount) {
	return Utils.isMinecraftPack(amount) && soulsCount(soul) >= amount;
    }

    UUID removeSoul(@Nullable UUID soul) {
	if (canRemoveSoul(soul, 1)) {
	    if (soul == null) {
		soul = souls.entrySet().stream().findAny().get().getKey();
	    }
	    souls.compute(soul, (k, v) -> v <= 1 ? null : v - 1);
	    return soul;
	} else return null;
    }

    private int removeSouls(@Nullable UUID soul, int amount) {
	if (!canRemoveSoul(soul, amount)) return 0;
	else {
	    Verify.verify(amount >= 0, "Amount must be greater than or equal to 0, value: %s", amount);
	    int souls = 0;
	    for (int i = 0; i < amount; i++) {
		removeSoul(soul);
		souls++;
	    }
	    return souls;
	}
    }

    public void reportDeath(SoulWallet killer) {
	if (canRemoveSoul(ANY, 1)) {
	    UUID soul = removeSoul(ANY);
	    killer.addSoul(soul);
	}
    }

    public ItemStack withdrawSoul(UUID soul) {
	if (canRemoveSoul(soul, 1)) {
	    soul = removeSoul(soul);
	    ItemBuilder builder = ItemBuilder.ofHead(Bukkit.getOfflinePlayer(soul), true);
	    builder.setName(Lang.SOUL_NAME.getText("{player}", Bukkit.getOfflinePlayer(soul).getName()));
	    builder.setLore(Lang.SOUL_LORE.getList("{wallet}", getPlayer().getName()));
	    return Nbts.saveValue(builder.build(), SOUL_ID, soul.toString());
	} else return null;
    }

    public ItemStack withdrawCoins(int amount) {
	if (canRemoveSoul(ANY, amount)) {
	    removeSouls(ANY, amount);
	    String url = "http://textures.minecraft.net/texture/77b9dfd281deaef2628ad5840d45bcda436d6626847587f3ac76498a51c861";
	    ItemBuilder builder = ItemBuilder.ofHeadUrl(url, true);
	    builder.setName(Lang.COIN_NAME.getText());
	    builder.setLore(Lang.COIN_LORE.getList());
	    builder.setAmount(amount);
	    return Nbts.saveValue(builder.build(), COIN_ID, null);
	} else return null;
    }

    public Set<UUID> getSouls() {
	return souls.keySet();
    }

    public int size() {
	return souls.size();
    }

    public int soulsCount() {
	return soulsCount(ANY);
    }

    public int soulsCount(@Nullable UUID uuid) {
	if (uuid == null) return souls.values().stream().reduce(0, Integer::sum);
	else return souls.containsKey(uuid) ? souls.get(uuid) : 0;
    }

    public double soulsRatio() {
	return (double) soulsCount() / size();
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
