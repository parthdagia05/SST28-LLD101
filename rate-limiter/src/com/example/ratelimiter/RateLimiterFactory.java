package com.example.ratelimiter;

/**
 * Factory to create rate limiter instances by algorithm name.
 * Makes it easy to switch algorithms via configuration.
 */
public class RateLimiterFactory {

    public enum Algorithm {
        FIXED_WINDOW,
        SLIDING_WINDOW
    }

    public static RateLimiter create(Algorithm algorithm, RateLimitConfig config) {
        switch (algorithm) {
            case FIXED_WINDOW:
                return new FixedWindowRateLimiter(config);
            case SLIDING_WINDOW:
                return new SlidingWindowRateLimiter(config);
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }
    }
}
