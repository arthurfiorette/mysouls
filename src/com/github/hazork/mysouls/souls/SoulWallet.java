package com.github.hazork.mysouls.souls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.utils.Utils.ItemStacks;

public class SoulWallet {

    final UUID ownerId;
    Map<UUID, Integer> souls = new HashMap<>();

    public SoulWallet(UUID ownerId) {
	this.ownerId = ownerId;
	souls.put(ownerId, 2);
    }

    /* ADD SOULS */

    public boolean canAddSoul(UUID soul) {
	return canAddSouls(soul, 1);
    }

    public boolean canAddSouls(UUID soul, int amount) {
	return (souls.containsKey(soul)) ? (souls.get(soul) + amount <= 64) : (true);
    }

    boolean addSoul(UUID soul) {
	if (canAddSoul(soul)) {
	    souls.compute(soul, (k, v) -> v == null ? 1 : v + 1);
	    return true;
	} else return false;
    }

    /* REMOVE SOULS */

    public boolean canRemoveSoul() {
	return canRemoveSouls(null, 1);
    }

    public boolean canRemoveSouls(int amount) {
	return canRemoveSouls(null, amount);
    }

    public boolean canRemoveSoul(UUID soul) {
	return canRemoveSouls(soul, 1);
    }

    public boolean canRemoveSouls(UUID soul, int amount) {
	return (amount <= 64 && amount >= 1) && getSoulsCount(soul) >= amount;
    }

    private UUID removeSoul() {
	return removeSoul(null);
    }

    private UUID removeSoul(UUID soul) {
	if (canRemoveSoul(soul)) {
	    if (soul == null) soul = souls.entrySet().stream().findAny().get().getKey();
	    souls.compute(soul, (k, v) -> v <= 1 ? null : v - 1);
	    return soul;
	} else return null;
    }

    private List<UUID> removeSouls(UUID soul, int amount) {
	List<UUID> list = new ArrayList<>();
	if (canRemoveSouls(soul, amount)) {
	    for (int i = 0; i < amount; i++) list.add(removeSoul(soul));
	}
	return list;
    }

    /* REPORT DEATH */

    public void reportDeath(Player killer) {
	reportDeath(MySouls.getDB().from(killer));
    }

    public void reportDeath(SoulWallet killer) {
	if (canRemoveSoul()) {
	    UUID soul = removeSoul();
	    killer.addSoul(soul);
	}
    }

    /* WITHDRAW SOULS */

    public static final String SOUL_VALUE = "souls-skulls-withdrawable-01";

    public ItemStack withdrawSoul() {
	return withdrawSoul(null);
    }

    public ItemStack withdrawSoul(UUID soul) {
	if (canRemoveSoul(soul)) {
	    soul = removeSoul(soul);
	    return soulToItem(soul);
	} else return null;
    }

    private ItemStack soulToItem(UUID soul) {
	HashMap<String, String> placeholders = new HashMap<String, String>();
	placeholders.put("{wallet}", getName());
	placeholders.put("{player}", Bukkit.getOfflinePlayer(soul).getName());

	OfflinePlayer owner = Bukkit.getOfflinePlayer(soul);

	ItemStack skull = ItemStacks.getHead(owner.getName());
	ItemStacks.setName(skull, Lang.SOUL_NAME.getFormat(placeholders));
	ItemStacks.setLore(skull, Lang.SOUL_LORE.getListFormat(placeholders));
	ItemStacks.removeFlags(skull);
	skull = ItemStacks.createNBT(skull, nbt -> nbt.setString(MySouls.NAME + ".uuid", soul.toString()));
	return ItemStacks.createNBT(skull, SOUL_VALUE);
    }

    /* WITHDRAW SOUL COIN */

    public static final String COIN_VALUE = "souls-skulls-withdrawable-02";

    public ItemStack withdrawCoin() {
	if (canRemoveSoul()) {
	    return withdrawCoins(1);
	} else return null;
    }

    public ItemStack withdrawCoins(int amount) {
	if (canRemoveSouls(amount)) {
	    removeSouls(null, amount);
	    ItemStack coin = COIN.clone();
	    ItemStacks.setName(coin, Lang.COIN_NAME.getText());
	    ItemStacks.setLore(coin, Lang.COIN_LORE.getTextList());
	    ItemStacks.removeFlags(coin);
	    coin.setAmount(amount);
	    return ItemStacks.createNBT(coin, COIN_VALUE);
	} else return null;
    }

    public static ItemStack COIN = ItemStacks.getHeadFromUrl(
	    "http://textures.minecraft.net/texture/77b9dfd281deaef2628ad5840d45bcda436d6626847587f3ac76498a51c861");

    /* GETTERS */

    public List<UUID> getSoulsList() {
	List<UUID> request = new ArrayList<>();
	for (Entry<UUID, Integer> entry : souls.entrySet()) {
	    for (int i = 0; i < entry.getValue(); i++) request.add(entry.getKey());
	}
	return request;
    }

    public Set<UUID> getSoulsSet() {
	return souls.keySet();
    }

    public int getSoulsCount() {
	return getSoulsCount(null);
    }

    public int getDifferentSoulsCount() {
	return souls.size();
    }

    public OfflinePlayer getMostKilledPlayer() {
	if (souls.isEmpty()) return null;
	return Bukkit.getOfflinePlayer(Collections.max(souls.entrySet(), Map.Entry.comparingByValue()).getKey());
    }

    public int getSoulsCount(UUID uuid) {
	if (uuid == null) return souls.values().stream().reduce(0, Integer::sum);
	else return souls.containsKey(uuid) ? souls.get(uuid) : 0;
    }

    public OfflinePlayer getPlayer() {
	return Bukkit.getOfflinePlayer(getOwnerId());
    }

    public String getName() {
	return getPlayer().getName();
    }

    public UUID getOwnerId() {
	return ownerId;
    }

}
