package com.github.arthurfiorette.mysouls.commands;

import com.github.arthurfiorette.mysouls.MySouls;
import com.github.arthurfiorette.sinklibrary.command.BaseCommand;
import com.github.arthurfiorette.sinklibrary.command.CommandStorage;

public class Commands extends CommandStorage {

  public Commands(final MySouls mysouls) {
    super(mysouls);
  }

  @Override
  protected BaseCommand[] commands() {
    return new BaseCommand[] { new SoulsCommand((MySouls) this.basePlugin) };
  }
}
