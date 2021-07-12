package com.github._hazork.oldmysouls.commands;

import java.util.Optional;

import org.bukkit.command.CommandSender;

public interface MySoulsCommand {

  void handle(CommandSender sender, String[] args, String label);

  String getName();

  boolean predicate(CommandSender sender);

  default Optional<String> getPermission() {
    return Optional.empty();
  }
}
