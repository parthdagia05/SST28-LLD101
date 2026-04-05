# Pluggable Rate Limiting System - Low Level Design

## Class Diagram

```
        ┌──────────────┐         ┌──────────────────┐
        │  Client API  │────────▶│  InternalService  │
        └──────────────┘         │──────────────────│
                                 │ - rateLimiter    │
                                 │ - externalClient │
                                 │──────────────────│
                                 │ + handleRequest()│
                                 └───┬──────────┬───┘
                                     │          │
                          if needed  │          │ if allowed
                                     ▼          ▼
                          ┌──────────────┐  ┌──────────────────────┐
                          │ «interface»  │  │ ExternalResourceClient│
                          │ RateLimiter  │  │──────────────────────│
                          │──────────────│  │ + call(request)      │
                          │+tryConsume() │  └──────────────────────┘
                          └──────┬───────┘
                                 │
                 ┌───────────────┼───────────────┐
                 ▼               ▼               ▼
    ┌────────────────────┐ ┌─────────────────────┐  ┌─────────────┐
    │FixedWindowRateLimiter│ │SlidingWindowRateLimiter│ │ Future:     │
    │────────────────────│ │─────────────────────│  │ TokenBucket │
    │- counters: Map     │ │- timestamps: Map    │  │ LeakyBucket │
    │  <key, WindowCtr>  │ │  <key, Deque<Long>> │  │ SlidingLog  │
    │────────────────────│ │─────────────────────│  └─────────────┘
    │+tryConsume(key)    │ │+tryConsume(key)     │
    └────────────────────┘ └─────────────────────┘

    ┌─────────────────┐       ┌──────────────────────┐
    │ RateLimitConfig │       │ RateLimiterFactory    │
    │─────────────────│       │──────────────────────│
    │ - maxRequests   │       │ + create(algorithm,  │
    │ - windowSizeMs  │       │         config)      │
    │─────────────────│       └──────────────────────┘
    │ + perMinute()   │
    │ + perHour()     │       ┌──────────────────────┐
    └─────────────────┘       │ RateLimitedException │
                              └──────────────────────┘
```

## Key Design Decisions

1. **Rate limiting at the external call boundary** — The `RateLimiter` is checked only when `InternalService` decides an external call is needed, not on every incoming request. This matches the requirement that rate limiting protects paid external resource usage.

2. **Strategy Pattern for algorithms** — `RateLimiter` is an interface. Swapping algorithms (Fixed Window → Sliding Window → Token Bucket) requires zero changes to `InternalService`. Just pass a different implementation.

3. **Key-based isolation** — `tryConsume(key)` accepts a string key, so the same limiter handles per-customer, per-tenant, or per-API-key limits depending on what key the caller passes.

4. **Thread safety** — `FixedWindowRateLimiter` uses `ConcurrentHashMap` + lock-free CAS via `AtomicReference` on immutable `WindowCounter` snapshots. `SlidingWindowRateLimiter` uses `ConcurrentHashMap` + `ConcurrentLinkedDeque`.

5. **Factory for algorithm selection** — `RateLimiterFactory` allows switching algorithms via config/enum without touching business logic.

## Algorithm Trade-offs

| Aspect | Fixed Window | Sliding Window |
|--------|-------------|----------------|
| **Memory** | O(1) per key (one counter) | O(N) per key (one timestamp per request) |
| **Time** | O(1) per check | O(expired) amortized cleanup |
| **Accuracy** | Boundary spike: up to 2x burst at window edges | Smooth: counts exact trailing window |
| **Simplicity** | Very simple | Moderate complexity |
| **Best for** | High-throughput, approximate limiting | Precise limiting on paid resources |

## How Cache Miss / Rate Limit Flow Works

```
Client Request
     │
     ▼
InternalService.handleRequest(customerId, request, needsExternalCall)
     │
     ├── Business logic runs (always)
     │
     ├── needsExternalCall == false?  →  Return local result (no rate check)
     │
     ├── rateLimiter.tryConsume(customerId)
     │       │
     │       ├── true  →  Call ExternalResourceClient  →  Return combined result
     │       │
     │       └── false →  Throw RateLimitedException (handled gracefully)
```
