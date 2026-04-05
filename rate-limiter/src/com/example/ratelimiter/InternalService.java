package com.example.ratelimiter;

/**
 * Internal service that handles client API requests.
 *
 * Business logic decides whether an external call is needed.
 * Rate limiting is applied ONLY before the external resource call,
 * not on every incoming request.
 */
public class InternalService {
    private final RateLimiter rateLimiter;
    private final ExternalResourceClient externalClient;

    public InternalService(RateLimiter rateLimiter, ExternalResourceClient externalClient) {
        this.rateLimiter = rateLimiter;
        this.externalClient = externalClient;
    }

    /**
     * Processes a client request.
     * @param customerId used as the rate limiting key
     * @param request the request payload
     * @param needsExternalCall business logic flag — only some requests need external calls
     */
    public String handleRequest(String customerId, String request, boolean needsExternalCall) {
        System.out.println("[Service] Handling request '" + request + "' for customer " + customerId);

        // Business logic that always runs
        String result = "processed:" + request;

        if (!needsExternalCall) {
            System.out.println("  [Service] No external call needed — returning local result");
            return result;
        }

        // Rate limit check only when external call is needed
        if (!rateLimiter.tryConsume(customerId)) {
            System.out.println("  [Service] RATE LIMITED — external call denied for " + customerId);
            throw new RateLimitedException(customerId);
        }

        System.out.println("  [Service] Rate limit OK — calling external resource");
        String externalResult = externalClient.call(request);
        return result + "|external:" + externalResult;
    }
}
