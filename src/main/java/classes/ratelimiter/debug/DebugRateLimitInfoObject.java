package classes.ratelimiter.debug;

import java.util.List;

/**
 * Copyright 2017 Riot Games, Inc.
 */

public class DebugRateLimitInfoObject {
    private long threadId;
    private String uri;
    private int responseCode;
    private String retryAfterHeaders;
    private String rateLimitTypeHeaders;
    private String appRateLimitCountHeaders;
    private String appRateLimitHeaders;
    private String methodRateLimitCountHeaders;
    private String methodRateLimitHeaders;
    private List<DebugRateLimitCounterState> appRateLimitCounterStateList;
    private List<DebugRateLimitCounterState> methodRateLimitCounterStateList;

    public DebugRateLimitInfoObject() {
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getRetryAfterHeaders() {
        return retryAfterHeaders;
    }

    public void setRetryAfterHeaders(String retryAfterHeaders) {
        this.retryAfterHeaders = retryAfterHeaders;
    }

    public String getRateLimitTypeHeaders() {
        return rateLimitTypeHeaders;
    }

    public void setRateLimitTypeHeaders(String rateLimitTypeHeaders) {
        this.rateLimitTypeHeaders = rateLimitTypeHeaders;
    }

    public String getAppRateLimitCountHeaders() {
        return appRateLimitCountHeaders;
    }

    public void setAppRateLimitCountHeaders(String appRateLimitCountHeaders) {
        this.appRateLimitCountHeaders = appRateLimitCountHeaders;
    }

    public String getAppRateLimitHeaders() {
        return appRateLimitHeaders;
    }

    public void setAppRateLimitHeaders(String appRateLimitHeaders) {
        this.appRateLimitHeaders = appRateLimitHeaders;
    }

    public String getMethodRateLimitCountHeaders() {
        return methodRateLimitCountHeaders;
    }

    public void setMethodRateLimitCountHeaders(String methodRateLimitCountHeaders) {
        this.methodRateLimitCountHeaders = methodRateLimitCountHeaders;
    }

    public String getMethodRateLimitHeaders() {
        return methodRateLimitHeaders;
    }

    public void setMethodRateLimitHeaders(String methodRateLimitHeaders) {
        this.methodRateLimitHeaders = methodRateLimitHeaders;
    }

    public List<DebugRateLimitCounterState> getAppRateLimitCounterStateList() {
        return appRateLimitCounterStateList;
    }

    public void setAppRateLimitCounterStateList(List<DebugRateLimitCounterState> appRateLimitCounterStateList) {
        this.appRateLimitCounterStateList = appRateLimitCounterStateList;
    }

    public List<DebugRateLimitCounterState> getMethodRateLimitCounterStateList() {
        return methodRateLimitCounterStateList;
    }

    public void setMethodRateLimitCounterStateList(List<DebugRateLimitCounterState> methodRateLimitCounterStateList) {
        this.methodRateLimitCounterStateList = methodRateLimitCounterStateList;
    }
}
