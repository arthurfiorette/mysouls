package com.github._hazork.mysouls.utils;

import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

public class Nbts {

  private static final String name = "MySoulsIdV1";
  private static final String id = "id";
  private static final String value = "value";

  public static ItemStack saveValue(final ItemStack source, final String id, final String value) {
    final Map<String, String> map = Maps.newHashMap();
    map.put(Nbts.id, id);
    if (value != null) {
      map.put(Nbts.value, value);
    }
    return Nbts.saveValues(source, map);
  }

  public static String getIdValue(final ItemStack item) {
    return Nbts.getValue(item, Nbts.id);
  }

  public static String getValue(final ItemStack item) {
    return Nbts.getValue(item, Nbts.value);
  }

  private static String getValue(final ItemStack item, final String keyName) {
    final NBTTagCompound nbt = Nbts.getNbt(item);
    if (Nbts.isNbtsItem(item)) {
      return nbt.getString(Nbts.getKey(keyName));
    }
    return null;
  }

  public static boolean isNbtsItem(final ItemStack item) {
    return Nbts.getNbt(item).getBoolean(Nbts.name);
  }

  private static ItemStack saveValues(final ItemStack source, final Map<String, String> values) {
    return Nbts.setNbt(source, nbt -> {
      nbt.setBoolean(Nbts.name, true);
      values.forEach((k, v) -> nbt.setString(Nbts.getKey(k), v));
    });
  }

  private static ItemStack setNbt(final ItemStack source, final Consumer<NBTTagCompound> nbt) {
    final net.minecraft.server.v1_8_R3.ItemStack i = CraftItemStack.asNMSCopy(source);
    nbt.accept(i.hasTag() ? i.getTag() : new NBTTagCompound());
    i.setTag(i.getTag());
    return CraftItemStack.asBukkitCopy(i);
  }

  private static NBTTagCompound getNbt(final ItemStack item) {
    final net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(item);
    return nms.hasTag() ? nms.getTag() : new NBTTagCompound();
  }

  private static String getKey(final String keyName) {
    return keyName.startsWith(Nbts.name + ".") ? keyName : Nbts.name + "." + keyName;
  }
}
