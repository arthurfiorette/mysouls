package com.github.hazork.mysouls.guis.implementations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.commands.commands.InfoCommand;
import com.github.hazork.mysouls.data.config.Config;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.guis.Gui;
import com.github.hazork.mysouls.guis.GuiListener;
import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.utils.ItemBuilder;
import com.github.hazork.mysouls.utils.ItemBuilder.Properties;
import com.github.hazork.mysouls.utils.Utils;
import com.github.hazork.mysouls.utils.Utils.Spigots;
import com.google.common.collect.Lists;

public class GeneralGui extends Gui {

    public static final String NAME = "General";

    private static UnaryOperator<Integer> slotFunc = i -> (i > 16 && i < 21) ? 21
	    : (i > 25 && i < 30) ? 30 : (i > 34 && i < 39) ? 39 : i;

    private static ItemBuilder emptyBuilder = new ItemBuilder(Material.THIN_GLASS, true).setName(" ")
	    .remove(Properties.LORE);
    private static ItemBuilder infoBuilder = ItemBuilder.ofHead("MHF_Question", true).setName(Lang.CREDITS.getText())
	    .setLores(InfoCommand.INFO);
    private static ItemBuilder trophyBuilder = ItemBuilder.ofHeadUrl(Config.TROPHY_HEAD_URL.getText(), true)
	    .setName(Lang.RANKING_NAME.getText()).setLores("§cNot ready yet..");

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
	inv.setItem(19, trophyBuilder.build());
	return inv;
    }

    @Override
    protected void load() {
	drawItens();
	soulsList = Lists.partition(new ArrayList<>(getWallet().getSouls()), 20);
	if (page >= soulsList.size()) page = 0;
	if (!soulsList.isEmpty()) drawPageableItems(soulsList.get(page));
    }

    private void drawItens() {
	HashMap<Integer, ItemStack> items = new HashMap<>();
	for (int i = 12; i <= 43; i = slotFunc.apply(i + 1)) {
	    if (!emptyBuilder.build().equals(inventory.getItem(i))) {
		items.put(i, emptyBuilder.build());
	    }
	}
	double ratio = getWallet().soulsRatio();
	Map<String, String> walletHolders = new HashMap<>();
	walletHolders.put("{souls}", getWallet().soulsCount() + "");
	walletHolders.put("{players}", getWallet().playerCount() + "");
	walletHolders.put("{average}", String.format("%.2f", Double.isNaN(ratio) ? 0 : ratio));
	walletHolders.put("{more-souls}", getWallet().entrySet(set -> {
	    if (set.isEmpty()) return "§7?";
	    else {
		UUID uuid = Collections.max(set, Map.Entry.comparingByValue()).getKey();
		return Bukkit.getOfflinePlayer(uuid).getName();
	    }
	}));

	int walletSlot = 10;
	ItemBuilder walletBuilder = ItemBuilder.ofHead(getOfflinePlayer(), true)
		.setName(Lang.YOUR_WALLET_NAME.getText()).setLore(Lang.YOUR_WALLET_LORE.getList(walletHolders));
	items.put(walletSlot, walletBuilder.build());

	int ppSlot = 26;
	if (!hasPreviousPage()) inventory.clear(ppSlot);
	else {
	    if (inventory.getItem(ppSlot) == null) {
		ItemBuilder ppBuilder = new ItemBuilder(Material.STONE_BUTTON, true).setName(Lang.BACKWARD.getText());
		items.put(ppSlot, ppBuilder.build());
	    }
	}

	int npSlot = 35;
	if (!hasNextPage()) inventory.clear(npSlot);
	else {
	    if (inventory.getItem(npSlot) == null) {
		ItemBuilder npBuilder = new ItemBuilder(Material.STONE_BUTTON, true).setName(Lang.FORWARD.getText());
		items.put(npSlot, npBuilder.build());
	    }
	}

	int wsSlot = 49;
	ItemBuilder wsBuilder = ItemBuilder.ofHeadUrl(Config.SOUL_HEAD_URL.getText(), true)
		.setName(Lang.WITHDRAW_SOULS_NAME.getText()).setLore(Lang.WITHDRAW_SOULS_LORE.getList());
	items.put(wsSlot, wsBuilder.build());

	int wcSlot = 51;
	ItemBuilder wcBuilder = ItemBuilder.ofHeadUrl(Config.COIN_HEAD_URL.getText(), true)
		.setName(Lang.WITHDRAW_COINS_NAME.getText()).setLore(Lang.WITHDRAW_COINS_LORE.getList());
	items.put(wcSlot, wcBuilder.build());

	int pageSlot = 53;
	ItemBuilder pageBuilder = new ItemBuilder(Material.PAPER, true).setName(Lang.PAGE.getText())
		.setAmount(page + 1);
	items.put(pageSlot, pageBuilder.build());

	items.forEach((k, v) -> inventory.setItem(k, v));
    }

    private void drawPageableItems(List<UUID> list) {
	int i = 12;
	for (UUID soul : list) {
	    i = slotFunc.apply(i);
	    inventory.setItem(i, soulToItem(soul, getWallet().soulsCount(soul)));
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
		    Lang message = null;
		    if (!Spigots.hasEmptySlot(getPlayer())) message = Lang.INVENTORY_FULL;
		    else {
			String[] args = msg.split(" ");
			if (!getWallet().canRemoveSoul(SoulWallet.ANY, 1)) message = Lang.DONT_HAVE_SOULS;
			else {
			    if (args[0].equalsIgnoreCase("*")) {
				ItemStack is = getWallet().withdrawSoul(SoulWallet.ANY);
				getPlayer().getInventory().addItem(is);
				message = Lang.SOUL_REMOVED;
				Utils.playSound(getPlayer(), Sound.ORB_PICKUP);
			    } else {
				@SuppressWarnings("deprecation")
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				if (player == null || !getWallet().canRemoveSoul(player.getUniqueId(), 1))
				    message = Lang.DONT_HAVE_SOUL;
				else {
				    ItemStack is = getWallet().withdrawSoul(player.getUniqueId());
				    getPlayer().getInventory().addItem(is);
				    message = Lang.SOUL_REMOVED;
				    Utils.playSound(getPlayer(), Sound.ORB_PICKUP);
				}
			    }
			}
		    }
		    getPlayer().sendMessage(message.getText());
		});
		break;

	    case 51: // coins
		getPlayer().closeInventory();
		getPlayer().sendMessage(Lang.COIN_CHAT_MESSAGE.getText());
		GuiListener.setChatAction(getOwnerId(), msg -> {
		    String[] args = msg.split(" ");
		    Lang message = null;
		    try {
			int amount = Integer.parseInt(args[0]);
			UUID soul = SoulWallet.ANY;
			if (!Spigots.hasEmptySlot(getPlayer())) message = Lang.INVENTORY_FULL;
			else {
			    if (!getWallet().canRemoveSoul(soul, amount)) message = Lang.DONT_HAVE_SOULS;
			    else {
				ItemStack is = getWallet().withdrawCoins(amount);
				getPlayer().getInventory().addItem(is);
				message = Lang.COINS_REMOVED;
			    }
			}
		    } catch (NumberFormatException e) {
			message = Lang.NOT_A_NUMBER;
		    }
		    getPlayer().sendMessage(message.getText());
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
