package com.github.arthurfiorette.mysouls.menu;

import com.github.arthurfiorette.sinklibrary.core.BasePlugin;
import com.github.arthurfiorette.sinklibrary.menu.BaseMenu;
import com.github.arthurfiorette.sinklibrary.menu.management.MenuFactory;
import org.bukkit.entity.Player;

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
