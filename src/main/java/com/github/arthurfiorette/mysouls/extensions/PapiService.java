package com.github.arthurfiorette.mysouls.extensions;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.sinklibrary.interfaces.BaseService;
import com.github.arthurfiorette.sinklibrary.services.SpigotService;
import java.util.logging.Level;
import lombok.Getter;

public class PapiService implements BaseService {

  @Getter
  private final MySouls basePlugin;

  @Getter
  private PapiExpansion expansion;

  public PapiService(final MySouls owner) {
    this.basePlugin = owner;
  }

  @Override
  public void enable() throws Exception {
    if (!SpigotService.hasPlugin("PlaceholderAPI")) {
      this.basePlugin.log(
          Level.WARNING,
          "PlaceholderAPI could not be found, ignoring this service."
        );
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
