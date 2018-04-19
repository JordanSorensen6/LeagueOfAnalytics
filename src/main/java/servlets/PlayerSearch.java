package servlets;

import classes.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PlayerSearch extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if(uri.equals("/search")) {
            request.getRequestDispatcher("/playersearch.jsp").forward(request, response);
        }
        else if(Pattern.compile("^/history/search?user=*$").matcher(uri).matches()) {
            // pull history from database
        }
        else {
            RiotCalls call = new RiotCalls();
            request.setAttribute("username", request.getParameter("name"));
            String summonerId = call.getAccountId(request.getParameter("name"));

            JsonArray matchlist = call.getRecentMatches(summonerId);
            // analyze each match
            for(int i = 0; i < matchlist.size(); i++) {
                String gameId = Long.toString(matchlist.get(i).getAsJsonObject().get("gameId").getAsLong());
                JsonObject match = call.getMatch(gameId);
                GamesEntity analyzed = analyzeMatch(request.getParameter("name"), gameId, match);
                if(analyzed != null) {
                    //only store if there are no errors in analyzing
                    SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
                    Session session = sessionFactory.openSession();
                    session.persist(analyzed);
                }
            }
            //TODO remove, this is a test save to database to ensure correct connection
/*            GamesEntity game = new GamesEntity();
            game.setMatchId(1234);
            game.setScore(5.5);
            game.setOutcome("Win");
            game.setSummoner("cloudrhymes");
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(game);
                tx.commit();
            }
            catch(Exception e) {
                if(tx != null)
                    tx.rollback();
                throw e;
            }
            finally {
                session.close();
            }*/

            request.getRequestDispatcher("/playerstats.jsp").forward(request, response);
        }
    }

    private GamesEntity analyzeMatch(String user, String gameId, JsonObject match) throws IOException {
        RiotCalls call = new RiotCalls();
        // get users team id
        JsonArray participantIdentities = match.getAsJsonArray("participantIdentities");
        int userTeamId = 0;
        int userParticipantId = 0;
        for(int j = 0; j < participantIdentities.size(); j++) {
            if(participantIdentities.get(j).getAsJsonObject().getAsJsonObject("player").get("summonerName")
                    .getAsString().equals(user)) {
                userParticipantId = participantIdentities.get(j).getAsJsonObject().get("participantId").getAsInt();
            }
        }
        JsonArray participants = match.getAsJsonArray("participants");
        for(int j = 0; j < participants.size(); j++) {
            if(participants.get(j).getAsJsonObject().get("participantId").getAsInt() == userParticipantId) {
                userTeamId = participants.get(j).getAsJsonObject().get("teamId").getAsInt();
            }
        }
        // get outcome
        String win = "";
        JsonArray teamStats = match.getAsJsonArray("teams");
        for(int j = 0; j < teamStats.size(); j++) {
            if(teamStats.get(j).getAsJsonObject().get("teamId").getAsInt() == userTeamId) {
                win = teamStats.get(j).getAsJsonObject().get("win").getAsString();
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
                score += scoring.calculateScore(Double.toString(matchupCalc), teammateMastery);
            }
            GamesEntity game = new GamesEntity();
            game.setMatchId(Integer.getInteger(gameId));
            game.setScore(score);
            game.setOutcome(win);
            game.setSummoner(user);

            return game;
        }
        else {
            return null;
        }
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
        String champ1 = new StaticChampions().getById(Integer.toString(champId1));
        String champ2 = new StaticChampions().getById(Integer.toString(champId2));
        // add matchup to map
        matchups.put(participantId, new Pair<String, String>(champ1, champ2));
        return participantId;
    }
}
