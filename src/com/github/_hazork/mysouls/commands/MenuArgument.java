package com.github._hazork.mysouls.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github._hazork.mysouls.SoulsPlugin;
import com.github._hazork.mysouls.menu.SoulsMenu;

public class MenuArgument implements Argument {

  @Override
  public boolean test(final CommandSender t) {
    return t instanceof Player;
  }

  @Override
  public void onArgument(final CommandSender sender, final String alias, final List<String> args) {
    final Player player = (Player) sender;
    final SoulsMenu menu = SoulsPlugin.get().getMenuDatabase().get(player);
    menu.show(true);
  }

  @Override
  public String getName() {
    return "menu";
  }

}
