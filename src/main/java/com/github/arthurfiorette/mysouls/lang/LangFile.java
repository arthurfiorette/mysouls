package com.github.arthurfiorette.mysouls.lang;

import com.github.arthurfiorette.sinklibrary.config.CustomConfig;
import com.github.arthurfiorette.sinklibrary.config.addons.EnumConfig;
import com.github.arthurfiorette.sinklibrary.interfaces.BasePlugin;

public class LangFile extends CustomConfig implements EnumConfig<Lang> {

  public LangFile(final BasePlugin plugin) {
    super(plugin, "lang.yml", false);
  }

  @Override
  public String getString(final Lang path) {
    // TODO Auto-generated method stub
    return EnumConfig.super.getString(path);
  }
}
