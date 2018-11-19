package classes.ratelimiter;

import classes.ratelimiter.debug.DebugRateLimitCounterState;

import java.util.*;

/**
 * Copyright 2017 Riot Games, Inc.
 */

public class RateLimitSet {
    private RateLimitType rateLimitType;
    private volatile Map<Long, RateLimit> rateLimits;
    private long retryAfterTimestamp;

    public RateLimitSet(RateLimitType rateLimitType) {
        this.rateLimits = new HashMap<>();
        this.rateLimitType = rateLimitType;
        this.retryAfterTimestamp = 0L;
    }

    public RateLimit getRateLimit(long duration) {
        return rateLimits.get(duration);
    }

    public RateLimit putRateLimit(RateLimit rateLimit) {
        Map<Long, RateLimit> newRateLimits = new HashMap<>(rateLimits);
        RateLimit oldRateLimit = newRateLimits.put(rateLimit.getRateLimitDuration(), rateLimit);
        rateLimits = newRateLimits;
        return oldRateLimit;
    }

    public void clearRateLimits() {
        this.rateLimits = new HashMap<>();
    }

    // Create a list of DebugRateLimitCounterState which stores the counter values when we increment the rate limit
    // counts in the rate limiter. This method will return an empty list if any of the rate limits were exceeded.
    public List<DebugRateLimitCounterState> incrementRateLimitCounts() {
        List<DebugRateLimitCounterState> rateLimitCounterStateList = new ArrayList<>();
        boolean returnEmpty = false;
        for (RateLimit rateLimit : rateLimits.values()) {
            long count = rateLimit.increaseRateLimitCount();
            boolean exceededLimit = (count > rateLimit.getRateLimitMax());
            returnEmpty = returnEmpty || exceededLimit;
            rateLimitCounterStateList.add(
                    new DebugRateLimitCounterState(count,
                            rateLimit.getRateLimitMax(),
                            rateLimit.getRateLimitDuration(),
                            rateLimit.getExpiry())
            );
        }
        if (returnEmpty) {
            return new ArrayList<>();
        }
        return rateLimitCounterStateList;
    }

    // Create a list of DebugRateLimitCounterState which stores the counter values when we decrement the rate limit
    // counts in the rate limiter. This method will return an empty list if any of the rate limits were exceeded.
    public List<DebugRateLimitCounterState> decrementRateLimitCounts(){
        List<DebugRateLimitCounterState> rateLimitCounterStateList = new ArrayList<>();
        for (RateLimit rateLimit : rateLimits.values()) {
            long count = rateLimit.decreaseRateLimitCount();
            rateLimitCounterStateList.add(
                    new DebugRateLimitCounterState(count,
                            rateLimit.getRateLimitMax(),
                            rateLimit.getRateLimitDuration(),
                            rateLimit.getExpiry())
            );
        }
        return rateLimitCounterStateList;
    }

    public boolean isUnderRateLimits() {
        for (RateLimit rateLimit : rateLimits.values()) {
            if (rateLimit.getRateLimitCount() >= rateLimit.getRateLimitMax()) {
                return false;
            }
        }
        return true;
    }

    public long getRetryAfterTimestamp() {
        return retryAfterTimestamp;
    }

    public void retryAfter(int seconds) {
        setRetryAfterTimestamp(System.currentTimeMillis() + (seconds * 1000));
    }

    public void setRetryAfterTimestamp(long retryAfterTimestamp) {
        this.retryAfterTimestamp = retryAfterTimestamp;
    }

    public RateLimitType getRateLimitType() {
        return rateLimitType;
    }

    public void rebuildRateLimitSet(Collection<RateLimit> rateLimitCollection) {
        Map<Long, RateLimit> newRateLimitsMap = new HashMap<>();
        for(RateLimit rateLimit : rateLimitCollection) {
            newRateLimitsMap.put(rateLimit.getRateLimitDuration(), rateLimit);
        }
        rateLimits = newRateLimitsMap;
    }
}
