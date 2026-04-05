package com.example.ratelimiter;

/**
 * Represents the paid external resource.
 * In a real system, this would make HTTP calls, SDK calls, etc.
 */
public class ExternalResourceClient {

    public String call(String request) {
        System.out.println("    [ExternalAPI] Called with: " + request);
        return "response_for_" + request;
    }
}
