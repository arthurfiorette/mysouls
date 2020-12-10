package com.github.hazork.mysouls.utils.db;

import java.util.Map;

public abstract class MapDB<M extends Map<K, V>, K, V> {

    public V from(K key) {
	if (!getMap().containsKey(key)) {
	    load(key);
	}
	return getMap().get(key);
    }

    protected abstract M getMap();

    protected abstract void putValue(V value);

    protected abstract K keyFunction(V value);

    protected abstract void load(K key);
}
