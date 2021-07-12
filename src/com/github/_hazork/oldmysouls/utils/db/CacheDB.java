package com.github._hazork.oldmysouls.utils.db;

public abstract class CacheDB<K, V> extends MapDB<CacheMap<K, V>, K, V> {

  private volatile CacheMap<K, V> cache;

  public CacheDB(final long delay, final long seconds) {
    this.cache = new CacheMap<>(delay, seconds, (k, v) -> this.save(v));
  }

  @Override
  protected CacheMap<K, V> getMap() {
    return this.cache;
  }

  @Override
  protected void putValue(final V value) {
    this.cache.put(this.keyFunction(value), value);
  }

  protected abstract void save(V value);

  public abstract boolean open();

  public abstract boolean close();
}
