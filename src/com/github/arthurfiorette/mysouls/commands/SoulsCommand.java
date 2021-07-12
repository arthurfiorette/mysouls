package com.github.arthurfiorette.mysouls.commands;

import com.github.arthurfiorette.mysouls.menu.MenuList;
import com.github.arthurfiorette.mysouls.menu.MenusStorage;
import com.github.arthurfiorette.mysouls.menu.WalletMenu;
import com.github.arthurfiorette.sinklibrary.command.BaseCommand;
import com.github.arthurfiorette.sinklibrary.command.wrapper.CommandInfo.CommandInfoBuilder;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SoulsCommand implements BaseCommand {

  private final BasePlugin basePlugin;

  private final MenusStorage menus;

  public SoulsCommand(final BasePlugin basePlugin) {
    this.basePlugin = basePlugin;
    this.menus = basePlugin.getComponent(MenusStorage.class);
  }

  @Override
  public void handle(final CommandSender sender, final Collection<String> args) {
    final Player player = (Player) sender;
    final WalletMenu menu = this.menus.get(player, MenuList.WALLET);
    menu.open(true);
  }

  @Override
  public List<String> onTabComplete(final CommandSender sender, final Collection<String> args) {
    return new ArrayList<>();
  }

  @Override
  public void info(final CommandInfoBuilder info) {
    info.name("souls");
    info.alias("ms").alias("mysouls");
    info.description(
      "The principal and only command from this plugin.\nYou can use /souls reload to reload our configuration."
    );
    info.usage("/souls [reload]");
    info.permission("mysouls.menu");
  }

  @Override
  public boolean test(final CommandSender sender) {
    return sender instanceof Player;
  }

  @Override
  public BasePlugin getBasePlugin() {
    return this.basePlugin;
  }
}
