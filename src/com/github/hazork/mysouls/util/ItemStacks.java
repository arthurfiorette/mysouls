package com.github.hazork.mysouls.util;

import java.util.Arrays;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class ItemStacks {

    public static ItemStack getHead(String player) {
	ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	SkullMeta sm = (SkullMeta) skull.getItemMeta();
	sm.setOwner(player);
	skull.setItemMeta(sm);
	return skull;
    }

    public static ItemStack createNBT(ItemStack item, Consumer<NBTTagCompound> nbt) {
	net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(item);
	nbt.accept(i.hasTag() ? i.getTag() : new NBTTagCompound());
	i.setTag(i.getTag());
	return CraftItemStack.asBukkitCopy(i);
    }

    public static NBTTagCompound getNBT(ItemStack item) {
	return CraftItemStack.asNMSCopy(item).getTag();
    }

    public static ItemStack create(Material material, int amount, String name, String... lores) {
	return set(create(material, amount), name, lores);
    }

    public static ItemStack setAmount(ItemStack item, int amount) {
	item.setAmount(amount);
	return item;
    }

    public static ItemStack create(Material material, int amount) {
	return new ItemStack(material, amount);
    }

    public static ItemStack set(ItemStack item, String name, String... lores) {
	return setLore(setName(item, name), lores);
    }

    public static ItemStack removeFlags(ItemStack item) {
	return setItemMeta(item, meta -> meta.addItemFlags(ItemFlag.values()));
    }

    public static ItemStack setName(ItemStack item, String name) {
	return setItemMeta(item, meta -> meta.setDisplayName(name));
    }

    public static ItemStack setLore(ItemStack item, String... lores) {
	return setItemMeta(item, meta -> meta.setLore(Arrays.asList(lores)));
    }

    public static ItemStack setItemMeta(ItemStack item, Consumer<ItemMeta> callback) {
	ItemMeta meta = item.getItemMeta();
	callback.accept(meta);
	item.setItemMeta(meta);
	return item;
    }

}
