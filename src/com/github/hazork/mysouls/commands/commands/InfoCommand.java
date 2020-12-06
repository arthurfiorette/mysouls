package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;

public class InfoCommand implements MySoulsCommand {

    public static final String[] INFO = { "", "§6This server is running: §5MySouls §c- §d" + MySouls.getVersion(),
	    "§fPowered by: §l§eHazork §c-  §e§nGithub.com/Hazork", "" };

    @Override
    public void handle(CommandSender sender, String[] arguments, String label) {
	sender.sendMessage(INFO);
    }

    @Override
    public String getName() {
	return "info";
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return true;
    }

}
