package servlets;

import classes.LoadConfig;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class RiotAPI extends HttpServlet {
    private static LoadConfig config = LoadConfig.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String apiKey = config.getRiotApiKey();

        String uri = request.getRequestURI();
        if(uri.equals("/riot/champions")) {
            //construct request
            String url = "https://na1.api.riotgames.com/lol/static-data/v3/champions?locale=en_US&champListData=keys";
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet req = new HttpGet(url);
            req.addHeader("X-Riot-Token", apiKey);

            HttpResponse resp = client.execute(req);

            int status = resp.getStatusLine().getStatusCode();
            if(status == 200) {
                //retrieve champion id: name map
                BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while((line = rd.readLine()) != null) {
                    result.append(line);
                }
                JsonParser parser = new JsonParser();
                JsonObject o = parser.parse(result.toString()).getAsJsonObject();
                JsonObject keys = o.getAsJsonObject("keys");

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write("{\"keys\": " + keys.toString() + "}");
                writer.close();
            } else {
                //error response from riot's api
                System.out.println("Riot Status " + status + ", something broke :(");
            }
        } else if(Pattern.compile("^*/riot/summonerIds*$").matcher(uri).matches()) {
            //extract query params
            ArrayList<String> summoners = new ArrayList<>();
            summoners.add(request.getParameter("s1"));
            summoners.add(request.getParameter("s2"));
            summoners.add(request.getParameter("s3"));
            summoners.add(request.getParameter("s4"));
            summoners.add(request.getParameter("s5"));

            JsonObject allSummonersIds = new JsonObject();
            Pattern validName = Pattern.compile("^[0-9\\p{L} _\\.]+$");
            for(String summoner : summoners) {
                if(validName.matcher(summoner).matches()) {
                    String id = getSummonerId(apiKey, summoner.replaceAll("\\s+", ""));
                    allSummonersIds.addProperty(summoner, id);
                }
            }

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(allSummonersIds.toString());
            writer.close();
        } else if(Pattern.compile("^*/riot/championMastery*$").matcher(uri).matches()) {
            //extract query parameters
            String summonerId = request.getParameter("summonerId");
            String championId = request.getParameter("championId");
            String masteryLevel = getChampionMastery(apiKey, summonerId, championId);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(masteryLevel);
            writer.close();
        }
    }

    private String getSummonerId(String apiKey, String summoner) throws IOException {
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
            System.out.println("Riot Status " + status + ", something broke :(");
        }
        return null;
    }

    private String getChampionMastery(String apiKey, String summonerId, String championId) throws IOException {
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
            System.out.println("Riot Status " + status + ", something broke :(");
            return "0";
        }
    }
}