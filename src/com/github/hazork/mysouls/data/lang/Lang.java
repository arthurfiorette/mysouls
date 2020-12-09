package com.github.hazork.mysouls.data.lang;

import java.util.List;
import java.util.Map;

public enum Lang {

    BACKWARD("messages.backward"),
    CANNOT_USE("messages.cannot-use"),
    COIN_CHAT_MESSAGE("messages.coin-chat-message"),
    COIN_LORE("itens.coin.lore"),
    COIN_NAME("itens.coin.name"),
    COINS_REMOVED("messages.coins-removed"),
    CREDITS("messages.credits"),
    DONT_HAVE_SOUL("messages.dont-have-soul"),
    DONT_HAVE_SOULS("messages.dont-have-souls"),
    FORWARD("messages.forward"),
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
    SOUL_CHAT_MESSAGE("messages.soul-chat-message"),
    SOUL_LORE("itens.soul.lore"),
    SOUL_NAME("itens.soul.name"),
    SOUL_REMOVED("messages.soul-removed"),
    SOULS_ADDED("messages.souls-added"),
    UNKNOWN_ARGUMENT("commands.unknown-argument"),
    WITHDRAW_COINS_LORE("menus.withdraw-coins.lore"),
    WITHDRAW_COINS_NAME("menus.withdraw-coins.name"),
    WITHDRAW_SOULS_LORE("menus.withdraw-souls.lore"),
    WITHDRAW_SOULS_NAME("menus.withdraw-souls.name"),
    YOUR_WALLET_LORE("menus.your-wallet.lore"),
    YOUR_WALLET_NAME("menus.your-wallet.name");

    private String path;

    Lang(String path) {
	this.path = path;
    }

    public String getPath() {
	return path;
    }

    public String getText() {
	return LangFile.getString(this);
    }

    public String getText(String key, String value) {
	return LangFile.getString(this, key, value);
    }

    public String getText(Map<String, String> placeholders) {
	return LangFile.getString(this, placeholders);
    }

    public List<String> getList() {
	return LangFile.getList(this);
    }

    public List<String> getList(String key, String value) {
	return LangFile.getList(this, key, value);
    }

    public List<String> getList(Map<String, String> placeholders) {
	return LangFile.getList(this, placeholders);
    }
}
