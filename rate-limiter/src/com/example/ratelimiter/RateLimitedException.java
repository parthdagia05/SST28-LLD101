package com.example.ratelimiter;

/**
 * Thrown when a rate limit is exceeded.
 */
public class RateLimitedException extends RuntimeException {
    public RateLimitedException(String key) {
        super("Rate limit exceeded for key: " + key);
    }
}
