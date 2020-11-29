package com.github.hazork.mysouls.commands;

import java.util.Optional;

import org.bukkit.command.CommandSender;

public interface MSCommand {

    void handle(CommandSender sender, String[] arguments, String label);

    String getName();

    boolean isOnlyPlayer();

    default Optional<String> getPermission() {
	return Optional.empty();
    }
}
