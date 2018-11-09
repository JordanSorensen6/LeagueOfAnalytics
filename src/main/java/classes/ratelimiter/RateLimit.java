package classes.ratelimiter;

/**
 * Copyright 2017 Riot Games, Inc.
 */

public interface RateLimit {
    long getRateLimitCount();

    long increaseRateLimitCount();

    long decreaseRateLimitCount();

    long getRateLimitMax();

    long getRateLimitDuration();

    long getExpiry();
}