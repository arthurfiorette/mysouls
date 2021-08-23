package com.github.arthurfiorette.mysouls;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SoulsUtils {

  public boolean isPack(final int amount) {
    return amount < 1 || amount > 64;
  }
}
