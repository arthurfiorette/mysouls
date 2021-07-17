package com.github.arthurfiorette.mysouls.nbt;

import org.bukkit.inventory.ItemStack;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;

public class NbtReader {

  public static boolean getBoolean(final ItemStack is, final NbtKey key) {
    final NBTCompound compound = NBTItem.convertItemtoNBT(is);
    return compound.getBoolean(key.getName());
  }

  public static String getString(final ItemStack is, final NbtKey key) {
    final NBTCompound compound = NBTItem.convertItemtoNBT(is);
    return compound.getString(key.getName());
  }
}
