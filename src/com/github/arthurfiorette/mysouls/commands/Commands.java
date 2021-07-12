package com.github.arthurfiorette.mysouls.commands;

import com.github.arthurfiorette.sinklibrary.command.BaseCommand;
import com.github.arthurfiorette.sinklibrary.command.CommandStorage;
import com.github.arthurfiorette.sinklibrary.core.BasePlugin;

public class Commands extends CommandStorage {

  public Commands(final BasePlugin basePlugin) {
    super(basePlugin);
  }

  @Override
  protected BaseCommand[] commands() {
    return new BaseCommand[] { new SoulsCommand(this.basePlugin) };
  }

}
