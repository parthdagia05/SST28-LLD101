package com.example.ratelimiter;

/**
 * Demonstrates the pluggable rate limiting system.
 *
 * Scenario: Customer T1 is allowed 5 external calls per minute.
 * - Some requests don't need external calls (no rate check).
 * - Requests needing external calls are rate-limited.
 * - After quota is exhausted, external calls are denied gracefully.
 * - Switching algorithm requires only changing the factory call.
 */
public class App {

    public static void main(String[] args) {
        RateLimitConfig config = new RateLimitConfig(5, 60_000); // 5 per minute

        System.out.println("========================================");
        System.out.println(" Demo 1: Fixed Window Rate Limiter");
        System.out.println(" Config: " + config);
        System.out.println("========================================\n");
        runDemo(RateLimiterFactory.create(RateLimiterFactory.Algorithm.FIXED_WINDOW, config));

        System.out.println("\n========================================");
        System.out.println(" Demo 2: Sliding Window Rate Limiter");
        System.out.println(" Config: " + config);
        System.out.println("========================================\n");
        runDemo(RateLimiterFactory.create(RateLimiterFactory.Algorithm.SLIDING_WINDOW, config));
    }

    private static void runDemo(RateLimiter rateLimiter) {
        ExternalResourceClient externalClient = new ExternalResourceClient();
        InternalService service = new InternalService(rateLimiter, externalClient);

        String customer = "T1";

        // Phase 1: Requests that don't need external calls (no rate limiting)
        System.out.println("--- Phase 1: Requests without external calls (not rate limited) ---");
        for (int i = 1; i <= 3; i++) {
            String result = service.handleRequest(customer, "local-req-" + i, false);
            System.out.println("  Result: " + result + "\n");
        }

        // Phase 2: Requests that need external calls (rate limited)
        System.out.println("--- Phase 2: Requests with external calls (rate limited, limit=5) ---");
        for (int i = 1; i <= 7; i++) {
            try {
                String result = service.handleRequest(customer, "ext-req-" + i, true);
                System.out.println("  Result: " + result + "\n");
            } catch (RateLimitedException e) {
                System.out.println("  DENIED: " + e.getMessage() + "\n");
            }
        }

        // Phase 3: Different customer — independent quota
        System.out.println("--- Phase 3: Different customer T2 (independent quota) ---");
        try {
            String result = service.handleRequest("T2", "ext-req-1", true);
            System.out.println("  Result: " + result + "\n");
        } catch (RateLimitedException e) {
            System.out.println("  DENIED: " + e.getMessage() + "\n");
        }
    }
}
