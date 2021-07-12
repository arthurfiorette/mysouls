package com.github._hazork.oldmysouls.guis.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.function.UnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github._hazork.oldmysouls.commands.commands.InfoCommand;
import com.github._hazork.oldmysouls.data.config.Config;
import com.github._hazork.oldmysouls.data.lang.Lang;
import com.github._hazork.oldmysouls.guis.Gui;
import com.github._hazork.oldmysouls.guis.GuiListener;
import com.github._hazork.oldmysouls.souls.SoulComunicator;
import com.github._hazork.oldmysouls.utils.ItemBuilder;
import com.github._hazork.oldmysouls.utils.ItemBuilder.Properties;
import com.github._hazork.oldmysouls.utils.Utils;
import com.google.common.collect.Lists;

public class GeneralGui extends Gui {

  public static final String NAME = "General";

  private static UnaryOperator<Integer> slotFunc = i -> ((i > 16) && (i < 21)) ? 21
      : ((i > 25) && (i < 30)) ? 30 : ((i > 34) && (i < 39)) ? 39 : i;

  private static ItemBuilder emptyBuilder = new ItemBuilder(Material.GLASS_PANE, true).setName(" ")
      .remove(Properties.LORE);
  private static ItemBuilder infoBuilder = ItemBuilder.ofHead("MHF_Question", true)
      .setName(Lang.CREDITS.getText()).setLore(InfoCommand.infoList);
  private static ItemBuilder trophyBuilder = ItemBuilder
      .ofHeadUrl(Config.TROPHY_HEAD_URL.getText(), true).setName(Lang.RANKING_NAME.getText())
      .setLores("§cNot ready yet..");

  List<List<UUID>> soulsList = new ArrayList<>();

  private int page;

  public GeneralGui(final UUID ownerId) {
    super(ownerId, 6, "§lMy Souls");
    this.load();
  }

  @Override
  protected String getName() {
    return GeneralGui.NAME;
  }

  @Override
  protected Inventory createInventory(final int lines, final String title) {
    final Inventory inv = super.createInventory(lines, title);
    inv.setItem(45, GeneralGui.infoBuilder.build());
    inv.setItem(19, GeneralGui.trophyBuilder.build());
    return inv;
  }

  @Override
  protected void load() {
    this.drawItens();
    this.soulsList = Lists.partition(new ArrayList<>(this.getWallet().getSouls()), 20);
    if (this.page >= this.soulsList.size()) {
      this.page = 0;
    }
    if (!this.soulsList.isEmpty()) {
      this.drawPageableItems(this.soulsList.get(this.page));
    }
  }

  private void drawItens() {
    for(int i = 12; i <= 43; i = GeneralGui.slotFunc.apply(i + 1)) {
      if (!GeneralGui.emptyBuilder.build().equals(this.inventory.getItem(i))) {
        this.inventory.setItem(i, GeneralGui.emptyBuilder.build());
      }
    }

    final double ratio = this.getWallet().soulsRatio();
    final Map<String, String> walletHolders = new HashMap<>();
    walletHolders.put("{souls}", this.getWallet().soulsCount() + "");
    walletHolders.put("{players}", this.getWallet().playerCount() + "");
    walletHolders.put("{average}", String.format("%.2f", Double.isNaN(ratio) ? 0 : ratio));
    final Entry<UUID, Integer> entry = this.getWallet().biggestEntry();
    if (entry != null) {
      walletHolders.put("{more-souls}", Bukkit.getOfflinePlayer(entry.getKey()).getName());
    }

    final int walletSlot = 10;
    final ItemBuilder walletBuilder = ItemBuilder.ofHead(this.getOfflinePlayer(), true);
    walletBuilder.setName(Lang.YOUR_WALLET_NAME.getText());
    walletBuilder.setLore(Lang.YOUR_WALLET_LORE.getList(walletHolders));
    this.inventory.setItem(walletSlot, walletBuilder.build());

    final int ppSlot = 26;
    if (this.hasPreviousPage() && (this.inventory.getItem(ppSlot) == null)) {
      final ItemBuilder ppBuilder = new ItemBuilder(Material.STONE_BUTTON, true);
      ppBuilder.setName(Lang.BACKWARD.getText());
      this.inventory.setItem(ppSlot, ppBuilder.build());
    } else {
      this.inventory.clear(ppSlot);
    }

    final int npSlot = 35;
    if (this.hasNextPage() && (this.inventory.getItem(ppSlot) == null)) {
      final ItemBuilder npBuilder = new ItemBuilder(Material.STONE_BUTTON, true);
      npBuilder.setName(Lang.FORWARD.getText());
      this.inventory.setItem(npSlot, npBuilder.build());
    } else {
      this.inventory.clear(npSlot);
    }

    final int wsSlot = 49;
    final ItemBuilder wsBuilder = ItemBuilder.ofHeadUrl(Config.SOUL_HEAD_URL.getText(), true);
    wsBuilder.setName(Lang.WITHDRAW_SOULS_NAME.getText());
    wsBuilder.setLore(Lang.WITHDRAW_SOULS_LORE.getList());
    this.inventory.setItem(wsSlot, wsBuilder.build());

    final int wcSlot = 51;
    final ItemBuilder wcBuilder = ItemBuilder.ofHeadUrl(Config.COIN_HEAD_URL.getText(), true);
    wcBuilder.setName(Lang.WITHDRAW_COINS_NAME.getText());
    wcBuilder.setLore(Lang.WITHDRAW_COINS_LORE.getList());
    this.inventory.setItem(wcSlot, wcBuilder.build());

    final int pageSlot = 53;
    if (this.page > 0) {
      final ItemBuilder pageBuilder = new ItemBuilder(Material.PAPER, true);
      pageBuilder.setName(Lang.PAGE.getText());
      pageBuilder.setAmount(this.page + 1);
      this.inventory.setItem(pageSlot, pageBuilder.build());
    } else {
      this.inventory.clear(pageSlot);
    }
  }

