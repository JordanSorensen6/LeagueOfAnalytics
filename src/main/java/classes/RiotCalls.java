package classes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RiotCalls {
    private static LoadConfig config = LoadConfig.getInstance();

    public String getSummonerId(String summoner) throws IOException {
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summoner;
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
            JsonParser parser = new JsonParser();
            JsonObject summonerDTO = parser.parse(result.toString()).getAsJsonObject();
            return summonerDTO.get("id").toString();
        } else {
            System.out.println("Riot Status " + status + ", Summoner by name error");
        }
        return null;
    }

    public String getAccountId(String summoner) throws IOException {
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summoner;
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
            JsonParser parser = new JsonParser();
            JsonObject summonerDTO = parser.parse(result.toString()).getAsJsonObject();
            return summonerDTO.get("accountId").toString();
        } else {
            System.out.println("Riot Status " + status + ", Summoner by name error");
        }
        return null;
    }

    public String getSummonerName(String summoner) throws IOException {
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + summoner;
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
            JsonParser parser = new JsonParser();
            JsonObject summonerDTO = parser.parse(result.toString()).getAsJsonObject();
            return summonerDTO.get("name").getAsString();
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
        }
        else {
            System.out.println("Riot Status " + status + ", Champion mastery error");
            return "0";
        }
    }

    public JsonArray getRecentMatches(String summonerId) throws IOException {
        String apiKey = config.getRiotApiKey();
        String url = "https://na1.api.riotgames.com/lol/match/v3/matchlists/by-account/"
                + summonerId + "/recent";
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
            JsonParser parser = new JsonParser();
            return parser.parse(result.toString()).getAsJsonObject().get("matches").getAsJsonArray();
        } else if (status == 403) {
            //no recent matches
            return null;
        }
        else {
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
        }
        else {
            System.out.println("Riot Status " + status + ", matches by matchId error");
            return null;
        }
    }
}
