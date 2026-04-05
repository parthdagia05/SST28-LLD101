package com.example.cache;

/**
 * Database interface for fetching values on cache miss.
 */
public interface Database {
    String get(String key);
    void put(String key, String value);
}
