# Distributed Cache - Low Level Design

## Class Diagram

```
                    ┌─────────────────────┐
                    │       App           │
                    │  (main entry point) │
                    └─────────┬───────────┘
                              │ uses
                              ▼
                ┌──────────────────────────────┐
                │      DistributedCache        │
                │──────────────────────────────│
                │ - nodes: List<CacheNode>     │
                │ - distributionStrategy       │
                │ - database: Database         │
                │──────────────────────────────│
                │ + get(key): String           │
                │ + put(key, value): void      │
                └──────┬──────┬──────┬─────────┘
                       │      │      │
          ┌────────────┘      │      └─────────────┐
          ▼                   ▼                     ▼
┌──────────────────┐ ┌────────────────┐  ┌──────────────────────┐
│«interface»       │ │   CacheNode    │  │ «interface»          │
│DistributionStrategy│ │──────────────│  │   Database           │
│──────────────────│ │ - id: int      │  │──────────────────────│
│+getNodeIndex(    │ │ - capacity     │  │ + get(key): String   │
│  key, totalNodes)│ │ - store: Map   │  │ + put(key, val): void│
└────────┬─────────┘ │ - evictionPolicy│  └──────────┬───────────┘
         │           │──────────────│            │
         ▼           │ + get(key)   │            ▼
┌──────────────────┐ │ + put(k,v)   │  ┌──────────────────────┐
│ModuloDistribution│ └──────┬───────┘  │  InMemoryDatabase     │
│    Strategy      │        │          └──────────────────────┘
└──────────────────┘        │ uses
                            ▼
                 ┌────────────────────┐
                 │ «interface»        │
                 │  EvictionPolicy<K> │
                 │────────────────────│
                 │ + keyAccessed(key) │
                 │ + evict(): K       │
                 │ + remove(key)      │
                 └────────┬───────────┘
                          │
                          ▼
                 ┌────────────────────┐
                 │ LRUEvictionPolicy  │
                 │────────────────────│
                 │ - accessOrder:     │
                 │   LinkedHashMap    │
                 └────────────────────┘

        ┌─────────────────────────┐
        │ «interface»             │
        │ EvictionPolicyFactory   │
        │─────────────────────────│
        │ + create(): EvictionPolicy │
        └─────────────────────────┘
```

## How It Works

### Data Distribution Across Nodes
- `DistributedCache` holds N `CacheNode` instances (configurable).
- When `get(key)` or `put(key, value)` is called, the `DistributionStrategy` determines which node handles the key.
- Current implementation: `ModuloDistributionStrategy` uses `Math.abs(key.hashCode()) % numberOfNodes`.

### Cache Miss Handling
1. `get(key)` routes to the correct node via the distribution strategy.
2. If the node doesn't have the key (**cache miss**), the `Database` is queried.
3. The fetched value is stored in the cache node for future hits.
4. If the key doesn't exist in the database either, `null` is returned.

### Eviction
- Each `CacheNode` has a fixed capacity.
- When a `put` would exceed capacity, the node's `EvictionPolicy` decides which key to remove.
- Current implementation: `LRUEvictionPolicy` uses a `LinkedHashMap` in access-order mode, so the least recently accessed key is always evicted first.

### Extensibility
| Concern | Interface | Current Impl | Future Options |
|---------|-----------|-------------|----------------|
| Distribution | `DistributionStrategy` | `ModuloDistributionStrategy` | Consistent Hashing, Map-based routing |
| Eviction | `EvictionPolicy<K>` | `LRUEvictionPolicy` | MRU, LFU, TTL-based |
| Database | `Database` | `InMemoryDatabase` | JDBC, Redis, any data store |
| Node creation | `EvictionPolicyFactory` | Lambda `LRUEvictionPolicy::new` | Factory for any policy |

To swap strategies, just pass a different implementation to `DistributedCache` — no code changes needed in the core classes.
