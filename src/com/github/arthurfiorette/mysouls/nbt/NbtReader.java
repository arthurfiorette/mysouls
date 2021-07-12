package com.github.arthurfiorette.mysouls.nbt;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

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
