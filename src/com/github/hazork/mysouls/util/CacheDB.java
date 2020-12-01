package com.github.hazork.mysouls.util;

public abstract class CacheDB<K, V> {

    private final CacheMap<K, V> cache;

    public CacheDB(long delay, long seconds) {
	cache = new CacheMap<K, V>(delay, seconds, (k, v) -> save(v));
    }

    public V from(K key) {
	if (!cache.containsKey(key)) load(key);
	return cache.get(key);
    }

    protected void putCache(V value) {
	cache.put(keyFunction(value), value);
    }

    protected abstract K keyFunction(V value);

    public abstract boolean open();

    protected abstract void save(V value);

    protected abstract void load(K key);

    public abstract boolean close();

}
