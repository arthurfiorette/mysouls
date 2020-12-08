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
	Lang message = null;
	try {
	    int i = Integer.parseInt(args[0]);
	    if (!wallet.canRemoveSoul(SoulWallet.ANY, i)) message = Lang.DONT_HAVE_SOULS;
	    else {
		ItemStack is = wallet.withdrawCoins(i);
		player.getInventory().addItem(is);
		message = Lang.COINS_REMOVED;
	    }
	} catch (NumberFormatException e) {
	    message = Lang.NOT_A_NUMBER;
	}
	player.sendMessage(message.getText());
    }

    @Override
    public String getName() {
	return Lang.GETCOINS_COMMAND.getText();
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return (sender instanceof Player);
    }

}
