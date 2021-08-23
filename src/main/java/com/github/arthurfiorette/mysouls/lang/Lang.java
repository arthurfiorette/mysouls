package com.github.arthurfiorette.mysouls.lang;

import com.github.arthurfiorette.sinklibrary.config.addons.PathResolver;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Lang implements PathResolver {
  
  BACKWARD("messages.backward"),
  CANNOT_USE("messages.cannot-use"),
  COIN_CHAT_MESSAGE("messages.coin-chat-message"),
  COIN_LORE("itens.coin.lore"),
  COIN_NAME("itens.coin.name"),
  COINS_REMOVED("messages.coins-removed"),
  CREDITS("messages.credits"),
  DONT_HAVE_SOULS("messages.dont-have-souls"),
  FORWARD("messages.forward"),
  KILL_MESSAGE("messages.kill-message"),
  KILL_MESSAGE_FAIL("messages.kill-message-fail"),
  DEATH_MESSAGE("messages.death-message"),
  DEATH_MESSAGE_FAIL("messages.death-message-fail"),
  INVENTORY_DATABASE_CLOSED("messages.inventory-database-closed"),
  INVENTORY_FULL("messages.inventory-full"),
  INVENTORY_SOUL_LORE("menus.inventory-soul.lore"),
  INVENTORY_SOUL_NAME("menus.inventory-soul.name"),
  MENU_COMMAND("commands.menu-command"),
  NOT_A_NUMBER("messages.not-a-number"),
  PAGE("messages.page"),
  PLAYER_NOT_FOUND("messages.player-not-found"),
  RANKING_LORE("menus.ranking.lore"),
  RANKING_NAME("menus.ranking.name"),
  SOUL_64_LIMIT("messages.soul-64-limit"),
  SOUL_ADDED("messages.soul-added"),
  SOULS_ADDED("messages.souls-added"),
  SOUL_CHAT_MESSAGE("messages.soul-chat-message"),
  SOUL_LORE("itens.soul.lore"),
  SOUL_NAME("itens.soul.name"),
  SOUL_REMOVED("messages.soul-removed"),
  UNKNOWN_ARGUMENT("commands.unknown-argument"),
  WITHDRAW_COINS_LORE("menus.withdraw-coins.lore"),
  WITHDRAW_COINS_NAME("menus.withdraw-coins.name"),
  WITHDRAW_SOULS_LORE("menus.withdraw-souls.lore"),
  WITHDRAW_SOULS_NAME("menus.withdraw-souls.name"),
  YOUR_WALLET_LORE("menus.your-wallet.lore"),
  YOUR_WALLET_NAME("menus.your-wallet.name"),
  RELOAD_SUCCESS("admin.reload-success"),
  RELOAD_FAIL("admin.reload-fail"),
  UNKNOWN_ERROR("admin.unknown-error");

  @Getter
  @NonNull
  private final String path;



}
