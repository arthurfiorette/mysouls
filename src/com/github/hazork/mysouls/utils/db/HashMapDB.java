package com.github.hazork.mysouls.utils.db;

import java.util.HashMap;

public abstract class HashMapDB<K, V> extends MapDB<HashMap<K, V>, K, V> {

    private volatile HashMap<K, V> hashmap = new HashMap<>();

    @Override
    protected HashMap<K, V> getMap() {
	return hashmap;
    }

    @Override
    protected void putValue(V value) {
	hashmap.put(keyFunction(value), value);
    }
}
