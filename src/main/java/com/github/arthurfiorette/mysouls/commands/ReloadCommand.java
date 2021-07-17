package com.github.arthurfiorette.mysouls.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.sinklibrary.command.BaseCommand;
import com.github.arthurfiorette.sinklibrary.command.wrapper.CommandInfo.CommandInfoBuilder;
import com.github.arthurfiorette.sinklibrary.components.ComponentManager;
import com.github.arthurfiorette.sinklibrary.components.ManagerState;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReloadCommand implements BaseCommand {

  @Getter
  private final MySouls basePlugin;

  @Override
  public void handle(final CommandSender sender, final Collection<String> args) {
    final ComponentManager manager = this.basePlugin.getManager();

    if (manager.getState() != ManagerState.ENABLED) {
      sender.sendMessage(
          "§cI cannot be reloaded if my status is " + manager.getState().toString().toLowerCase());
      return;
    }

    sender.sendMessage("§cReloading...");

    manager.disableServices();
    manager.enableServices();

    sender.sendMessage("§aReloaded");
  }

  @Override
  public List<String> onTabComplete(final CommandSender sender, final Collection<String> args) {
    return new ArrayList<>();
  }

  @Override
  public void info(final CommandInfoBuilder info) {
    info.name("reload");
    info.permission("mysouls.reload");
    info.description("Reload this plugin");
  }

  @Override
  public boolean test(final CommandSender sender) {
    // Everyone can use it
    return true;
  }

}
