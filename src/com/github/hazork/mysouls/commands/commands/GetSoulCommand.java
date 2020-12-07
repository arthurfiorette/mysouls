package com.github.hazork.mysouls.commands.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.souls.SoulsDB;
import com.github.hazork.mysouls.utils.Utils.Spigots;

public class GetSoulCommand implements MySoulsCommand {

    private static final SoulsDB DB = MySouls.getDB();

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	Player player = (Player) sender;
	SoulWallet wallet = DB.from(player);
	if (args.length < 1) {
	    if (wallet.canRemoveSoul()) {
		if (Spigots.hasEmptySlot(player)) {
		    ItemStack is = wallet.withdrawSoul();
		    player.getInventory().addItem(is);
		    player.sendMessage(Lang.SOUL_REMOVED.getText());
		} else {
		    player.sendMessage(Lang.INVENTORY_FULL.getText());
		}
	    } else {
		player.sendMessage(Lang.DONT_HAVE_SOULS.getText());
	    }
	} else {
	    Player soul = Bukkit.getPlayerExact(args[0]);
	    if (soul != null) {
		if (wallet.canRemoveSoul(soul.getUniqueId())) {
		    ItemStack is = wallet.withdrawSoul(soul.getUniqueId());
		    player.getInventory().addItem(is);
		    player.sendMessage(Lang.SOUL_REMOVED.getText());
		} else {
		    player.sendMessage(Lang.DONT_HAVE_SOUL.getText());
		}
	    } else {
		player.sendMessage(Lang.PLAYER_NOT_FOUND.getText());
	    }
	}
    }

    @Override
    public String getName() {
	return Lang.GETSOUL_COMMAND.getText();
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return (sender instanceof Player);
    }

}
