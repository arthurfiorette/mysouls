package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MSCommand;

public class InfoCommand implements MSCommand {

    @Override
    public void handle(CommandSender sender, String[] arguments, String label) {
	String[] info = { "§6This server is running: §5MySouls §c- §d" + MySouls.getVersion(),
		"§fPowered by: §l§eHazork §c-  §e§nGithub.com/Hazork" };
	sender.sendMessage(info);
    }

    @Override
    public String getName() {
	return "info";
    }

    @Override
    public boolean isOnlyPlayer() {
	return false;
    }

}
