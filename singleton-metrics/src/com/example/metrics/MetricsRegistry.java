package com.example.metrics;

import java.io.ObjectStreamException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MetricsRegistry implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Long> counters = new HashMap<>();

    // Reflection guard
    private static boolean initialized = false;

    // Private constructor
    private MetricsRegistry() {
        synchronized (MetricsRegistry.class) {
            if (initialized) {
                throw new IllegalStateException("Singleton already initialized");
            }
            initialized = true;
        }
    }

    // Lazy-loaded, thread-safe holder
    private static class Holder {
        private static final MetricsRegistry INSTANCE = new MetricsRegistry();
    }

    public static MetricsRegistry getInstance() {
        return Holder.INSTANCE;
    }

    // ---- Singleton preservation during deserialization ----
    private Object readResolve() throws ObjectStreamException {
        return getInstance();
    }

    // ---- Metrics API ----

    public synchronized void setCount(String key, long value) {
        counters.put(key, value);
    }

    public synchronized void increment(String key) {
        counters.put(key, getCount(key) + 1);
    }

    public synchronized long getCount(String key) {
        return counters.getOrDefault(key, 0L);
    }

    public synchronized Map<String, Long> getAll() {
        return Collections.unmodifiableMap(new HashMap<>(counters));
    }
}