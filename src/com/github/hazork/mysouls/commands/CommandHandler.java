package com.github.hazork.mysouls.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.hazork.mysouls.commands.commands.GetCoinCommand;
import com.github.hazork.mysouls.commands.commands.GetSoulCommand;
import com.github.hazork.mysouls.commands.commands.InfoCommand;
import com.github.hazork.mysouls.commands.commands.MenuCommand;
import com.github.hazork.mysouls.commands.commands.SoulsCommand;
import com.github.hazork.mysouls.data.lang.Lang;

public class CommandHandler implements CommandExecutor {

    private final String defaultArgument;
    private final String commandName;

    private Map<String, MySoulsCommand> commandMap = new HashMap<>();

    public CommandHandler(String commandName, String defaultArgument) {
	this.commandName = commandName;
	this.defaultArgument = defaultArgument;
	addCommand(new InfoCommand(), new GetSoulCommand(), new GetCoinCommand(), new MenuCommand(),
		new SoulsCommand());
    }

    public void registerFor(JavaPlugin plugin) {
	plugin.getCommand(commandName).setExecutor(this);
    }

    private void addCommand(MySoulsCommand... mscs) {
	Arrays.stream(mscs).forEach(msc -> {
	    if (commandMap.containsKey(msc.getName()))
		throw new RuntimeException("Command with this name is already created: " + msc.getName());
	    else commandMap.put(msc.getName(), msc);
	});
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	String arg0 = args.length == 0 ? defaultArgument : args[0];
	MySoulsCommand msc = commandMap.get(arg0);
	if (Objects.nonNull(msc) && msc.predicate(sender)) {
	    if (!msc.getPermission().isPresent() || sender.hasPermission(msc.getPermission().get())) {
		String[] arguments = args.length == 0 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
		msc.handle(sender, arguments, label);
		return true;
	    }
	}
	sender.sendMessage(Lang.UNKNOWN_ARGUMENT.getText());
	return true;
    }

}
