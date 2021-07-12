package com.github._hazork.oldmysouls.commands;

import com.github._hazork.oldmysouls.commands.commands.InfoCommand;
import com.github._hazork.oldmysouls.commands.commands.MenuCommand;
import com.github._hazork.oldmysouls.data.lang.Lang;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandHandler implements CommandExecutor {

  private final String defaultArgument;
  private final String commandName;

  private final Map<String, MySoulsCommand> commandMap = new HashMap<>();

  public CommandHandler(final String commandName, final String defaultArgument) {
    this.commandName = commandName;
    this.defaultArgument = defaultArgument;
    this.addCommand(new InfoCommand(), new MenuCommand());
  }

  public void registerFor(final JavaPlugin plugin) {
    plugin.getCommand(this.commandName).setExecutor(this);
  }

  private void addCommand(final MySoulsCommand... mscs) {
    Arrays
      .stream(mscs)
      .filter(ms -> !this.commandMap.containsKey(ms.getName()))
      .forEach(ms -> this.commandMap.put(ms.getName(), ms));
  }

  @Override
  public boolean onCommand(
    final CommandSender sender,
    final Command command,
    final String label,
    final String[] args
  ) {
    final String arg0 = args.length == 0 ? this.defaultArgument : args[0];
    final MySoulsCommand msc = this.commandMap.get(arg0);
    if (
      ((msc != null) && msc.predicate(sender)) &&
      (!msc.getPermission().isPresent() || sender.hasPermission(msc.getPermission().get()))
    ) {
      final String[] arguments = args.length == 0
        ? new String[0]
        : Arrays.copyOfRange(args, 1, args.length);
      msc.handle(sender, arguments, label);
      return true;
    }
    sender.sendMessage(Lang.UNKNOWN_ARGUMENT.getText());
    return true;
  }
}
