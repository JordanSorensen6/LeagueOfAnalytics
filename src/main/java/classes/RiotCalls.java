package classes;

import com.google.common.util.concurrent.RateLimiter;
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
import java.util.regex.Pattern;

public class RiotCalls {

    private static RiotCalls riotCalls = null;

    private LoadConfig config;

    //TODO implement advanced rate limiter using example given by Riot
    private static final double rate = 100.0 / 140.0;
    private RateLimiter rateLimiter;

    private RiotCalls() {
        config = LoadConfig.getInstance();
        rateLimiter = RateLimiter.create(rate);
    }

    public static RiotCalls getInstance() {
        if(riotCalls == null) {
            riotCalls = new RiotCalls();
        }
        return riotCalls;
    }

    public String getSummonerId(String summoner) throws IOException {
        summoner = formatSummonerName(summoner);
        String apiKey = config.getRiotApiKey();
        Pattern validName = Pattern.compile("^[0-9\\p{L} _\\.]+$");
        // if name is invalid
        if(!validName.matcher(summoner).matches()){
            return null;
        }
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summoner;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);

        this.rateLimiter.acquire(1);
        HttpResponse resp = client.execute(req);

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
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/league/v3/positions/by-summoner/"+summonerID;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);
        HttpResponse resp = client.execute(req);
        int status = resp.getStatusLine().getStatusCode();
        if (status == 200) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }


            return result.toString();
//            Gson g = new Gson();
//            String jsonRepresentation = g.toJson(result.toString());
//            JsonParser parser = new JsonParser();
//            JsonObject jobj = parser.parse(result.toString()).getAsJsonObject();
//            return jobj.toString();
        }
        else {
            System.out.println("Riot Status " + status + ", https://na1.api.riotgames.com/lol/league/v3/positions/by-summoner/");
        }
        return null;
    }

    public String getAccountId(String summoner) throws IOException {
        summoner = formatSummonerName(summoner);
        String apiKey = config.getRiotApiKey();
        Pattern validName = Pattern.compile("^[0-9\\p{L} _\\.]+$");
        // if name is invalid
        if(!validName.matcher(summoner).matches()){
            return null;
        }
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summoner;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);

        this.rateLimiter.acquire(1);
        HttpResponse resp = client.execute(req);

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
            return summonerDTO.get("accountId").toString();
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", Summoner by name error");
        }
        return null;
    }

    public String getSummonerName(String summoner) throws IOException {
        summoner = formatSummonerName(summoner);
        String apiKey = config.getRiotApiKey();
        Pattern validName = Pattern.compile("^[0-9\\p{L} _\\.]+$");
        // if name is invalid
        if(!validName.matcher(summoner).matches()){
            return null;
        }
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summoner;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);

        this.rateLimiter.acquire(1);
        HttpResponse resp = client.execute(req);

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
            return summonerDTO.get("name").getAsString();
        } else if (status == 429) {
            System.out.println("WARNING: API Limit Exceeded, try again in " + rateLimitExceeded(resp) + " seconds");
            return null;
        } else {
            System.out.println("Riot Status " + status + ", Summoner by name error");
        }
        return null;
    }

    public String getChampionMastery(String summonerId, String championId) throws IOException {
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/champion-mastery/v3/champion-masteries/by-summoner/"
                + summonerId + "/by-champion/" + championId;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);

        this.rateLimiter.acquire(1);
        HttpResponse resp = client.execute(req);

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
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/"
                + accountId + "?beginIndex=" + beginIndex + "&endIndex=" + endIndex + "&queue=420&queue=440"; //420 and 440 filters the matchlist to only ranked matches
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);

        this.rateLimiter.acquire(1);
        HttpResponse resp = client.execute(req);

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
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/match/v3/matches/" + matchId;
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet req = new HttpGet(url);
        req.addHeader("X-Riot-Token", apiKey);

        this.rateLimiter.acquire(1);
        HttpResponse resp = client.execute(req);

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
}
