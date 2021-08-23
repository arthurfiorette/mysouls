package com.github.arthurfiorette.mysouls.nbt;

import lombok.Getter;

public enum NbtKey {
  COIN("coin"),
  SOUL("soul");

  @Getter
  private final String name;

  NbtKey(final String name) {
    this.name = "MySouls-2.0.0-" + name;
  }
}