  private void drawPageableItems(final List<UUID> list) {
    int i = 12;
    for(final UUID soul: list) {
      i = GeneralGui.slotFunc.apply(i);
      this.inventory.setItem(i, this.soulToItem(soul, this.getWallet().soulsCount(soul)));
      i++;
    }
  }

  private ItemStack soulToItem(final UUID soul, final int amount) {
    final OfflinePlayer player = Bukkit.getOfflinePlayer(soul);
    return ItemBuilder.ofHead(player, true).setAmount(amount)
        .setName(Lang.INVENTORY_SOUL_NAME.getText("{player}", player.getName()))
        .addLore(Lang.INVENTORY_SOUL_LORE.getList()).build();
  }

  @Override
  public void onClick(final InventoryClickEvent event) {
    super.onClick(event);

    switch (event.getSlot()) {
      case 26: // previous
        this.previousPage();
        break;

      case 35: // next
        this.nextPage();
        break;

      case 49: // souls
        Utils.closeInventory(this.getPlayer());
        this.getPlayer().sendMessage(Lang.SOUL_CHAT_MESSAGE.getText());
        GuiListener.setChatAction(this.getOwnerId(), msg -> {
          final String[] args = msg.split(" ");
          UUID soul = null;
          if (!args[0].equalsIgnoreCase("*")) {
            @SuppressWarnings("deprecation")
            final OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            if (player != null) {
              soul = player.getUniqueId();
            } else {
              this.getPlayer().sendMessage(Lang.DONT_HAVE_SOULS.getText());
              return;
            }
          }
          SoulComunicator.of(this.getPlayer()).withdrawSoul(soul);
        });
        break;

      case 51: // coins
        Utils.closeInventory(this.getPlayer());
        this.getPlayer().sendMessage(Lang.COIN_CHAT_MESSAGE.getText());
        GuiListener.setChatAction(this.getOwnerId(), msg -> {
          final String[] args = msg.split(" ");
          try {
            final int amount = Integer.parseInt(args[0]);
            SoulComunicator.of(this.getPlayer()).withdrawCoins(amount);
          } catch (final NumberFormatException e) {
            this.getPlayer().sendMessage(Lang.NOT_A_NUMBER.getText("{text}", args[0]));
          }
        });
        break;
    }
  }

  private boolean hasNextPage() {
    return (this.page + 1) < this.soulsList.size();
  }

  private void nextPage() {
    if (this.hasNextPage()) {
      this.page++;
      this.load();
    }
  }

  private boolean hasPreviousPage() {
    return this.page > 0;
  }

  private void previousPage() {
    if (this.hasPreviousPage()) {
      this.page--;
      this.load();
    }
  }
}
