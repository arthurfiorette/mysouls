package com.github.arthurfiorette.mysouls.extensions;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseService;
import com.github.arthurfiorette.sinklibrary.services.SpigotService;
import java.util.logging.Level;

public class PapiService implements BaseService {

  private final MySouls owner;
  private PapiExpansion expansion;

  public PapiService(final MySouls owner) {
    this.owner = owner;
  }

  @Override
  public void enable() throws Exception {
    if (!SpigotService.hasPlugin("PlaceholderAPI")) {
      this.owner.log(Level.INFO, "PlaceholderAPI not found.");
      return;
    }

    this.expansion = new PapiExpansion(this.owner);
    this.expansion.register();
  }

  @Override
  public void disable() throws Exception {
    if (this.expansion != null) {
      this.expansion.unregister();
    }
  }

  @Override
  public BasePlugin getBasePlugin() {
    return this.owner;
  }
}
