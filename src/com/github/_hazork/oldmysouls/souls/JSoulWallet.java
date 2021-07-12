package com.github._hazork.oldmysouls.souls;

import com.google.gson.Gson;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class JSoulWallet {

  private static final Gson GSON = new Gson();

  private transient SoulWallet obj = null;
  private transient String json = null;

  private String uuid;
  private Map<String, Integer> souls;

  public static JSoulWallet from(final SoulWallet wallet) {
    final JSoulWallet jsw = new JSoulWallet();
    jsw.obj = wallet;
    jsw.uuid = wallet.getOwnerId().toString();
    jsw.souls =
      wallet.souls
        .entrySet()
        .stream()
        .collect(Collectors.toMap(e -> e.getKey().toString(), Entry::getValue));
    return jsw;
  }

  public static JSoulWallet from(final String json) {
    return JSoulWallet.GSON.fromJson(json, JSoulWallet.class);
  }

  public String getJson() {
    return (Objects.nonNull(this.json)) ? this.json : (this.json = JSoulWallet.GSON.toJson(this));
  }

  public SoulWallet getWallet() {
    if (Objects.nonNull(this.obj)) {
      return this.obj;
    }
    final SoulWallet wallet = new SoulWallet(UUID.fromString(this.uuid));
    wallet.souls =
      this.souls.entrySet()
        .stream()
        .collect(Collectors.toMap(e -> UUID.fromString(e.getKey()), Entry::getValue));
    return this.obj = wallet;
  }
}
