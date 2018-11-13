package classes;

import classes.ratelimiter.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RiotCalls {

    private static RiotCalls riotCalls = null;

    private LoadConfig config;

    private RateLimitSet appRateLimitSet;
    private SimpleMethod summoners;
    private SimpleMethod league;
    private SimpleMethod championMastery;
    private SimpleMethod match;
    private SimpleMethod matchlist;

    private RiotCalls() {
        config = LoadConfig.getInstance();

        appRateLimitSet = new RateLimitSet(RateLimitType.APPLICATION);
        appRateLimitSet.putRateLimit(new AtomicLongRateLimit(0, 1, System.currentTimeMillis(), 10));
        summoners = new SimpleMethod("https://na1.api.riotgames.com/lol/summoner/v3/", appRateLimitSet);
        league = new SimpleMethod("https://na1.api.riotgames.com/lol/league/v3/", appRateLimitSet);
        championMastery = new SimpleMethod("https://na1.api.riotgames.com/lol/champion-mastery/v3/", appRateLimitSet);
        match = new SimpleMethod("https://na1.api.riotgames.com/lol/match/v3/", appRateLimitSet);
        matchlist = new SimpleMethod("https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/", appRateLimitSet);
    }

    public static RiotCalls getInstance() {
        if(riotCalls == null) {
            riotCalls = new RiotCalls();
        }
        return riotCalls;
    }

    public String getSummonerId(String summoner) throws IOException {
        summoner = formatSummonerName(summoner);
        Pattern validName = Pattern.compile("^[0-9\\p{L} _\\.]+$");
        // if name is invalid
        if(!validName.matcher(summoner).matches()){
            return null;
        }

        HttpResponse resp = handleAPICall(summoners, "summoners/by-name/" + summoner);
        if(resp == null) { //error occurred so try again
            resp = handleAPICall(summoners, "summoners/by-name/" + summoner);
            if(resp == null) {
                System.out.println("Error occurred when handling api request for endpoint: " + summoners.getEndpoint());
                return null;
            }
        }

        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JsonParser parser = new JsonParser();
            JsonObject summonerDTO = parser.parse(result.toString()).getAsJsonObject();
            return summonerDTO.get("id").toString();
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", Summoner by name error");
        }
        return null;
    }

    public String getSummonerInfo(String summonerID) throws IOException
    {
        HttpResponse resp = handleAPICall(league, "positions/by-summoner/" + summonerID);
        if(resp == null) { //error occurred so try again
            resp = handleAPICall(league, "positions/by-summoner/" + summonerID);
            if(resp == null) {
                System.out.println("Error occurred when handling api request for endpoint: " + league.getEndpoint());
                return null;
            }
        }

        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            return result.toString();
        }
        else {
            System.out.println("Riot Status " + status + ", https://na1.api.riotgames.com/lol/league/v3/positions/by-summoner/");
        }
        return null;
    }

    private JsonObject getAccountInfo(String summoner) throws IOException {
        summoner = formatSummonerName(summoner);
        String apiKey = config.getRiotApiKey();
        Pattern validName = Pattern.compile("^[0-9\\p{L} _\\.]+$");
        // if name is invalid
        if(!validName.matcher(summoner).matches()){
            return null;
        }

        HttpResponse resp = handleAPICall(summoners, "summoners/by-name/" + summoner);
        if(resp == null) { //error occurred so try again
            resp = handleAPICall(summoners, "summoners/by-name/" + summoner);
            if(resp == null) {
                System.out.println("Error occurred when handling api request for endpoint: " + summoners.getEndpoint());
                return null;
            }
        }

        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JsonParser parser = new JsonParser();
            JsonObject summonerDTO = parser.parse(result.toString()).getAsJsonObject();
            return summonerDTO;
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", Summoner by name error");
        }
        return null;
    }

    public String getAccountId(String summoner) throws IOException {
        JsonObject accountInfo = getAccountInfo(summoner);
        if(accountInfo != null)
            return accountInfo.get("accountId").toString();
        return null;
    }

    public String getSummonerName(String summoner) throws IOException {
        JsonObject accountInfo = getAccountInfo(summoner);
        if(accountInfo != null)
            return accountInfo.get("name").getAsString();
        return null;
    }

    public String getChampionMastery(String summonerId, String championId) throws IOException {

        HttpResponse resp = handleAPICall(
                championMastery,
                "champion-masteries/by-summoner/" + summonerId + "/by-champion/" + championId
        );
        if(resp == null) { //error occurred so try again
            resp = handleAPICall(
                    championMastery,
                    "champion-masteries/by-summoner/" + summonerId + "/by-champion/" + championId
            );
            if(resp == null) {
                System.out.println("Error occurred when handling api request for endpoint: " + championMastery.getEndpoint());
                return null;
            }
        }

        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JsonParser parser = new JsonParser();
            JsonObject mastery = parser.parse(result.toString()).getAsJsonObject();
            return mastery.getAsJsonPrimitive("championLevel").toString();
        } else if (status == 403) {
            //no data found, so level is 0
            return "0";
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", Champion mastery error");
            return "0";
        }
    }

    public ArrayList<String> getRecentMatchIds(String accountId, Integer beginIndex, Integer endIndex) throws IOException {

        HttpResponse resp = handleAPICall(
                matchlist,
                accountId + "?beginIndex=" + beginIndex + "&endIndex=" + endIndex + "&queue=420&queue=440"
        );//420 and 440 filters the matchlist to only ranked matches
        if(resp == null) { //error occurred so try again
            resp = handleAPICall(
                    matchlist,
                    accountId + "?beginIndex=" + beginIndex + "&endIndex=" + endIndex + "&queue=420&queue=440"
            );
            if(resp == null) {
                System.out.println("Error occurred when handling api request for endpoint: " + matchlist.getEndpoint());
                return null;
            }
        }

        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JsonParser parser = new JsonParser();
            JsonArray matches = parser.parse(result.toString()).getAsJsonObject().get("matches").getAsJsonArray();
            ArrayList<String> matchIds = new ArrayList<>();
            for(int i = 0; i < matches.size(); i++) {
                matchIds.add(matches.get(i).getAsJsonObject().get("gameId").getAsString());
            }
            return matchIds;
        } else if (status == 403) {
            //no recent matches
            return null;
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", recent matchlist by account error");
            return null;
        }
    }

    public JsonObject getMatch(String matchId) throws IOException {

        HttpResponse resp = handleAPICall(match, "matches/" + matchId);
        if(resp == null) { //error occurred so try again
            resp = handleAPICall(match, "matches/" + matchId);
            if(resp == null) {
                System.out.println("Error occurred when handling api request for endpoint: " + match.getEndpoint());
                return null;
            }
        }

        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JsonParser parser = new JsonParser();
            return parser.parse(result.toString()).getAsJsonObject();
        } else if (status == 403) {
            //no data found, so level is 0
            return null;
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", matches by matchId error");
            return null;
        }
    }

    private String formatSummonerName(String name) {
        return name.replaceAll("\\s+","");
    }

    private Integer rateLimitExceeded(HttpResponse resp) {
        Header[] headers = resp.getAllHeaders();
        for(Header header : headers) {
            if (header.getName().equals("Retry-After")) {
                return Integer.parseInt(header.getValue());
            }
        }
        return 0;
    }

    private HttpResponse handleAPICall(SimpleMethod method, String args) {
        try {
            while (true) {
                if (isMethodCallExecutableNow(method)) {
                    if (method.getAppRateLimitSet() != null) {
                        if (method.getAppRateLimitSet().incrementRateLimitCounts().isEmpty()) {
                            // if empty, we are rate limited so wait
                            method.getAppRateLimitSet().decrementRateLimitCounts();
                            Thread.sleep(ApiConstants.API_CALL_SLEEP_TIME_MS);
                            continue;
                        }
                    }
                    if (method.getMethodRateLimitSet() != null) {
                        if (method.getMethodRateLimitSet().incrementRateLimitCounts().isEmpty()) {
                            // if empty, we are rate limited so wait
                            method.getMethodRateLimitSet().decrementRateLimitCounts();
                            Thread.sleep(ApiConstants.API_CALL_SLEEP_TIME_MS);
                            continue;
                        }
                    }
                    // Make the api call
                    String url = method.getEndpoint() + args;
                    String apiKey = config.getRiotApiKey();

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet req = new HttpGet(url);
                    req.addHeader("X-Riot-Token", apiKey);

                    HttpResponse resp = client.execute(req);

                    //sync headers
                    if (summoners.getAppRateLimitSet() != null) {
                        syncRateLimitsFromHeaders(
                                resp.getHeaders(ApiConstants.APP_RATE_LIMIT_HEADER),
                                resp.getHeaders(ApiConstants.APP_RATE_LIMIT_COUNT_HEADER),
                                summoners.getAppRateLimitSet()
                        );
                    }
                    if (summoners.getMethodRateLimitSet() != null) {
                        syncRateLimitsFromHeaders(
                                resp.getHeaders(ApiConstants.METHOD_RATE_LIMIT_HEADER),
                                resp.getHeaders(ApiConstants.METHOD_RATE_LIMIT_COUNT_HEADER),
                                summoners.getMethodRateLimitSet()
                        );
                    }
                    syncRetryAfterFromHeaders(
                            resp.getHeaders(ApiConstants.RETRY_AFTER_HEADER),
                            resp.getHeaders(ApiConstants.RATE_LIMIT_TYPE_HEADER),
                            summoners
                    );
                    return resp;

                } else {
                    Thread.sleep(ApiConstants.API_CALL_SLEEP_TIME_MS);
                }
            }
        } catch(Exception e) { //InterruptedException and IOException
            e.printStackTrace();
            return null;
        }
    }

    // Checks if a SimpleMethod is able to be executed right now.
    private static boolean isMethodCallExecutableNow(SimpleMethod method) {
        boolean isNotMethodRateLimited = method.getAppRateLimitSet() == null;
        boolean isUnderMethodRateLimits = isNotMethodRateLimited || method.getMethodRateLimitSet().isUnderRateLimits();
        boolean isNowAfterMethodsRetryAfterTimestamp = isNotMethodRateLimited || (System.currentTimeMillis() > method.getMethodRateLimitSet().getRetryAfterTimestamp());

        boolean isNotApplicationRateLimited = method.getAppRateLimitSet() == null;
        boolean isUnderApplicationRateLimits = isNotApplicationRateLimited || method.getAppRateLimitSet().isUnderRateLimits();
        boolean isNowAfterApplicationsRetryAfterTimestamp = isNotApplicationRateLimited || (System.currentTimeMillis() > method.getAppRateLimitSet().getRetryAfterTimestamp());

        // If we're not method rate limited, or if we're still below rate limits and after any Retry-After timestamps.
        return (isNotMethodRateLimited || (isUnderMethodRateLimits && isNowAfterMethodsRetryAfterTimestamp)) &&
                // If we're not application rate limited, or if we're still below rate limits and after any Retry-After timestamps.
                (isNotApplicationRateLimited || (isUnderApplicationRateLimits && isNowAfterApplicationsRetryAfterTimestamp));
    }

    private static void syncRateLimitsFromHeaders(Header[] rateLimitHeaderArr,
                                                  Header[] rateLimitCountHeaderArr,
                                                  RateLimitSet rateLimitSet) {

        if(rateLimitHeaderArr.length < 1 || rateLimitCountHeaderArr.length < 1) {
            if(rateLimitSet != null) {
                // If we don't get the rate limit headers back for a request with a valid response, it means we are not
                // rate limited on this endpoint and can put back the rate limit we consumed.
                rateLimitSet.decrementRateLimitCounts();
            }
            return;
        }
        Map<Long, Long> rateLimitDurationToCountMap = RateLimitHeaderUtils.parseRateLimitPairsFromHeaders(rateLimitCountHeaderArr);
        Map<Long, Long> rateLimitDurationToMaxMap = RateLimitHeaderUtils.parseRateLimitPairsFromHeaders(rateLimitHeaderArr);
        List<RateLimit> survivingRateLimits = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : rateLimitDurationToCountMap.entrySet()) {
            long rateLimitDuration = entry.getKey();
            long rateLimitCount = entry.getValue();
            long rateLimitMax = rateLimitDurationToMaxMap.get(rateLimitDuration);
            AtomicLongRateLimit rateLimit = (AtomicLongRateLimit) rateLimitSet.getRateLimit(Long.valueOf(rateLimitDuration));
            if (rateLimit == null) {
                rateLimit = new AtomicLongRateLimit(Long.valueOf(rateLimitCount), rateLimitMax, System.currentTimeMillis(), rateLimitDuration);
            } else if (rateLimit.getRateLimitMax() != rateLimitMax) {
                rateLimit.setRateLimitMax(rateLimitMax);
            } else if (rateLimitCount == 1) {
                rateLimit.setRateLimitStartTimestamp(System.currentTimeMillis());
            }
            survivingRateLimits.add(rateLimit);
        }
        rateLimitSet.rebuildRateLimitSet(survivingRateLimits);
    }

    private static void syncRetryAfterFromHeaders(Header[] retryAfterHeaderArr,
                                                  Header[] rateLimitTypeHeaderArr,
                                                  SimpleMethod method) {

        if (retryAfterHeaderArr.length > 0 && rateLimitTypeHeaderArr.length > 0) {
            String rateLimitType = rateLimitTypeHeaderArr[0].getValue();
            for (Header header : retryAfterHeaderArr) {
                int retryAfter = Integer.valueOf(header.getValue());
                RateLimitSet rateLimitSet;
                if ("application".equalsIgnoreCase(rateLimitType)) {
                    rateLimitSet = method.getAppRateLimitSet();
                } else if ("method".equalsIgnoreCase(rateLimitType)) {
                    rateLimitSet = method.getMethodRateLimitSet();
                } else {
                    // If service rate limited, back off from this method.
                    rateLimitSet = method.getMethodRateLimitSet();
                }
                rateLimitSet.retryAfter(retryAfter);
            }
        }
    }
}
