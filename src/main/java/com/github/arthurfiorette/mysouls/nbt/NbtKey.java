package com.github.arthurfiorette.mysouls.nbt;

public enum NbtKey {
  COIN("coin"),
  SOUL("soul");

  private String name;

  NbtKey(final String name) {
    this.name = "MySouls-2.0.0-" + name;
  }

  public String getName() {
    return this.name;
  }
}
