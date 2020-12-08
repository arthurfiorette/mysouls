package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.data.lang.Lang;

public class SoulsCommand implements MySoulsCommand {

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	int amount = MySouls.getDB().from((Player) sender).getSoulsCount();
	sender.sendMessage(Lang.SOULS_INFO.getText("{souls}", String.valueOf(amount)));
    }

    @Override
    public String getName() {
	return Lang.SOULS_INFO_COMMAND.getText();
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return sender instanceof Player;
    }

}
