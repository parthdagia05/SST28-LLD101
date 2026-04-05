package com.example.ratelimiter;

/**
 * Core interface for all rate limiting algorithms.
 * Internal services call tryConsume() before making an external resource call.
 */
public interface RateLimiter {
    /**
     * Attempts to consume one unit of quota for the given key.
     * @param key the rate limiting key (e.g., customerId, tenantId, apiKey)
     * @return true if the call is allowed, false if rate limit is exceeded
     */
    boolean tryConsume(String key);
}
