package com.example.cache;

/**
 * Demonstrates the Distributed Cache system.
 *
 * - Data is distributed across nodes using modulo hashing: hash(key) % numberOfNodes
 * - Cache miss triggers a database fetch and populates the cache node
 * - LRU eviction kicks in when a node exceeds its capacity
 * - Both distribution strategy and eviction policy are pluggable via interfaces
 */
public class App {
    public static void main(String[] args) {
        // Configuration
        int numberOfNodes = 3;
        int capacityPerNode = 2;

        // Pluggable components
        DistributionStrategy strategy = new ModuloDistributionStrategy();
        EvictionPolicyFactory evictionFactory = LRUEvictionPolicy::new;
        Database database = new InMemoryDatabase();

        // Pre-populate database
        database.put("user:1", "Alice");
        database.put("user:2", "Bob");
        database.put("user:3", "Charlie");
        database.put("user:4", "Diana");
        database.put("user:5", "Eve");

        System.out.println("=== Distributed Cache Demo ===");
        System.out.println("Nodes: " + numberOfNodes + ", Capacity per node: " + capacityPerNode);
        System.out.println("Distribution: Modulo Hashing, Eviction: LRU\n");

        DistributedCache cache = new DistributedCache(
                numberOfNodes, capacityPerNode, strategy, evictionFactory, database);

        // 1. Cache misses — fetched from DB and stored in cache
        System.out.println("--- Phase 1: Initial gets (all cache misses) ---");
        System.out.println("Result: " + cache.get("user:1"));
        System.out.println();
        System.out.println("Result: " + cache.get("user:2"));
        System.out.println();
        System.out.println("Result: " + cache.get("user:3"));
        System.out.println();

        // 2. Cache hits — served from cache
        System.out.println("--- Phase 2: Repeat gets (cache hits) ---");
        System.out.println("Result: " + cache.get("user:1"));
        System.out.println();

        // 3. Put new values — triggers eviction if node is full
        System.out.println("--- Phase 3: Puts (may trigger LRU eviction) ---");
        cache.put("user:4", "Diana");
        System.out.println();
        cache.put("user:5", "Eve");
        System.out.println();

        // 4. Verify eviction — previously cached key may now be a miss
        System.out.println("--- Phase 4: Gets after eviction ---");
        System.out.println("Result: " + cache.get("user:2"));
        System.out.println();
        System.out.println("Result: " + cache.get("user:3"));
        System.out.println();
        System.out.println("Result: " + cache.get("user:4"));
        System.out.println();
    }
}
