package com.github.hazork.mysouls.commands.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.souls.SoulWallet;
import com.github.hazork.mysouls.souls.SoulsDB;
import com.github.hazork.mysouls.utils.Utils;
import com.github.hazork.mysouls.utils.Utils.Spigots;

public class GetSoulCommand implements MySoulsCommand {

    private static final SoulsDB DB = MySouls.getDB();

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	Player player = (Player) sender;
	SoulWallet wallet = DB.from(player);
	if (args.length < 1) {
	    if (wallet.canRemoveSoul() && Spigots.hasEmptySlot(player)) {
		ItemStack is = wallet.withdrawSoul();
		player.getInventory().addItem(is);
		Utils.sendMessageFormat(player, "§aVocê transformou uma alma em item.");
	    } else {
		Utils.sendMessageFormat(player, "§cSem almas restantes ou inventário cheio.");
	    }
	} else {
	    Player soul = Bukkit.getPlayerExact(args[0]);
	    if (soul != null) {
		if (wallet.canRemoveSoul(soul.getUniqueId())) {
		    ItemStack is = wallet.withdrawSoul(soul.getUniqueId());
		    player.getInventory().addItem(is);
		    Utils.sendMessageFormat(player, "§aVocê transformou a alma do jogador %s em item.", soul.getName());
		} else {
		    Utils.sendMessageFormat(player, "§cVocê não tem uma alma deste jogador.");
		}
	    } else {
		Utils.sendMessageFormat(player, "§cJogador não encontrado.");
	    }
	}
    }

    @Override
    public String getName() {
	return "getsoul";
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return (sender instanceof Player);
    }

}
