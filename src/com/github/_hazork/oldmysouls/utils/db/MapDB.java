package com.github._hazork.oldmysouls.utils.db;

import java.util.Map;

public abstract class MapDB<M extends Map<K, V>, K, V> {

  public V from(final K key) {
    if (!this.getMap().containsKey(key)) {
      this.load(key);
    }
    return this.getMap().get(key);
  }

  protected abstract M getMap();

  protected abstract void putValue(V value);

  protected abstract K keyFunction(V value);

  protected abstract void load(K key);
}
