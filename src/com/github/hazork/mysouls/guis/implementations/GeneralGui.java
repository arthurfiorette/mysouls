package com.github.hazork.mysouls.guis.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.commands.commands.InfoCommand;
import com.github.hazork.mysouls.guis.Gui;
import com.github.hazork.mysouls.guis.GuiListener;
import com.github.hazork.mysouls.utils.ItemBuilder;
import com.github.hazork.mysouls.utils.Utils.ItemStacks;
import com.github.hazork.mysouls.utils.Utils.Spigots;
import com.google.common.collect.Lists;

public class GeneralGui extends Gui {

    public static final String NAME = "General";

    private static UnaryOperator<Integer> slotFunc = i -> (i > 16 && i < 21) ? 21
	    : (i > 25 && i < 30) ? 30 : (i > 34 && i < 39) ? 39 : i;

    private static final ItemStack EMPTY_ITEM = new ItemStack(Material.THIN_GLASS);
    private static final ItemStack INFO = ItemStacks.set(true, ItemStacks.getHead("MHF_Question"), "§5Créditos",
	    InfoCommand.INFO);
    private static final ItemStack TROPHY = ItemStacks.set(true, ItemStacks.getHeadFromUrl(
	    "http://textures.minecraft.net/texture/e34a592a79397a8df3997c43091694fc2fb76c883a76cce89f0227e5c9f1dfe"),
	    "§6Ranking", "§cEm desenvolvimento.");

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
	inv.setItem(45, INFO);
	inv.setItem(19, TROPHY); // TODO Trophy page.
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
	    if (!EMPTY_ITEM.equals(inventory.getItem(i))) {
		inventory.setItem(i, EMPTY_ITEM);
	    }
	}

	List<String> lores = new ArrayList<>();
	lores.add("§eEste menu representa sua carteira de almas.");
	lores.add("");
	lores.add("§eAlmas: §f" + getWallet().getSoulsCount());
	lores.add("§eJogadores: §f" + getWallet().getDifferentSoulsCount());
	String media = String.format("%.2f",
		((double) getWallet().getSoulsCount() / getWallet().getDifferentSoulsCount()));

	lores.add("§eMédia: §f" + (media.equalsIgnoreCase("NaN") ? "§7?" : media));
	OfflinePlayer p = getWallet().getMostKilledPlayer();
	lores.add("§eMais almas de: §f" + (p == null ? "§7?" : p.getName()));

	ItemBuilder.ofHead(getOfflinePlayer(), true).setName("§6Sua carteira.").setLore(lores).setOnInventory(inventory,
		10);

	if (hasPreviousPage()) {
	    if (inventory.getItem(26) == null)
		new ItemBuilder(Material.STONE_BUTTON, true).setName("§cVoltar").setOnInventory(inventory, 26);
	} else if (inventory.getItem(26) != null) inventory.clear(26);

	if (hasNextPage()) {
	    if (inventory.getItem(35) == null)
		new ItemBuilder(Material.STONE_BUTTON, true).setName("§aAvançar").setOnInventory(inventory, 35);
	} else if (inventory.getItem(35) != null) inventory.clear(35);

	new ItemBuilder(Material.SKULL_ITEM, true).setDurability((byte) 1).setName("§7Retirar almas")
		.addLores("§fClique aqui para transfomar uma alma ", "§fda sua carteira em cabeça.")
		.setOnInventory(inventory, 49);

	ItemBuilder.ofHeadUrl(
		"http://textures.minecraft.net/texture/77b9dfd281deaef2628ad5840d45bcda436d6626847587f3ac76498a51c861",
		true).setName("§7Retirar coins")
		.setLores("§fClique aqui para transfomar uma alma ", "§fda sua carteira em coin.")
		.setOnInventory(inventory, 51);

	new ItemBuilder(Material.PAPER, true).setName("§7Página").setAmount(page + 1).setOnInventory(inventory, 53);
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
	return ItemBuilder.ofHead(player, true).setAmount(amount).setName("§5" + player.getName())
		.addLores("§dAlma de jogador.").build();
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
		getPlayer().sendMessage(
			"§6Escreva no chat o nome da alma que quer retirar ou escreva * para uma qualquer:");
		GuiListener.setChatAction(getOwnerId(), msg -> {
		    String[] args = msg.split(" ");
		    if (Spigots.hasEmptySlot(getPlayer())) {
			if (getWallet().canRemoveSoul()) {
			    if (args[0].equalsIgnoreCase("*")) {
				ItemStack is = getWallet().withdrawSoul();
				getPlayer().getInventory().addItem(is);
				getPlayer().sendMessage("§aAlma retirada.");
			    } else {
				@SuppressWarnings("deprecation")
				OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
				UUID uuid = player.getUniqueId();
				if (player != null && getWallet().canRemoveSoul(uuid)) {
				    ItemStack is = getWallet().withdrawSoul(uuid);
				    getPlayer().getInventory().addItem(is);
				    getPlayer().sendMessage("§aAlma retirada.");
				} else {
				    getPlayer().sendMessage("§cVocê não tem este jogador em sua carteira.");
				}
			    }
			} else {
			    getPlayer().sendMessage("§cSem almas suficientes.");
			}
		    } else {
			getPlayer().sendMessage("§cSem espaço no inventário.");
		    }
		    getPlayer().sendMessage("§eOperação finalizada");
		});
		break;

	    case 51: // coins
		getPlayer().closeInventory();
		getPlayer().sendMessage("\n§6Escreva no chat a quantia de almas para virar coins:\n");
		GuiListener.setChatAction(getOwnerId(), msg -> {
		    String[] args = msg.split(" ");
		    try {
			int amount = Integer.parseInt(args[0]);
			if (Spigots.hasEmptySlot(getPlayer())) {
			    if (getWallet().canRemoveSouls(amount)) {
				ItemStack is = getWallet().withdrawCoins(amount);
				getPlayer().getInventory().addItem(is);
				getPlayer().sendMessage("§aCoins retirados.");
			    } else {
				getPlayer().sendMessage("§cSem almas suficientes.");
			    }
			} else {
			    getPlayer().sendMessage("§cSem espaço no inventário.");
			}
		    } catch (NumberFormatException e) {
			getPlayer().sendMessage("§c" + args[0] + " não é um numero.");
		    }
		    getPlayer().sendMessage("§eOperação finalizada");
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
