package com.github._hazork.oldmysouls.commands.commands;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.commands.MySoulsCommand;
import com.github._hazork.oldmysouls.data.lang.Lang;
import com.github._hazork.oldmysouls.guis.implementations.GeneralGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MenuCommand implements MySoulsCommand {

  @Override
  public void handle(final CommandSender sender, final String[] args, final String label) {
    MySouls.getGuiDB().from((Player) sender).open(GeneralGui.NAME);
  }

  @Override
  public String getName() {
    return Lang.MENU_COMMAND.getText();
  }

  @Override
  public boolean predicate(final CommandSender sender) {
    return sender instanceof Player;
  }
}
