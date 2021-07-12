package com.github._hazork.oldmysouls.utils;

import com.github._hazork.oldmysouls.MySouls;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

public class ItemBuilder {

  private transient ItemStack lastBuild;
  private transient boolean modified = false;

  private final Material material;
  private EnumMap<Properties, Consumer<ItemStack>> properties = new EnumMap<>(Properties.class);

  public static ItemBuilder ofHead(final OfflinePlayer player) {
    return ItemBuilder.ofHead(player, false);
  }

  public static ItemBuilder ofHead(final OfflinePlayer player, final boolean allItemFlags) {
    return ItemBuilder.ofSkullGameProfile(
      new GameProfile(player.getUniqueId(), player.getName()),
      allItemFlags
    );
  }

  public static ItemBuilder ofHead(final String playername) {
    return ItemBuilder.ofHead(playername, false);
  }

  public static ItemBuilder ofHead(final String playername, final boolean allItemFlags) {
    final ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, true).setDurability(3);
    return builder.addCustomMeta(
      im -> {
        final SkullMeta sm = (SkullMeta) im;
        sm.setOwner(playername);
        return sm;
      }
    );
  }

  public static ItemBuilder ofHeadUrl(final String url) {
    return ItemBuilder.ofHeadUrl(url, false);
  }

  public static ItemBuilder ofHeadUrl(final String url, final boolean allItemFlags) {
    final GameProfile profile = new GameProfile(UUID.randomUUID(), null);
    final byte[] encodedData = Base64.encodeBase64(
      String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes()
    );
    profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
    return ItemBuilder.ofSkullGameProfile(profile, true);
  }

  public static ItemBuilder ofSkullGameProfile(final GameProfile gp) {
    return ItemBuilder.ofSkullGameProfile(gp, false);
  }

  public static ItemBuilder ofSkullGameProfile(final GameProfile gp, final boolean allItemFlags) {
    final ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, allItemFlags).setDurability(3);
    builder.addCustomMeta(
      meta -> {
        try {
          final SkullMeta headMeta = (SkullMeta) meta;
          final Field profileField = headMeta.getClass().getDeclaredField("profile");
          profileField.setAccessible(true);
          profileField.set(headMeta, gp);
          return headMeta;
        } catch (IllegalAccessException | NoSuchFieldException exc) {
          MySouls.treatException(
            ItemBuilder.class,
            "Error when trying to load custom head via GameProfile",
            exc
          );
          return meta;
        }
      }
    );
    return builder;
  }

  public ItemBuilder(final Material material, final boolean removeAllItemFlags) {
    this(material);
    if (removeAllItemFlags) {
      this.setItemFlags();
    }
  }

  public ItemBuilder(final Material material) {
    this.material = (material == null ? Material.AIR : material);
  }

  public ItemBuilder setDurability(final int durability) {
    return this.addProperties(Properties.DAMAGE, is -> is.setDurability((short) durability));
  }

  public ItemBuilder setAmount(final int amount) {
    return this.addProperties(Properties.AMOUNT, is -> is.setAmount(amount));
  }

  public ItemBuilder setData(final MaterialData data) {
    return this.addProperties(Properties.MATERIAL_DATA, is -> is.setData(data));
  }

  public ItemBuilder addEnchantment(final Enchantment ench, final int level) {
    return this.addProperties(Properties.ENCHANTMENT, is -> is.addUnsafeEnchantment(ench, level));
  }

  public ItemBuilder addEnchantments(final Map<Enchantment, Integer> enchantments) {
    return this.addProperties(Properties.ENCHANTMENT, is -> is.addUnsafeEnchantments(enchantments));
  }

  public ItemBuilder setName(final String name) {
    return this.addProperties(
        Properties.NAME,
        is -> ItemBuilder.setItemMeta(is, im -> im.setDisplayName(name))
      );
  }

  public ItemBuilder setItemFlags() {
    return this.setItemFlags(ItemFlag.values());
  }

  public ItemBuilder setItemFlags(final ItemFlag... itemFlags) {
    return this.addProperties(
        Properties.ITEM_FLAG,
        is ->
          ItemBuilder.setItemMeta(
            is,
            im -> {
              im.removeItemFlags(ItemFlag.values());
              im.addItemFlags(itemFlags);
            }
          )
      );
  }

  public ItemBuilder addItemFlags(final ItemFlag... itemFlags) {
    return this.addProperties(
        Properties.ITEM_FLAG,
        is -> ItemBuilder.setItemMeta(is, im -> im.addItemFlags(itemFlags))
      );
  }

  public ItemBuilder setLores(final String... lorelines) {
    return this.setLore(Arrays.asList(lorelines));
  }

  public ItemBuilder setLore(final List<String> lore) {
    return this.addProperties(
        Properties.LORE,
        is -> ItemBuilder.setItemMeta(is, im -> im.setLore(lore))
      );
  }

  public ItemBuilder addLores(final String... lorelines) {
    return this.addLore(Arrays.asList(lorelines));
  }

  public ItemBuilder addLore(final List<String> lore) {
    if (this.properties.containsKey(Properties.LORE)) {
      return this.addProperties(
          Properties.LORE,
          is -> ItemBuilder.setItemMeta(is, im -> im.getLore().addAll(lore))
        );
    }
    return this.setLore(lore);
  }

  public ItemBuilder addCustomMeta(final UnaryOperator<ItemMeta> customMeta) {
    return this.addProperties(
        Properties.CUSTOM_META,
        is -> is.setItemMeta(customMeta.apply(is.getItemMeta()))
      );
  }

  public ItemStack build() {
    if (!this.modified) {
      return this.getLastBuild();
    }
    final ItemStack item = new ItemStack(this.material);
    this.properties.values().stream().forEach(c -> c.accept(item));
    return this.lastBuild = item;
  }

  public ItemStack getLastBuild() {
    return this.lastBuild;
  }

  public ItemMeta.Spigot spigot() {
    return this.build().getItemMeta().spigot();
  }

  @Override
  public ItemBuilder clone() {
    final ItemBuilder clone = new ItemBuilder(this.material);
    clone.properties = this.properties;
    return clone;
  }

  public ItemBuilder remove(final Properties property) {
    this.properties.remove(property);
    return this;
  }

  private ItemBuilder addProperties(final Properties type, final Consumer<ItemStack> consumer) {
    if (this.properties.containsKey(type) && type.isCumulative()) {
      this.properties.compute(type, (k, v) -> v.andThen(consumer));
    } else {
      this.properties.put(type, consumer);
    }
    this.modified = true;
    return this;
  }

  private static void setItemMeta(final ItemStack item, final Consumer<ItemMeta> callback) {
    final ItemMeta meta = item.getItemMeta();
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

    Properties(final boolean cumulative) {
      this.cumulative = cumulative;
    }

    public boolean isCumulative() {
      return this.cumulative;
    }
  }
}
