package com.github.hazork.mysouls.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hazork.mysouls.commands.commands.InfoCommand;
import com.github.hazork.mysouls.commands.commands.WithdrawCommand;
import com.github.hazork.mysouls.util.Spigots;

public class CommandHandler implements CommandExecutor {

    private final String defaultArgument;
    private final String commandName;

    private Map<String, MSCommand> commandMap = new HashMap<>();

    public CommandHandler(String commandName, String defaultArgument) {
	this.commandName = commandName;
	this.defaultArgument = defaultArgument;
	addCommand(new InfoCommand(), new WithdrawCommand());
    }

    public void registerFor(JavaPlugin plugin) {
	plugin.getCommand(commandName).setExecutor(this);
    }

    private void addCommand(MSCommand... mscs) {
	Arrays.stream(mscs).forEach(msc -> commandMap.put(msc.getName(), msc));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	String arg0 = args.length == 0 ? defaultArgument : args[0];
	MSCommand msc = commandMap.get(arg0);
	if (Objects.nonNull(msc)) {
	    if (!msc.isOnlyPlayer() || msc.isOnlyPlayer() && Spigots.isPlayer(sender)) {
		if (!msc.getPermission().isPresent() || sender.hasPermission(msc.getPermission().get())) {
		    msc.handle(sender, args, label);
		    return true;
		}
	    }
	}
	sender.sendMessage("§cArgumento inválido, tente novamente.");
	return true;
    }
}
