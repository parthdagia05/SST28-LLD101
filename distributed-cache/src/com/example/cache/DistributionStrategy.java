package com.example.cache;

/**
 * Pluggable distribution strategy interface.
 * Determines which cache node stores a given key.
 * Implementations: ModuloDistribution, ConsistentHashing, etc.
 */
public interface DistributionStrategy {
    int getNodeIndex(String key, int totalNodes);
}
