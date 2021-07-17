package com.github.arthurfiorette.mysouls.listeners;

import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.listener.SinkListener;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends SinkListener {

  @Getter
  private final Map<UUID, Consumer<String>> actions = new ConcurrentHashMap<>();

  public ChatListener(final BasePlugin basePlugin) {
    super(basePlugin);
  }

  public void addAction(final Player player, final Consumer<String> action) {
    this.actions.put(player.getUniqueId(), action);
  }

  @Override
  @EventHandler(priority = EventPriority.LOWEST)
  public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
    final UUID uuid = event.getPlayer().getUniqueId();

    final Consumer<String> action = this.actions.remove(uuid);

    // Action exists
    if (action != null) {
      action.accept(event.getMessage());
      event.setCancelled(true);
    }
  }
}
