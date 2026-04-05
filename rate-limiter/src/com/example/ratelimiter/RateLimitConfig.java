package com.example.ratelimiter;

/**
 * Configuration for rate limiting: max allowed requests within a time window.
 * Examples: 100 requests per 60000ms (1 minute), 1000 requests per 3600000ms (1 hour).
 */
public class RateLimitConfig {
    private final int maxRequests;
    private final long windowSizeMs;

    public RateLimitConfig(int maxRequests, long windowSizeMs) {
        if (maxRequests <= 0) throw new IllegalArgumentException("maxRequests must be positive");
        if (windowSizeMs <= 0) throw new IllegalArgumentException("windowSizeMs must be positive");
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    public int getMaxRequests() { return maxRequests; }
    public long getWindowSizeMs() { return windowSizeMs; }

    /** Convenience factory: e.g., RateLimitConfig.perMinute(100) */
    public static RateLimitConfig perMinute(int maxRequests) {
        return new RateLimitConfig(maxRequests, 60_000);
    }

    /** Convenience factory: e.g., RateLimitConfig.perHour(1000) */
    public static RateLimitConfig perHour(int maxRequests) {
        return new RateLimitConfig(maxRequests, 3_600_000);
    }

    @Override
    public String toString() {
        return maxRequests + " requests per " + windowSizeMs + "ms";
    }
}
