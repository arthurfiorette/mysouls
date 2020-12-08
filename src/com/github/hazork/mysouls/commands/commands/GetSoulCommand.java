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
	Lang message = null;
	if (args.length < 1) {
	    if (!wallet.canRemoveSoul(SoulWallet.ANY, 1)) message = Lang.DONT_HAVE_SOULS;
	    else {
		if (!Spigots.hasEmptySlot(player)) message = Lang.INVENTORY_FULL;
		else {
		    ItemStack is = wallet.withdrawSoul(SoulWallet.ANY);
		    player.getInventory().addItem(is);
		    message = Lang.SOUL_REMOVED;
		}
	    }
	} else {
	    Player soul = Bukkit.getPlayerExact(args[0]);
	    if (soul == null) message = Lang.PLAYER_NOT_FOUND;
	    else {
		if (!wallet.canRemoveSoul(soul.getUniqueId(), 1)) message = Lang.DONT_HAVE_SOUL;
		else {
		    ItemStack is = wallet.withdrawSoul(soul.getUniqueId());
		    player.getInventory().addItem(is);
		    message = Lang.SOUL_REMOVED;
		}
	    }
	}
	player.sendMessage(message.getText());
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
