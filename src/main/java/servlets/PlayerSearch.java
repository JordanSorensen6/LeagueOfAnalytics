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
                    gameCheck(summoner, id, analyzed);
                }
                Gson gson = new Gson();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(gson.toJson(analyzed));
                writer.close();
            }
            else {
                List<GamesEntity> analyzed = analyzeNextFive(summoner, match);
                Gson gson = new Gson();
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                PrintWriter writer = response.getWriter();
                writer.write(gson.toJson(analyzed));
                writer.close();
            }
        }
        else if(uri.equals("/match")){
            String matchID = request.getParameter("matchID");
            JsonObject matchStats = getMatchInfo(matchID);
            Gson gson = new Gson();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(gson.toJson(matchStats));
            writer.close();
        }
        else {
            request.setAttribute("username", call.getSummonerName(request.getParameter("name")));
            request.getRequestDispatcher("/playerstats.jsp").forward(request, response);
        }
    }

    private List<GamesEntity> analyzeNextFive(String summoner, String match) throws IOException {
        RiotCalls call = RiotCalls.getInstance();
        ArrayList<String> matchIds = call.getRecentMatchIds(call.getAccountId(summoner), 0, 100);
        ArrayList<GamesEntity> analyzed = new ArrayList<>();
        int startIndex = -1;
        for(int i = 0; i < matchIds.size(); i++) {
            if(matchIds.get(i).equals(match))
                startIndex = i + 1;
        }
        if(startIndex == -1) // game wasn't found in the most recent 100 matches
            return analyzed;
        for(int i = startIndex; i < matchIds.size(); i++) {
            if(analyzed.size() >= 5) break;
            gameCheck(summoner, matchIds.get(i), analyzed);
        }
        return analyzed;
    }

    private void gameCheck(String summoner, String id, List<GamesEntity> analyzed) throws IOException {
        GamesEntity game = GamesDB.getGameByMatchId(summoner, Long.parseLong(id));
        if(game != null) {
            if (game.getScore() != null) {
                analyzed.add(game);
                return;
            }
            return;
        }
        game = analyzeMatch(summoner, id);
        if(game == null) {
            game = new GamesEntity();
            game.setMatchId(Long.parseLong(id));
            game.setSummoner(summoner);
        }
        if(game.getScore() != null)
            analyzed.add(game);
        GamesDB.saveGame(game);
    }

    private JsonObject getMatchInfo (String gameId) throws IOException {
        RiotCalls call = RiotCalls.getInstance();
        JsonObject match = call.getMatch(gameId);
        return match;
    }

    private GamesEntity analyzeMatch(String user, String gameId) throws IOException {
        RiotCalls call = RiotCalls.getInstance();
        JsonObject match = call.getMatch(gameId);
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
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "middle", "gold");
                else if(key.equals(top))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "top", "gold");
                else if(key.equals(jung))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "jungle", "gold");
                else if(key.equals(adc))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "bottom", "gold");
                else if(key.equals(supp))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "support", "gold");
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

    private HashMap<Integer, Pair<String, String>> findMatchups(int teamId, JsonObject match) {
        HashMap<Integer, Pair<String, String>> matchups = new HashMap<>();
        JsonArray participants = match.getAsJsonArray("participants");
        for(int i = 0; i < participants.size(); i++) {
            JsonObject participant = participants.get(i).getAsJsonObject();
            JsonObject participantTimeline = participants.get(i).getAsJsonObject().getAsJsonObject("timeline");
            switch(participantTimeline.get("lane").getAsString()) {
                case "MIDDLE":
                    break;
                case "TOP":
                    break;
                case "JUNGLE":
                    break;
                case "BOTTOM":
                    break;
                // if it reaches the default case, the lane is "NONE"
                default:
                    break;
            }
        }

        return matchups;
    }

    //TODO refactor to find all matchups in 1 call to solve ambiguities
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
