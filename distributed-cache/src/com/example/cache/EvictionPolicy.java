package com.example.cache;

/**
 * Pluggable eviction policy interface.
 * Implementations: LRU, MRU, LFU, etc.
 */
public interface EvictionPolicy<K> {
    void keyAccessed(K key);
    K evict();
    void remove(K key);
}
