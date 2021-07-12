package com.github.arthurfiorette.mysouls.menu;

import org.bukkit.entity.Player;

import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.menu.BaseMenu;
import com.github.arthurfiorette.sinklibrary.menu.management.MenuFactory;

public enum MenuList implements MenuFactory {
  WALLET(WalletMenu::new);

  private MenuFactory factory;

  MenuList(final MenuFactory factory) {
    this.factory = factory;
  }

  @Override
  public BaseMenu create(final BasePlugin plugin, final Player player) {
    return this.factory.create(plugin, player);
  }
}
