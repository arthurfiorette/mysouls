package com.github.hazork.mysouls.commands.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.hazork.mysouls.MySouls;
import com.github.hazork.mysouls.commands.MySoulsCommand;
import com.github.hazork.mysouls.data.lang.Lang;
import com.github.hazork.mysouls.guis.implementations.GeneralGui;

public class MenuCommand implements MySoulsCommand {

    @Override
    public void handle(CommandSender sender, String[] args, String label) {
	MySouls.getGuiDB().from((Player) sender).open(GeneralGui.NAME);
    }

    @Override
    public String getName() {
	return Lang.MENU_COMMAND.getText();
    }

    @Override
    public boolean predicate(CommandSender sender) {
	return sender instanceof Player;
    }

}
