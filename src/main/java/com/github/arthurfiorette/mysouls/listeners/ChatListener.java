package com.github.arthurfiorette.mysouls.listeners;

import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import com.github.arthurfiorette.sinklibrary.events.waiter.SingleEventWaiter;
import java.util.concurrent.CompletableFuture;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends SingleEventWaiter<AsyncPlayerChatEvent> {

  public ChatListener(@NonNull BasePlugin basePlugin) {
    super(AsyncPlayerChatEvent.class, basePlugin, EventPriority.LOWEST);
  }

  public CompletableFuture<String> waitMessage(Player player) {
    return this.waitEvent(e -> e.getPlayer().getUniqueId().equals(player.getUniqueId()))
      .thenApply(event -> event.getMessage());
  }
}
