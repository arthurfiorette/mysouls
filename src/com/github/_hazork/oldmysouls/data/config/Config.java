package com.github._hazork.oldmysouls.data.config;

public enum Config {
  INITIAL_SOULS("souls.initial-souls"),
  COIN_HEAD_URL("itens.coin-head-url"),
  SOUL_HEAD_URL("itens.soul-head-url"),
  TROPHY_HEAD_URL("itens.trophy-head-url");

  private String path;

  Config(final String path) {
    this.path = path;
  }

  public String getPath() {
    return this.path;
  }

  public String getText() {
    return ConfigFile.getText(this);
  }

  public int getInt() {
    return ConfigFile.getInteger(this);
  }
}
