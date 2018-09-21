package servlets;

import classes.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerSearch extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if(uri.equals("/search")) {
            request.getRequestDispatcher("/playersearch.jsp").forward(request, response);
        }
        else if(uri.equals("/history")) {
            RiotCalls call = new RiotCalls();
            String summoner = call.getSummonerName(request.getParameter("user"));
            if(summoner != null && summoner != "") {
                // pull history from database
                Session session = HibernateUtil.getSession();
                String q = "FROM GamesEntity AS G WHERE G.summoner = :user_summoner";
                Query query = session.createQuery(q);
                query.setMaxResults(5);
                query.setParameter("user_summoner", summoner);
                List<GamesEntity> games = query.getResultList();
                if(games.size() < 5) {
                    getAndSaveRecentGames(request.getParameter("user"));
                    games = query.getResultList();
                }

                Gson gson = new Gson();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(gson.toJson(games));
                writer.close();
                session.close();
            }
            else {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write("");
                writer.close();
            }
        }
        else {
            RiotCalls call = new RiotCalls();
            request.setAttribute("username", call.getSummonerName(request.getParameter("name")));
            request.getRequestDispatcher("/playerstats.jsp").forward(request, response);
        }
    }

    private GamesEntity analyzeMatch(String user, String gameId, JsonObject match) throws IOException {
        RiotCalls call = new RiotCalls();
        // get users team id
        JsonArray participantIdentities = match.getAsJsonArray("participantIdentities");
        int userTeamId = 0;
        int userParticipantId = 0;
        String userSummonerName = "";
        for(int j = 0; j < participantIdentities.size(); j++) {
            if(participantIdentities.get(j).getAsJsonObject().getAsJsonObject("player").get("summonerName")
                    .getAsString().toUpperCase().replaceAll("\\s+","").equals(user.toUpperCase().replaceAll("\\s+",""))) {
                userParticipantId = participantIdentities.get(j).getAsJsonObject().get("participantId").getAsInt();
                userSummonerName = participantIdentities.get(j).getAsJsonObject().getAsJsonObject("player").get("summonerName").getAsString();
                break;
            }
        }
        JsonArray participants = match.getAsJsonArray("participants");
        for(int j = 0; j < participants.size(); j++) {
            if(participants.get(j).getAsJsonObject().get("participantId").getAsInt() == userParticipantId) {
                userTeamId = participants.get(j).getAsJsonObject().get("teamId").getAsInt();
                break;
            }
        }
        // get outcome
        String win = "";
        JsonArray teamStats = match.getAsJsonArray("teams");
        for(int j = 0; j < teamStats.size(); j++) {
            if(teamStats.get(j).getAsJsonObject().get("teamId").getAsInt() == userTeamId) {
                win = teamStats.get(j).getAsJsonObject().get("win").getAsString();
                break;
            }
        }
        // get teammates mastery levels
        HashMap<Integer, String> teammateMasteries = new HashMap<>();
        for(int j = 0; j < participants.size(); j++) {
            // found teammate
            if(participants.get(j).getAsJsonObject().get("teamId").getAsInt() == userTeamId) {
                int teammateChampionId = participants.get(j).getAsJsonObject().get("championId").getAsInt();
                int participantId = participants.get(j).getAsJsonObject().get("participantId").getAsInt();
                for(int k = 0; k < participantIdentities.size(); k++) {
                    if(participantIdentities.get(k).getAsJsonObject().get("participantId").getAsInt() == participantId) {
                        long teammateSummId = participantIdentities.get(k).getAsJsonObject().getAsJsonObject("player").get("summonerId").getAsLong();
                        String teammateMastery = call.getChampionMastery(Long.toString(teammateSummId), Integer.toString(teammateChampionId));
                        teammateMasteries.put(participantId, teammateMastery);
                    }
                }
            }
        }
        // get matchups the resulting pair is of the form (teammate champion, enemy champion)
        HashMap<Integer, Pair<String, String>> matchups = new HashMap<>();
        Integer mid = findMatchup("MIDDLE", "SOLO", userTeamId, matchups, match);
        Integer top = findMatchup("TOP", "SOLO", userTeamId, matchups, match);
        Integer jung = findMatchup("JUNGLE", "NONE", userTeamId, matchups, match);
        Integer adc = findMatchup("BOTTOM", "DUO_CARRY", userTeamId, matchups, match);
        Integer supp = findMatchup("BOTTOM", "DUO_SUPPORT", userTeamId, matchups, match);

        if(matchups.size() == 5) {
            // only analyze & store if all matchups were able to be found
            Scoring scoring = new Scoring();
            Double score = 0.0;
            Double matchupCalc = 0.0;
            for(Integer key: matchups.keySet()) {
                String teammateMastery = teammateMasteries.get(key);
                Pair<String, String> matchup = matchups.get(key);
                //calculate matchup
                if(key.equals(mid))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "Middle", "gold");
                else if(key.equals(top))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "Top", "gold");
                else if(key.equals(jung))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "Jungle", "gold");
                else if(key.equals(adc))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "ADC", "gold");
                else if(key.equals(supp))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "Support", "gold");
                //calculate score
                if(matchupCalc == null)
                    matchupCalc = 50.0;
                score += scoring.calculateScore(Double.toString(matchupCalc), teammateMastery, "0.0", "False");//TODO: Get player winrate and hotstreak info from Riot.
            }
            GamesEntity game = new GamesEntity();
            game.setMatchId(Long.parseLong(gameId));
            game.setScore(score);
            game.setOutcome(win);
            game.setSummoner(userSummonerName);

            return game;
        }
        else {
            return null;
        }
    }

    private void getAndSaveRecentGames(String name) throws IOException {
        RiotCalls call = new RiotCalls();
        String summonerId = call.getAccountId(name);

        JsonArray matchlist = call.getRecentMatches(summonerId);
        // analyze each match
        ArrayList<GamesEntity> analyzedList = new ArrayList<>();
        for(int i = 0; i < matchlist.size() && i < 11; i++) {
            String gameId = Long.toString(matchlist.get(i).getAsJsonObject().get("gameId").getAsLong());
            JsonObject match = call.getMatch(gameId);
            GamesEntity analyzed = null;
            if(match != null)
                analyzed = analyzeMatch(name, gameId, match);
            if(analyzed != null) {
                analyzedList.add(analyzed);
            }
        }
        // store games
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        for(GamesEntity analyzed : analyzedList) {
            try {
                session.saveOrUpdate(analyzed);
            }
            catch(Exception e) {
                // do nothing
            }
        }
        tx.commit();
        session.close();
    }

    private Integer findMatchup(String lane, String role, int teamId, HashMap<Integer, Pair<String, String>> matchups, JsonObject match) {
        JsonArray participants = match.getAsJsonArray("participants");
        int participantId = 0;
        int champId1 = -1;
        int champId2 = -1;
        for(int i = 0; i < participants.size(); i++) {
            JsonObject participant = participants.get(i).getAsJsonObject();
            JsonObject participantTimeline = participants.get(i).getAsJsonObject().getAsJsonObject("timeline");
            if(participantTimeline.get("lane").getAsString().equals(lane) && participantTimeline.get("role").getAsString().equals(role)) {
                if(participant.get("teamId").getAsInt() == teamId) {
                    participantId = participant.get("participantId").getAsInt();
                    champId1 = participant.get("championId").getAsInt();
                }
                else {
                    champId2 = participant.get("championId").getAsInt();
                }
            }
        }
        if(participantId == 0 || champId1 == -1 || champId2 == -1) {
            //something broke, don't analyze this game
            return 0;
        }
        String champ1 = StaticChampionsDB.getNameById(champId1);
        String champ2 = StaticChampionsDB.getNameById(champId2);
        // add matchup to map
        matchups.put(participantId, new Pair<String, String>(champ1, champ2));
        return participantId;
    }
}
