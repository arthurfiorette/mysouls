package com.github.hazork.mysouls.commands.commands;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.google.common.collect.Lists;

public class InfoCommand implements MySoulsCommand {

    public static final List<String> infoList = Lists.newArrayList(
	    "§6This server is running: §5MySouls §c- §d" + MySouls.getVersion(),
	    "§fPowered by: §l§eHazork §c-  §e§nGithub.com/Hazork");

    @Override
    public void handle(CommandSender sender, String[] arguments, String label) {
	LinkedList<String> info = new LinkedList<>(infoList);
	info.addFirst("");
	info.addLast("");
	info.stream().forEach(sender::sendMessage);
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
