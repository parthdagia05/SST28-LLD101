package com.example.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * A single cache node with a fixed capacity and a pluggable eviction policy.
 * Stores key-value pairs and evicts according to the configured policy when full.
 */
public class CacheNode {
    private final int id;
    private final int capacity;
    private final Map<String, String> store;
    private final EvictionPolicy<String> evictionPolicy;

    public CacheNode(int id, int capacity, EvictionPolicy<String> evictionPolicy) {
        this.id = id;
        this.capacity = capacity;
        this.store = new HashMap<>();
        this.evictionPolicy = evictionPolicy;
    }

    public String get(String key) {
        if (!store.containsKey(key)) {
            return null;
        }
        evictionPolicy.keyAccessed(key);
        return store.get(key);
    }

    public void put(String key, String value) {
        if (store.containsKey(key)) {
            // Update existing key
            store.put(key, value);
            evictionPolicy.keyAccessed(key);
            return;
        }

        if (store.size() >= capacity) {
            String evictedKey = evictionPolicy.evict();
            if (evictedKey != null) {
                store.remove(evictedKey);
                System.out.println("  [Node " + id + "] Evicted key '" + evictedKey + "'");
            }
        }

        store.put(key, value);
        evictionPolicy.keyAccessed(key);
    }

    public int getId() {
        return id;
    }

    public int size() {
        return store.size();
    }
}
