package com.github.hazork.mysouls.commands.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.data.lang.Lang;

public class SoulsCommand implements MySoulsCommand {

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	int amount = MySouls.getDB().from((Player) sender).getSoulsCount();
	Map<String, String> placeholders = new HashMap<>();
	placeholders.put("{souls}", amount + "");
	sender.sendMessage(Lang.SOULS_INFO.getText());
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
