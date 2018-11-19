package classes.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Copyright 2017 Riot Games, Inc.
 * AtomicLongRateLimit uses an atomic counter to ensure the rate limit count is accurate in multi-threaded applications.
 */

public class AtomicLongRateLimit implements RateLimit {
    // We start our rate limit counter before the API actually rate limits us, so we define a safety buffer to try to
    // avoid resetting the bucket before it actually expires.
    private static final long SAFETY_BUFFER_IN_MS = 500L;

    private AtomicLong rateLimitCount;
    private long rateLimitMax;
    private long rateLimitStartTimestamp;
    private long rateLimitDuration;

    public AtomicLongRateLimit(long rateLimitCount, long rateLimitMax, long rateLimitStartTimestamp, long rateLimitDuration) {
        this.rateLimitCount = new AtomicLong(rateLimitCount);
        this.rateLimitMax = rateLimitMax;
        this.rateLimitStartTimestamp = rateLimitStartTimestamp;
        this.rateLimitDuration = rateLimitDuration;
    }

    public long getRateLimitCount() {
        resetIfBucketExpired();
        return rateLimitCount.get();
    }

    public void setRateLimitCount(long rateLimitCount) {
        this.rateLimitCount.set(rateLimitCount);
    }

    public long increaseRateLimitCount() {
        resetIfBucketExpired();
        long count = rateLimitCount.incrementAndGet();
        if(count == 1) {
            // When the count is 1 we can assume we've started a new bucket and can set the start timestamp.
            rateLimitStartTimestamp = System.currentTimeMillis();
        }
        return count;
    }

    public long decreaseRateLimitCount() {
        if (resetIfBucketExpired()) {
            return 0;
        }
        return rateLimitCount.decrementAndGet();
    }

    public long getRateLimitMax() {
        return rateLimitMax;
    }

    public void setRateLimitMax(long rateLimitMax) {
        this.rateLimitMax = rateLimitMax;
    }

    public long getRateLimitStartTimestamp() {
        return rateLimitStartTimestamp;
    }

    public void setRateLimitStartTimestamp(long rateLimitStartTimestamp) {
        this.rateLimitStartTimestamp = rateLimitStartTimestamp;
    }

    public long getRateLimitDuration() {
        return rateLimitDuration;
    }

    public void setRateLimitDuration(long rateLimitDuration) {
        this.rateLimitDuration = rateLimitDuration;
    }

    @Override
    public long getExpiry() {
        return rateLimitStartTimestamp + (rateLimitDuration * 1000) + SAFETY_BUFFER_IN_MS;
    }

    private synchronized boolean resetIfBucketExpired() {
        if(System.currentTimeMillis() > getExpiry()) {
            // We're past the expiry timestamp for the bucket so we can reset it even if we don't start using it yet.
            rateLimitCount.set(0);
            this.rateLimitStartTimestamp = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
