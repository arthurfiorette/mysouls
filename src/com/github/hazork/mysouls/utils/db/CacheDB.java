package com.github.hazork.mysouls.utils.db;

public abstract class CacheDB<K, V> extends MapDB<CacheMap<K, V>, K, V> {

    private volatile CacheMap<K, V> cache;

    public CacheDB(long delay, long seconds) {
	cache = new CacheMap<>(delay, seconds, (k, v) -> save(v));
    }

    @Override
    protected CacheMap<K, V> getMap() {
	return cache;
    }

    @Override
    protected void putValue(V value) {
	cache.put(keyFunction(value), value);
    }

    protected abstract void save(V value);

    public abstract boolean open();

    public abstract boolean close();
}
