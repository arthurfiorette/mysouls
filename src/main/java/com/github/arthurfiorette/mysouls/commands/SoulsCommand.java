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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SoulsCommand implements BaseCommand {

  @Getter
  @NonNull
  private final BasePlugin basePlugin;

  @Getter
  @NonNull
  private final MenusStorage menusStorage;

  public SoulsCommand(final BasePlugin plugin) {
    this(plugin, plugin.get(MenusStorage.class));
  }

  @Override
  public void handle(final CommandSender sender, final Collection<String> args) {
    final Player player = (Player) sender;
    final WalletMenu menu = this.menusStorage.get(player, MenuList.WALLET);
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
  public BaseCommand[] subCommands() {
    return new BaseCommand[] { new ReloadCommand(this.basePlugin) };
  }
}
