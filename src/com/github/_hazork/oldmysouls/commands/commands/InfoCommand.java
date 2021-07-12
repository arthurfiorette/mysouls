package com.github._hazork.oldmysouls.commands.commands;

import com.github._hazork.oldmysouls.MySouls;
import com.github._hazork.oldmysouls.commands.MySoulsCommand;
import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.CommandSender;

public class InfoCommand implements MySoulsCommand {

  public static final List<String> infoList = Lists.newArrayList(
    "§6This server is running: §5MySouls §c- §d" + MySouls.getVersion(),
    "§fPowered by: §l§eHazork §c-  §e§nGithub.com/Hazork"
  );

  @Override
  public void handle(final CommandSender sender, final String[] arguments, final String label) {
    final LinkedList<String> info = new LinkedList<>(InfoCommand.infoList);
    info.addFirst("");
    info.addLast("");
    info.stream().forEach(sender::sendMessage);
  }

  @Override
  public String getName() {
    return "info";
  }

  @Override
  public boolean predicate(final CommandSender sender) {
    return true;
  }
}
