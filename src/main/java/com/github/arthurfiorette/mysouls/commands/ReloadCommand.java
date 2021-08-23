package com.github.arthurfiorette.mysouls.commands;

import com.github.arthurfiorette.sinklibrary.command.BaseCommand;
import com.github.arthurfiorette.sinklibrary.command.wrapper.CommandInfo.CommandInfoBuilder;
import com.github.arthurfiorette.sinklibrary.component.providers.ComponentProvider;
import com.github.arthurfiorette.sinklibrary.component.providers.ComponentProvider.State;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ReloadCommand implements BaseCommand {

  @Getter
  @NonNull
  private final BasePlugin basePlugin;

  @Override
  public void handle(final CommandSender sender, final Collection<String> args) {
    final ComponentProvider provider = this.basePlugin.getProvider();

    if (provider.state() != State.ENABLED) {
      sender.sendMessage(
        "§cI cannot be reloaded if my status is " + provider.state().toString().toLowerCase()
      );
      return;
    }

    sender.sendMessage("§cReloading...");

    provider.disableAll();
    provider.enableAll();

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

  /**
   * Everyone can use it
   */
  @Override
  public boolean test(final CommandSender sender) {
    return true;
  }
}
