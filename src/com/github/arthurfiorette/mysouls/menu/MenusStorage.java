package com.github.arthurfiorette.mysouls.menu;

import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;
import com.github.arthurfiorette.sinklibrary.menu.management.MenuStorage;

public class MenusStorage extends MenuStorage<MenuList> {

  public MenusStorage(final BasePlugin plugin) {
    super(plugin, MenuList.class);
  }
}
