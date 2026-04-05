package com.example.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Distributed cache that spreads keys across multiple CacheNodes
 * using a pluggable DistributionStrategy.
 *
 * On cache miss, it fetches from the Database and populates the cache.
 * On put, it stores in both the appropriate cache node and the database.
 */
public class DistributedCache {
    private final List<CacheNode> nodes;
    private final DistributionStrategy distributionStrategy;
    private final Database database;

    public DistributedCache(int numberOfNodes, int capacityPerNode,
                            DistributionStrategy distributionStrategy,
                            EvictionPolicyFactory evictionPolicyFactory,
                            Database database) {
        this.distributionStrategy = distributionStrategy;
        this.database = database;
        this.nodes = new ArrayList<>();

        for (int i = 0; i < numberOfNodes; i++) {
            EvictionPolicy<String> policy = evictionPolicyFactory.create();
            nodes.add(new CacheNode(i, capacityPerNode, policy));
        }
    }

    public String get(String key) {
        int nodeIndex = distributionStrategy.getNodeIndex(key, nodes.size());
        CacheNode node = nodes.get(nodeIndex);
        System.out.println("[Cache] GET '" + key + "' -> Node " + nodeIndex);

        String value = node.get(key);
        if (value != null) {
            System.out.println("  [Cache HIT] Found in Node " + nodeIndex);
            return value;
        }

        // Cache miss: fetch from database
        System.out.println("  [Cache MISS] Key not in Node " + nodeIndex);
        value = database.get(key);
        if (value != null) {
            node.put(key, value);
        }
        return value;
    }

    public void put(String key, String value) {
        int nodeIndex = distributionStrategy.getNodeIndex(key, nodes.size());
        CacheNode node = nodes.get(nodeIndex);
        System.out.println("[Cache] PUT '" + key + "' -> Node " + nodeIndex);

        node.put(key, value);
        database.put(key, value);
    }

    public int getNumberOfNodes() {
        return nodes.size();
    }
}
