package com.github.hazork.mysouls.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.github.hazork.mysouls.MySouls;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

/**
 * Useful Java Methods
 * 
 * @author github.com/Hazork
 *
 */
public class Utils {

    public static <E> E poll(List<E> list) {
	int index = getRandom(list.size());
	E element = list.get(index);
	list.remove(index);
	return element;
    }

    public static int getRandom(int range) {
	return new Random().nextInt(range);
    }

    public static UUID[] multiply(UUID source, int mult) {
	List<UUID> list = Arrays.asList(new UUID[mult]);
	Collections.fill(list, source);
	return list.toArray(new UUID[mult]);
    }

    public static void sendMessageFormat(Player player, String message) {
	player.sendMessage(String.format("§5[MySouls] §7%s", message));
    }

    public static void sendMessageFormat(Player player, String message, Object... args) {
	sendMessageFormat(player, String.format(message, args));
    }

    /**
     * Useful Spigot Methods
     * 
     * @author github.com/Hazork
     *
     */
    public static class Spigots {

	public static boolean hasPlugin(String name) {
	    return (Bukkit.getPluginManager().getPlugin(name) != null);
	}

	public static boolean hasEmptySlot(Player player) {
	    return player.getInventory().firstEmpty() != -1;
	}

	public static boolean hasEmptySpace(Player player, int amount) {
	    if (!hasEmptySlot(player)) return false;
	    return ((getEmptySlots(player) * 64) >= amount);
	}

	public static int getEmptySlots(Player player) {
	    int space = 0;
	    for (ItemStack is : player.getInventory().getContents())
		if (is == null || is.getType() == Material.AIR) space += 64;
	    return space;
	}

	public static boolean isSelfKill(EntityDeathEvent event) {
	    return event.getEntity().equals(event.getEntity().getKiller());
	}

	public static void callEvent(Event event) {
	    Bukkit.getPluginManager().callEvent(event);
	}

	public static boolean isPlayer(Object... objs) {
	    for (Object obj : objs) if (!isPlayer(obj)) return false;
	    return true;
	}

	public static boolean isPlayer(Object obj) {
	    return obj instanceof Player;
	}
    }

    /**
     * Useful ItemStack Methods
     * 
     * @author github.com/Hazork
     *
     */
    public static class ItemStacks {

	public static ItemStack getHead(String player) {
	    ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	    SkullMeta sm = (SkullMeta) skull.getItemMeta();
	    sm.setOwner(player);
	    skull.setItemMeta(sm);
	    return skull;
	}

	public static ItemStack getHeadFromUrl(String url) {
	    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
	    if (url.isEmpty()) return head;
	    SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
	    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
	    Field profileField = null;
	    try {
		profileField = headMeta.getClass().getDeclaredField("profile");
		profileField.setAccessible(true);
		profileField.set(headMeta, profile);
	    } catch (Exception exc) {
		MySouls.treatException(ItemStacks.class,
			"Erro ao tentar carregar cabeça customizada. Link: (" + url + ")", exc);
	    }
	    head.setItemMeta(headMeta);
	    return head;
	}

	public static ItemStack createNBT(ItemStack template, Consumer<NBTTagCompound> nbt) {
	    net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(template);
	    nbt.accept(i.hasTag() ? i.getTag() : new NBTTagCompound());
	    i.setTag(i.getTag());
	    return CraftItemStack.asBukkitCopy(i);
	}

	/**
	 * Set an nbt tag in String in the plugin pattern.
	 * 
	 * @param template the ItemStack to create a copy with nbt.
	 * @param value    the value to set int nbt
	 * @return The ItemStack with nbt placed (different from the parameter)
	 */
	public static ItemStack createNBT(ItemStack template, String value) {
	    return createNBT(template, nbt -> {
		nbt.setBoolean(MySouls.NAME, true);
		nbt.setString(MySouls.NAME + ".value", value);
	    });
	}

	public static Optional<String> getNBTPattern(ItemStack item) {
	    NBTTagCompound nbt = getNBT(item);
	    if (nbt.getBoolean(MySouls.NAME)) return Optional.ofNullable(nbt.getString(MySouls.NAME + ".value"));
	    else return Optional.empty();
	}

	public static NBTTagCompound getNBT(ItemStack item) {
	    net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
	    return nms.hasTag() ? nms.getTag() : new NBTTagCompound();
	}

	public static ItemStack setAmount(ItemStack item, int amount) {
	    item.setAmount(amount);
	    return item;
	}

	public static ItemStack create(Material material, int amount) {
	    return new ItemStack(material, amount);
	}

	public static ItemStack set(boolean removeFlags, ItemStack item, String name, String... lores) {
	    if (removeFlags) removeFlags(item);
	    setName(item, name);
	    setLore(item, lores);
	    return item;
	}

	public static void removeFlags(ItemStack item) {
	    setItemMeta(item, meta -> meta.addItemFlags(ItemFlag.values()));
	}

	public static void setName(ItemStack item, String name) {
	    setItemMeta(item, meta -> meta.setDisplayName(name));
	}

	public static void setLore(ItemStack item, String... lores) {
	    if (lores == null) return;
	    setItemMeta(item, meta -> meta.setLore(Arrays.asList(lores)));
	}

	public static void setItemMeta(ItemStack item, Consumer<ItemMeta> callback) {
	    ItemMeta meta = item.getItemMeta();
	    callback.accept(meta);
	    item.setItemMeta(meta);
	}
    }

}
