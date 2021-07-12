package com.github._hazork.oldmysouls.utils.db;

import java.util.HashMap;

public abstract class HashMapDB<K, V> extends MapDB<HashMap<K, V>, K, V> {

  private volatile HashMap<K, V> hashmap = new HashMap<>();

  @Override
  protected HashMap<K, V> getMap() {
    return this.hashmap;
  }

  @Override
  protected void putValue(final V value) {
    this.hashmap.put(this.keyFunction(value), value);
  }
}
