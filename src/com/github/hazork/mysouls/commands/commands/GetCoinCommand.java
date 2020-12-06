package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.souls.SoulsDB;
import com.github.hazork.mysouls.utils.Utils;

public class GetCoinCommand implements MySoulsCommand {

    private static final SoulsDB DB = MySouls.getDB();

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	Player player = (Player) sender;
	SoulWallet wallet = DB.from(player);
	if (args.length < 1) {
	    if (wallet.canRemoveSoul()) {
		ItemStack is = wallet.withdrawCoin();
		player.getInventory().addItem(is);
		Utils.sendMessageFormat(player, "§aVocê retirou uma alma em coins.");
	    } else {
		Utils.sendMessageFormat(player, "§cVocê não tem almas suficientes.");
	    }
	} else {
	    try {
		int i = Integer.parseInt(args[0]);
		if (wallet.canRemoveSoul()) {
		    ItemStack is = wallet.withdrawCoins(i);
		    player.getInventory().addItem(is);
		    Utils.sendMessageFormat(player, "§aVocê retirou %s almas em coins.", i);
		} else {
		    Utils.sendMessageFormat(player, "§cVocê não tem almas suficientes.");
		}
	    } catch (NumberFormatException e) {
		Utils.sendMessageFormat(player, "§c%s não é um numero.", args[1]);
	    }
	}

    }

    @Override
    public String getName() {
	return "getcoins";
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return (sender instanceof Player);
    }

}
