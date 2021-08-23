package com.github.arthurfiorette.mysouls.nbt;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class NbtReader {

  public boolean getBoolean(final ItemStack is, final NbtKey key) {
    final NBTCompound compound = NBTItem.convertItemtoNBT(is);
    return compound.getBoolean(key.getName());
  }

  public String getString(final ItemStack is, final NbtKey key) {
    final NBTCompound compound = NBTItem.convertItemtoNBT(is);
    return compound.getString(key.getName());
  }
}
