package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MSCommand;
import com.github.hazork.mysouls.souls.SoulWallet;

public class WithdrawCommand implements MSCommand {

    @Override
    public void handle(CommandSender sender, String[] arguments, String label) {
	Player player = (Player) sender;
	SoulWallet wallet = MySouls.getWalletDB().from(player);
	if (player.getInventory().firstEmpty() != -1) {
	    if (wallet.canLoseSoul()) {
		ItemStack soul = wallet.withdraw();
		player.getInventory().addItem(soul);
	    } else {
		player.sendMessage("§cSem almas restantes");
	    }
	} else {
	    player.sendMessage("§cSem espaço no inventário");
	}
    }

    @Override
    public String getName() {
	return "withdraw";
    }

    @Override
    public boolean isOnlyPlayer() {
	return true;
    }

}
