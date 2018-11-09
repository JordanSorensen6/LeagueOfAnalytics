package classes.ratelimiter;

public final class ApiConstants {
    public static final String METHOD_RATE_LIMIT_COUNT_HEADER = "X-Method-Rate-Limit-Count";
    public static final String METHOD_RATE_LIMIT_HEADER = "X-Method-Rate-Limit";
    public static final String APP_RATE_LIMIT_COUNT_HEADER = "X-App-Rate-Limit-Count";
    public static final String APP_RATE_LIMIT_HEADER = "X-App-Rate-Limit";
    public static final String RATE_LIMIT_TYPE_HEADER = "X-Rate-Limit-Type";
    public static final String RETRY_AFTER_HEADER = "Retry-After";

    public static final long API_CALL_SLEEP_TIME_MS = 10;
}
