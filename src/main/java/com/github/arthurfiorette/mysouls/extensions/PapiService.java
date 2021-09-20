package com.github.arthurfiorette.mysouls.extensions;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.sinklibrary.component.Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public class PapiService implements Service {

  @Getter
  private final MySouls basePlugin;

  @Getter
  private PapiExpansion expansion;

  @Override
  public void enable() throws Exception {
    if (!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
      basePlugin.getBaseLogger().warn("PlaceholderAPI could not be found, ignoring this service.");
      return;
    }

    this.expansion = new PapiExpansion(this.basePlugin);
    this.expansion.register();
  }

  @Override
  public void disable() throws Exception {
    if (this.expansion != null) {
      this.expansion.unregister();
    }
  }
}
