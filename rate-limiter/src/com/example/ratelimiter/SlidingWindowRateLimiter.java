package com.example.ratelimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Sliding Window Counter algorithm.
 *
 * Maintains a deque of timestamps per key. On each check, it removes expired
 * entries and counts remaining ones against the limit.
 *
 * Trade-offs:
 * + Smooth rate limiting — no boundary spike problem
 * + Accurate: counts only requests within the exact trailing window
 * - Higher memory usage: stores one timestamp per request per key
 * - Cleanup cost: O(expired) per call (amortized)
 */
public class SlidingWindowRateLimiter implements RateLimiter {
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, ConcurrentLinkedDeque<Long>> timestamps;

    public SlidingWindowRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.timestamps = new ConcurrentHashMap<>();
    }

    @Override
    public boolean tryConsume(String key) {
        ConcurrentLinkedDeque<Long> deque = timestamps.computeIfAbsent(
                key, k -> new ConcurrentLinkedDeque<>());

        long now = System.currentTimeMillis();
        long windowStart = now - config.getWindowSizeMs();

        // Evict expired timestamps from the front
        while (!deque.isEmpty()) {
            Long oldest = deque.peekFirst();
            if (oldest != null && oldest <= windowStart) {
                deque.pollFirst();
            } else {
                break;
            }
        }

        // Check if under limit. Note: slight over-count possible under extreme
        // concurrency, but acceptable for an in-memory LLD exercise.
        if (deque.size() >= config.getMaxRequests()) {
            return false;
        }

        deque.addLast(now);
        return true;
    }
}
