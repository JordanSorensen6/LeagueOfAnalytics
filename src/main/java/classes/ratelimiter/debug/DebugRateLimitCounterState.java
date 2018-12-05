package classes.ratelimiter.debug;

/**
 * Copyright 2017 Riot Games, Inc.
 * DebugRateLimitCounterState stores the values of a RateLimit at the time of the request.
 */

public class DebugRateLimitCounterState {
    private long count;
    private long max;
    private long duration;
    private long expiry;

    public DebugRateLimitCounterState(long count, long max, long duration, long expiry) {
        this.count = count;
        this.max = max;
        this.duration = duration;
        this.expiry = expiry;
    }

    public long getCount() {
        return count;
    }

    public long getMax() {
        return max;
    }

    public long getDuration() {
        return duration;
    }

    public long getExpiry() {
        return expiry;
    }

    @Override
    public String toString() {
        return "DebugRateLimitCounterState{" +
                "count=" + count +
                ", max=" + max +
                ", duration=" + duration +
                ", expiry=" + expiry +
                '}';
    }
}
