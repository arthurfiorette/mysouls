package com.github.hazork.mysouls.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import com.github.hazork.mysouls.MySouls;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class ItemBuilder {

    private transient ItemStack lastBuild;
    private transient boolean modified = false;

    private Material material;
    private EnumMap<Properties, Consumer<ItemStack>> properties = new EnumMap<>(Properties.class);

    public static ItemBuilder ofHead(OfflinePlayer player) {
	return ofHead(player, false);
    }

    public static ItemBuilder ofHead(OfflinePlayer player, boolean allItemFlags) {
	return ofSkullGameProfile(new GameProfile(player.getUniqueId(), player.getName()), allItemFlags);
    }

    public static ItemBuilder ofHead(String playername) {
	return ofHead(playername, false);
    }

    public static ItemBuilder ofHead(String playername, boolean allItemFlags) {
	ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, true).setDurability(3);
	return builder.addCustomMeta(im -> {
	    SkullMeta sm = (SkullMeta) im;
	    sm.setOwner(playername);
	    return sm;
	});
    }

    public static ItemBuilder ofHeadUrl(String url) {
	return ofHeadUrl(url, false);
    }

    public static ItemBuilder ofHeadUrl(String url, boolean allItemFlags) {
	GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
	profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	return ofSkullGameProfile(profile, true);
    }

    public static ItemBuilder ofSkullGameProfile(GameProfile gp) {
	return ofSkullGameProfile(gp, false);
    }

    public static ItemBuilder ofSkullGameProfile(GameProfile gp, boolean allItemFlags) {
	ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, allItemFlags).setDurability(3);
	builder.addCustomMeta(meta -> {
	    try {
		SkullMeta headMeta = (SkullMeta) meta;
		Field profileField = headMeta.getClass().getDeclaredField("profile");
		profileField.setAccessible(true);
		profileField.set(headMeta, gp);
		return headMeta;
	    } catch (IllegalAccessException | NoSuchFieldException exc) {
		MySouls.treatException(ItemBuilder.class, "Error when trying to load custom head via GameProfile", exc);
		return meta;
	    }
	});
	return builder;
    }

    public ItemBuilder(Material material, boolean removeAllItemFlags) {
	this(material);
	if (removeAllItemFlags) {
	    setItemFlags();
	}
    }

    public ItemBuilder(Material material) {
	this.material = (material == null ? Material.AIR : material);
    }

    public ItemBuilder setDurability(int durability) {
	return addProperties(Properties.DAMAGE, is -> is.setDurability((short) durability));
    }

    public ItemBuilder setAmount(int amount) {
	return addProperties(Properties.AMOUNT, is -> is.setAmount(amount));
    }

    public ItemBuilder setData(MaterialData data) {
	return addProperties(Properties.MATERIAL_DATA, is -> is.setData(data));
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
	return addProperties(Properties.ENCHANTMENT, is -> is.addUnsafeEnchantment(ench, level));
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
	return addProperties(Properties.ENCHANTMENT, is -> is.addUnsafeEnchantments(enchantments));
    }

    public ItemBuilder setName(String name) {
	return addProperties(Properties.NAME, is -> setItemMeta(is, im -> im.setDisplayName(name)));
    }

    public ItemBuilder setItemFlags() {
	return setItemFlags(ItemFlag.values());
    }

    public ItemBuilder setItemFlags(ItemFlag... itemFlags) {
	return addProperties(Properties.ITEM_FLAG, is -> setItemMeta(is, im -> {
	    im.removeItemFlags(ItemFlag.values());
	    im.addItemFlags(itemFlags);
	}));
    }

    public ItemBuilder addItemFlags(ItemFlag... itemFlags) {
	return addProperties(Properties.ITEM_FLAG, is -> setItemMeta(is, im -> im.addItemFlags(itemFlags)));
    }

    public ItemBuilder setLores(String... lorelines) {
	return setLore(Arrays.asList(lorelines));
    }

    public ItemBuilder setLore(List<String> lore) {
	return addProperties(Properties.LORE, is -> setItemMeta(is, im -> im.setLore(lore)));
    }

    public ItemBuilder addLores(String... lorelines) {
	return addLore(Arrays.asList(lorelines));
    }

    public ItemBuilder addLore(List<String> lore) {
	if (properties.containsKey(Properties.LORE)) {
	    return addProperties(Properties.LORE, is -> setItemMeta(is, im -> im.getLore().addAll(lore)));
	}
	return setLore(lore);
    }

    public ItemBuilder addCustomMeta(UnaryOperator<ItemMeta> customMeta) {
	return addProperties(Properties.CUSTOM_META, is -> is.setItemMeta(customMeta.apply(is.getItemMeta())));
    }

    public ItemStack build() {
	if (!modified) {
	    return getLastBuild();
	}
	ItemStack item = new ItemStack(material);
	properties.values().stream().forEach(c -> c.accept(item));
	return lastBuild = item;
    }

    public ItemStack getLastBuild() {
	return lastBuild;
    }

    public ItemMeta.Spigot spigot() {
	return build().getItemMeta().spigot();
    }

    @Override
    public ItemBuilder clone() {
	ItemBuilder clone = new ItemBuilder(material);
	clone.properties = properties;
	return clone;
    }

    public ItemBuilder remove(Properties property) {
	properties.remove(property);
	return this;
    }

    private ItemBuilder addProperties(Properties type, Consumer<ItemStack> consumer) {
	if (properties.containsKey(type) && type.isCumulative()) {
	    properties.compute(type, (k, v) -> v.andThen(consumer));
	} else {
	    properties.put(type, consumer);
	}
	modified = true;
	return this;
    }

    private static void setItemMeta(ItemStack item, Consumer<ItemMeta> callback) {
	ItemMeta meta = item.getItemMeta();
	callback.accept(meta);
	item.setItemMeta(meta);
    }

    public enum Properties {
	NAME(false),
	LORE(false),
	DAMAGE(false),
	AMOUNT(false),
	MATERIAL_DATA(false),
	ENCHANTMENT(true),
	ITEM_FLAG(true),
	CUSTOM_META(true);

	private boolean cumulative;

	Properties(boolean cumulative) {
	    this.cumulative = cumulative;
	}

	public boolean isCumulative() {
	    return cumulative;
	}
    }
}
