package com.example.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple in-memory database stub for demonstration.
 */
public class InMemoryDatabase implements Database {
    private final Map<String, String> store = new HashMap<>();

    @Override
    public String get(String key) {
        System.out.println("  [DB] Fetching key '" + key + "' from database");
        return store.get(key);
    }

    @Override
    public void put(String key, String value) {
        System.out.println("  [DB] Storing key '" + key + "' in database");
        store.put(key, value);
    }
}
