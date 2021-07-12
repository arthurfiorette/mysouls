package com.github.arthurfiorette.mysouls.extensions;

import com.github.arthurfiorette.mysouls.MySouls;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PapiExpansion extends PlaceholderExpansion {

  private final MySouls plugin;

  public PapiExpansion(final MySouls plugin) {
    this.plugin = plugin;
  }

  @Override
  public String onPlaceholderRequest(final Player player, @NotNull final String params) {
    // TODO: Implement placeholder request.
    return null;
  }

  @Override
  public String getAuthor() {
    return this.plugin.getDescription().getAuthors().get(0);
  }

  @Override
  public String getIdentifier() {
    return this.getRequiredPlugin().toLowerCase();
  }

  @Override
  public String getVersion() {
    return MySouls.VERSION;
  }

  @Override
  public @Nullable String getRequiredPlugin() {
    return this.plugin.getName();
  }
}
