package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.souls.SoulsDB;

public class GetCoinCommand implements MySoulsCommand {

    private static final SoulsDB DB = MySouls.getDB();

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	Player player = (Player) sender;
	SoulWallet wallet = DB.from(player);
	try {
	    int i = Integer.parseInt(args[0]);
	    if (wallet.canRemoveSoul()) {
		ItemStack is = wallet.withdrawCoins(i);
		player.getInventory().addItem(is);
		player.sendMessage(Lang.COINS_REMOVED.getText());
	    } else {
		player.sendMessage(Lang.DONT_HAVE_SOULS.getText());
	    }
	} catch (NumberFormatException e) {
	    player.sendMessage(Lang.NOT_A_NUMBER.getText());
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
