package com.github.arthurfiorette.mysouls.config;

import com.github.arthurfiorette.sinklibrary.config.addons.PathResolver;

public enum Config implements PathResolver {

  INITIAL_SOULS("souls.initial-souls"),
  COIN_HEAD_URL("itens.coin-head-url"),
  SOUL_HEAD_URL("itens.soul-head-url"),
  TROPHY_HEAD_URL("itens.trophy-head-url");

  private String path;

  Config(final String path) {
    this.path = path;
  }

  @Override
  public String getPath() {
    return this.path;
  }

}
