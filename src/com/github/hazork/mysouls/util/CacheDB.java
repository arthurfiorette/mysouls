package com.github.hazork.mysouls.util;

public abstract class CacheDB<K, V> {

    protected final CacheMap<K, V> cache;

    public CacheDB(long delay, long seconds) {
	cache = new CacheMap<K, V>(delay, seconds);
    }

    public int getCacheSize() {
	return cache.size();
    }

    public V from(K key) {
	if (!cache.containsKey(key)) load(key);
	return cache.get(key);
    }

    public abstract boolean open();

    protected abstract void save(V value);

    protected abstract void load(K key);

    public abstract boolean close();

}
