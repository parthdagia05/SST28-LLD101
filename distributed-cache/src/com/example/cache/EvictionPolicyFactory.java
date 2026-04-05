package com.example.cache;

/**
 * Factory interface to create eviction policy instances.
 * Each cache node gets its own eviction policy instance.
 */
public interface EvictionPolicyFactory {
    EvictionPolicy<String> create();
}
