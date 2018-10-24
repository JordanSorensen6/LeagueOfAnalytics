package servlets;

import classes.*;
import classes.db.GamesEntity;
import com.google.gson.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class PlayerSearch extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        RiotCalls call = RiotCalls.getInstance();
        if(uri.equals("/search")) {
            request.getRequestDispatcher("/playersearch.jsp").forward(request, response);
        }
        else if(uri.equals("/history")) {
            String summoner = call.getSummonerName(request.getParameter("user"));
            String match = request.getParameter("match");
            if(summoner == null || summoner.equals("")) { // can't search history without a summoner name
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write("");
                writer.close();
            }
            else if(match == null) {
                ArrayList<String> matchIds = call.getRecentMatchIds(call.getAccountId(summoner), 0, 50);

                ArrayList<GamesEntity> analyzed = new ArrayList<>();
                // analyze the first 5 matches
                for(String id : matchIds) {
                    if(analyzed.size() >= 5) break;
                    new MatchAnalyzer().gameCheck(summoner, id, analyzed);
                }
                Gson gson = new Gson();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(gson.toJson(analyzed));
                writer.close();
            }
            else {
                List<GamesEntity> analyzed = new MatchAnalyzer().analyzeNextFive(summoner, match);
                Gson gson = new Gson();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(gson.toJson(analyzed));
                writer.close();
            }
        }
        else {
            request.setAttribute("username", call.getSummonerName(request.getParameter("name")));
            request.getRequestDispatcher("/playerstats.jsp").forward(request, response);
        }
    }
}
