package com.example.cache;

import java.util.LinkedHashMap;
import java.util.Iterator;

/**
 * LRU eviction: evicts the least recently accessed key.
 * Uses a LinkedHashMap in access-order mode to track recency.
 */
public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final LinkedHashMap<K, Boolean> accessOrder;

    public LRUEvictionPolicy() {
        // accessOrder=true makes iteration order = access order (least recent first)
        this.accessOrder = new LinkedHashMap<>(16, 0.75f, true);
    }

    @Override
    public void keyAccessed(K key) {
        accessOrder.put(key, Boolean.TRUE);
    }

    @Override
    public K evict() {
        Iterator<K> iterator = accessOrder.keySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        K lruKey = iterator.next();
        iterator.remove();
        return lruKey;
    }

    @Override
    public void remove(K key) {
        accessOrder.remove(key);
    }
}
