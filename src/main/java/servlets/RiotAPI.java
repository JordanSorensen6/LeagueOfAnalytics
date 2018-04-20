package servlets;

import classes.LoadConfig;
import classes.RiotCalls;
import classes.StaticChampions;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class RiotAPI extends HttpServlet {
    private static LoadConfig config = LoadConfig.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RiotCalls call = new RiotCalls();
        String apiKey = config.getRiotApiKey();
        String dbUser = config.getDatabaseUser();
        String dbPass = config.getDatabasePass();

        String uri = request.getRequestURI();
        if(uri.equals("/riot/champions")) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(StaticChampions.champions);
            writer.close();
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
                    String id = call.getSummonerId(summoner.replaceAll("\\s+", ""));
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
            String masteryLevel = call.getChampionMastery(summonerId, championId);

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(masteryLevel);
            writer.close();
        }
    }
}
