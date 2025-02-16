package com.cache;

import java.util.*;


public class CacheManager<K, V> {
    private final int maxSize;
    private final Map<K, V> cache;

    public CacheManager(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > CacheManager.this.maxSize;
            }
        };
    }

    public V get(K key) {
        return cache.get(key);
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V remove(K key) {
        return cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    @Override
    public String toString() {
        return cache.toString();
    }

	public void putIfAbsent(K key, V value) {
		cache.putIfAbsent(key,value);
	}

	public boolean containsKey(K key) {
		return cache.containsKey(key);
	}

	public int size() {
		return cache.size();
	}

	public Collection<V> getValues() {
		return cache.values();
	}
}
