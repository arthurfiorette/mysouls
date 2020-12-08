package com.github.hazork.mysouls.guis.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.commands.commands.InfoCommand;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.guis.Gui;
import com.github.hazork.mysouls.guis.GuiListener;
import com.github.hazork.mysouls.utils.ItemBuilder;
import com.github.hazork.mysouls.utils.ItemBuilder.Properties;
import com.github.hazork.mysouls.utils.Utils.Spigots;
import com.google.common.collect.Lists;

public class GeneralGui extends Gui {

    public static final String NAME = "General";

    private static UnaryOperator<Integer> slotFunc = i -> (i > 16 && i < 21) ? 21
	    : (i > 25 && i < 30) ? 30 : (i > 34 && i < 39) ? 39 : i;

    private static ItemBuilder emptyBuilder = new ItemBuilder(Material.THIN_GLASS, true).setName("")
	    .remove(Properties.LORE);
    private static ItemBuilder infoBuilder = ItemBuilder.ofHead("MHF_Question", true).setName(Lang.CREDITS.getText())
	    .setLores(InfoCommand.INFO);
    private static ItemBuilder trophyBuilder = ItemBuilder.ofHeadUrl(
	    "http://textures.minecraft.net/texture/e34a592a79397a8df3997c43091694fc2fb76c883a76cce89f0227e5c9f1dfe",
	    true).setName(Lang.RANKING_NAME.getText()).setLores("§cNot ready yet..");

    List<List<UUID>> soulsList = new ArrayList<>();

    private int page;

    public GeneralGui(UUID ownerId) {
	super(ownerId, 6, "§lMy Souls");
	load();
    }

    @Override
    protected String getName() {
	return NAME;
    }

    @Override
    protected Inventory createInventory(int lines, String title) {
	Inventory inv = super.createInventory(lines, title);
	inv.setItem(45, infoBuilder.build());
	inv.setItem(19, trophyBuilder.build()); // TODO Trophy information.
	return inv;
    }

    @Override
    protected void load() {
	setDefault();
	soulsList = Lists.partition(new ArrayList<>(getWallet().getSoulsSet()), 20);
	if (page >= soulsList.size()) page = 0;
	if (!soulsList.isEmpty()) setInventory(soulsList.get(page));
    }

    private void setDefault() {
	for (int i = 12; i <= 43; i++) {
	    i = slotFunc.apply(i);
	    if (!emptyBuilder.build().equals(inventory.getItem(i))) {
		inventory.setItem(i, emptyBuilder.build());
	    }
	}

	String media = String.format("%.2f",
		((double) getWallet().getSoulsCount() / getWallet().getDifferentSoulsCount()));
	Map<String, String> placeholders = new HashMap<>();

	placeholders.put("{souls}", getWallet().getSoulsCount() + "");
	placeholders.put("{players}", getWallet().getDifferentSoulsCount() + "");
	placeholders.put("{average}", (media.equalsIgnoreCase("NaN") ? "§7?" : media));
	placeholders.put("{more-souls}",
		(getWallet().getMostKilledPlayer() == null ? "§7?" : getWallet().getMostKilledPlayer().getName()));

	ItemBuilder.ofHead(getOfflinePlayer(), true).setName(Lang.YOUR_WALLET_NAME.getText())
		.setLore(Lang.YOUR_WALLET_LORE.getList(placeholders)).setOnInventory(inventory, 10);

	if (hasPreviousPage()) {
	    if (inventory.getItem(26) == null) new ItemBuilder(Material.STONE_BUTTON, true)
		    .setName(Lang.BACKWARD.getText()).setOnInventory(inventory, 26);
	} else if (inventory.getItem(26) != null) inventory.clear(26);

	if (hasNextPage()) {
	    if (inventory.getItem(35) == null) new ItemBuilder(Material.STONE_BUTTON, true)
		    .setName(Lang.FORWARD.getText()).setOnInventory(inventory, 35);
	} else if (inventory.getItem(35) != null) inventory.clear(35);

	new ItemBuilder(Material.SKULL_ITEM, true).setDurability(1).setName(Lang.WITHDRAW_SOULS_NAME.getText())
		.setLore(Lang.WITHDRAW_COINS_LORE.getList()).setOnInventory(inventory, 49);

	ItemBuilder.ofHeadUrl(
		"http://textures.minecraft.net/texture/77b9dfd281deaef2628ad5840d45bcda436d6626847587f3ac76498a51c861",
		true).setName(Lang.WITHDRAW_COINS_NAME.getText()).setLore(Lang.WITHDRAW_COINS_LORE.getList())
		.setOnInventory(inventory, 51);

	new ItemBuilder(Material.PAPER, true).setName(Lang.PAGE.getText()).setAmount(page + 1).setOnInventory(inventory,
		53);
    }

