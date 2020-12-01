package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MSCommand;

public class SoulsCommand implements MSCommand {

    @Override
    public void handle(CommandSender sender, String[] arguments, String label) {
	Player player = (Player) sender;
	int value = MySouls.getWalletDB().from(player).getValue();
	player.sendMessage("Você tem " + value + " almas.");
    }

    @Override
    public String getName() {
	return "souls";
    }

    @Override
    public boolean isOnlyPlayer() {
	return true;
    }

}
