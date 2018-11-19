package classes.ratelimiter;

import org.apache.http.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2017 Riot Games, Inc.
 */

public class RateLimitHeaderUtils {

    public static Map<Long, Long> parseRateLimitPairsFromHeaders(Header[] rateLimitHeaderArr) {
        Map<Long, Long> durationToValueMap = new HashMap<>();
        for (Header header : rateLimitHeaderArr) {
            String[] rateLimitSplitArr = header.getValue().split(",");
            for (String rateLimitPairString : rateLimitSplitArr) {
                String[] rateLimitSplit = rateLimitPairString.split(":");
                long value = Long.valueOf(rateLimitSplit[0]);
                long rateLimitDuration = Long.valueOf(rateLimitSplit[1]);
                durationToValueMap.put(rateLimitDuration, value);
            }
        }
        return durationToValueMap;
    }
}

