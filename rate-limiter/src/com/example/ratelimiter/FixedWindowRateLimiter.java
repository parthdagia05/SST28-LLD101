package com.example.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Fixed Window Counter algorithm.
 *
 * Divides time into fixed windows (e.g., 0:00-1:00, 1:00-2:00).
 * Each key gets a counter per window. Resets when the window rolls over.
 *
 * Trade-offs:
 * + Simple and memory-efficient (one counter per key)
 * + O(1) time per check
 * - Boundary spike problem: a burst at the end of window N and start of window N+1
 *   can allow up to 2x the limit in a short span.
 */
public class FixedWindowRateLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, AtomicReference<WindowCounter>> counters;

    public FixedWindowRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.counters = new ConcurrentHashMap<>();
    }

    @Override
    public boolean tryConsume(String key) {
        AtomicReference<WindowCounter> ref = counters.computeIfAbsent(
                key, k -> new AtomicReference<>(new WindowCounter(currentWindow(), 0)));

        while (true) {
            WindowCounter current = ref.get();
            long now = currentWindow();

            if (now != current.windowStart) {
                // New window — reset counter
                WindowCounter fresh = new WindowCounter(now, 1);
                if (ref.compareAndSet(current, fresh)) return true;
                // CAS failed, retry
                continue;
            }

            if (current.count >= config.getMaxRequests()) {
                return false;
            }

            WindowCounter incremented = new WindowCounter(current.windowStart, current.count + 1);
            if (ref.compareAndSet(current, incremented)) return true;
            // CAS failed, retry
        }
    }

    private long currentWindow() {
        return System.currentTimeMillis() / config.getWindowSizeMs();
    }

    /** Immutable snapshot of a window's state — safe for CAS operations. */
    private static class WindowCounter {
        final long windowStart;
        final int count;

        WindowCounter(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }
}
