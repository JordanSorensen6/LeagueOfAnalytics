package servlets;

import classes.Scoring;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractMatchup extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Scoring scoring = new Scoring();
        String uri = request.getRequestURI();
//        System.out.println("ARRIVING AT SERVLET WITH URI: " + uri);
        if(uri.equals("/matchup/champions"))
        {
            String c1 = request.getParameter("c1");
            String c2 = request.getParameter("c2");
            String role = request.getParameter("role");
            String league = request.getParameter("league");

            String matchupInfo;
            try {
                matchupInfo = scoring.getMatchupInfo(c1, c2, role, league) + "%";
            }catch (NoResultException ex) {
                matchupInfo = "?";
            }
//            System.out.println(matchupInfo);

            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(matchupInfo);
            writer.close();
        }
        else if(uri.equals("/matchup/score"))//Calculate user score.
        {
            String mastery = request.getParameter("mastery");
            String matchup = request.getParameter("matchup");
            String winrate = request.getParameter("winrate");
            String hotstreak = request.getParameter("hotstreak");
            String score = scoring.calculateScore(matchup, mastery, winrate, hotstreak)+"";

            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(score);
            writer.close();
        }
        else if(uri.equals("/matchup/opponent"))
        {
            String opponent = request.getParameter("opponent");
            String league = request.getParameter("league");
            String role = request.getParameter("role");
            String json = scoring.getOpponentMatchup(opponent, league, role)+"";
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(json);
            writer.close();
        }

    }
}
