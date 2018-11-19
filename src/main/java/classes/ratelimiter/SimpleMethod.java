package classes.ratelimiter;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2017 Riot Games, Inc.
 */

public class SimpleMethod {
    private String endpoint;
    private Map<RateLimitType, RateLimitSet> rateLimitSetMap;

    // This constructor should only be used when a method doesn't enforce an application rate limit.
    // Static-data is the only API which currently utilizes this constructor.
    public SimpleMethod(String endpoint) {
        this(endpoint, null);
    }

    public SimpleMethod(String endpoint, RateLimitSet applicationRateLimitSet) {
        this.endpoint = endpoint;
        this.rateLimitSetMap = new HashMap<>();
        this.rateLimitSetMap.put(RateLimitType.APPLICATION, applicationRateLimitSet);

        // Create a default method rate limit of 1 call per 10 seconds.
        // This will get replaced when we sync the method rate limits after the first API call.
        RateLimitSet initialMethodRateLimitSet = new RateLimitSet(RateLimitType.METHOD);
        initialMethodRateLimitSet.putRateLimit(new AtomicLongRateLimit(0, 1, System.currentTimeMillis(), 10));
        this.rateLimitSetMap.put(RateLimitType.METHOD, initialMethodRateLimitSet);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public RateLimitSet getMethodRateLimitSet() {
        return rateLimitSetMap.get(RateLimitType.METHOD);
    }

    public RateLimitSet getAppRateLimitSet() {
        return rateLimitSetMap.get(RateLimitType.APPLICATION);
    }
}