    private void setInventory(List<UUID> list) {
	int i = 12;
	for (UUID soul : list) {
	    i = slotFunc.apply(i);
	    inventory.setItem(i, soulToItem(soul, getWallet().getSoulsCount(soul)));
	    i++;
	}
    }

    private ItemStack soulToItem(UUID soul, int amount) {
	OfflinePlayer player = Bukkit.getOfflinePlayer(soul);
	return ItemBuilder.ofHead(player, true).setAmount(amount)
		.setName(Lang.INVENTORY_SOUL_NAME.getText("{player}", player.getName()))
		.addLore(Lang.INVENTORY_SOUL_LORE.getList()).build();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
	super.onClick(event);

	switch (event.getSlot()) {

	    case 26: // previous
		previousPage();
		break;

	    case 35: // next
		nextPage();
		break;

	    case 49: // souls
		getPlayer().closeInventory();
		getPlayer().sendMessage(Lang.SOUL_CHAT_MESSAGE.getText());
		GuiListener.setChatAction(getOwnerId(), msg -> {
		    String[] args = msg.split(" ");
		    if (Spigots.hasEmptySlot(getPlayer())) {
			if (getWallet().canRemoveSoul()) {
			    if (args[0].equalsIgnoreCase("*")) {
				ItemStack is = getWallet().withdrawSoul();
				getPlayer().getInventory().addItem(is);
				getPlayer().sendMessage(Lang.SOUL_REMOVED.getText());
			    } else {
				@SuppressWarnings("deprecation")
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				UUID uuid = player.getUniqueId();
				if (player != null && getWallet().canRemoveSoul(uuid)) {
				    ItemStack is = getWallet().withdrawSoul(uuid);
				    getPlayer().getInventory().addItem(is);
				    getPlayer().sendMessage(Lang.SOUL_REMOVED.getText());
				} else {
				    getPlayer().sendMessage(Lang.DONT_HAVE_SOUL.getText());
				}
			    }
			} else {
			    getPlayer().sendMessage(Lang.DONT_HAVE_SOULS.getText());
			}
		    } else {
			getPlayer().sendMessage(Lang.INVENTORY_FULL.getText());
		    }
		});
		break;

	    case 51: // coins
		getPlayer().closeInventory();
		getPlayer().sendMessage(Lang.COIN_CHAT_MESSAGE.getText());
		GuiListener.setChatAction(getOwnerId(), msg -> {
		    String[] args = msg.split(" ");
		    try {
			int amount = Integer.parseInt(args[0]);
			if (Spigots.hasEmptySlot(getPlayer())) {
			    if (getWallet().canRemoveSouls(amount)) {
				ItemStack is = getWallet().withdrawCoins(amount);
				getPlayer().getInventory().addItem(is);
				getPlayer().sendMessage(Lang.COINS_REMOVED.getText());
			    } else {
				getPlayer().sendMessage(Lang.DONT_HAVE_SOULS.getText());
			    }
			} else {
			    getPlayer().sendMessage(Lang.INVENTORY_FULL.getText());
			}
		    } catch (NumberFormatException e) {
			getPlayer().sendMessage(Lang.NOT_A_NUMBER.getText("{text}", args[0]));
		    }
		});
		break;
	}
    }

    private boolean hasNextPage() {
	return (page + 1) < soulsList.size();
    }

    private void nextPage() {
	if (hasNextPage()) {
	    page++;
	    load();
	}
    }

    private boolean hasPreviousPage() {
	return page > 0;
    }

    private void previousPage() {
	if (hasPreviousPage()) {
	    page--;
	    load();
	}
    }

}
