package com.github._hazork.mysouls.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.github._hazork.mysouls.SoulsPlugin;

public class InfoArgument implements Argument {

  public static final String[] INFO = {
      "§6This server is running: §5MySouls §c- §d" + SoulsPlugin.getVersion(),
      "§fPowered by: §l§eHazork §c-  §e§nGithub.com/Hazork" };

  @Override
  public boolean test(final CommandSender t) {
    return true;
  }

  @Override
  public void onArgument(final CommandSender sender, final String alias, final List<String> args) {
    sender.sendMessage("");
    sender.sendMessage(InfoArgument.INFO);
    sender.sendMessage("");
  }

  @Override
  public String getName() {
    return "info";
  }

}
