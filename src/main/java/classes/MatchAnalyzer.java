package classes;

import com.google.gson.*;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchAnalyzer {

    public void gameCheck(String summoner, String id, List<GamesEntity> analyzed) throws IOException {
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
                        teammateMasteries.put(teammateChampionId, teammateMastery);
                    }
                }
            }
        }

        HashMap<String, Pair<String, String>> matchups = findMatchups(userTeamId, match);

        if(matchups.size() == 5) {
            // only analyze & store if all matchups were able to be found
            Scoring scoring = new Scoring();
            Double score = 0.0;
            Double matchupCalc = 0.0;
            for(String key : matchups.keySet()) {
                Integer teamChampId = StaticChampionsDB.getIdByName(matchups.get(key).getKey());
                String teammateMastery = teammateMasteries.get(teamChampId);
                Pair<String, String> matchup = matchups.get(key);
                //calculate matchup
                if(key.equals("MIDDLE"))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "middle", "gold");
                else if(key.equals("TOP"))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "top", "gold");
                else if(key.equals("JUNGLE"))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "jungle", "gold");
                else if(key.equals("BOTTOM"))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "bottom", "gold");
                else if(key.equals("SUPPORT"))
                    matchupCalc = scoring.getMatchupInfo(matchup.getKey(), matchup.getValue(), "support", "gold");
                //calculate score
                if(matchupCalc == null)
                    matchupCalc = 50.0;
                String info = call.getSummonerInfo(call.getSummonerId(user));
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray infoJson = parser.parse(info).getAsJsonArray();
                Double winrate = 50.0;
                for(JsonElement queue : infoJson) {
                    if(queue.getAsJsonObject().get("queueType").getAsString().equals("RANKED_SOLO_5x5")) {
                        Double wins = queue.getAsJsonObject().get("wins").getAsDouble();
                        Double losses = queue.getAsJsonObject().get("losses").getAsDouble();
                        if(wins == 0.0 && losses == 0.0)
                            winrate = 50.0;
                        else if(wins == 0.0)
                            winrate = 0.0;
                        else if(losses == 0.0)
                            winrate = 100.0;
                        else
                            winrate = (wins / (wins + losses)) * 100;
                    }
                }
                score += scoring.calculateScore(Double.toString(matchupCalc), teammateMastery, winrate.toString(), "False");//TODO: Get player winrate and hotstreak info from Riot.
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

    public List<GamesEntity> analyzeNextFive(String summoner, String match) throws IOException {
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

    private HashMap<String, Pair<String, String>> findMatchups(int teamId, JsonObject match) {
        HashMap<String, MatchupHelper> matchups = new HashMap<>();
        ArrayList<Integer> toBeAssigned = new ArrayList<>();
        JsonArray participants = match.getAsJsonArray("participants");
        String midTeam = null;
        String midOpp = null;
        String topTeam = null;
        String topOpp = null;
        String jgTeam = null;
        String jgOpp = null;
        String adTeam = null;
        String adOpp = null;
        String suppTeam = null;
        String suppOpp = null;

        for(int i = 0; i < participants.size(); i++) {
            JsonObject participant = participants.get(i).getAsJsonObject();
            int currTeamId = participant.get("teamId").getAsInt();
            JsonObject participantTimeline = participants.get(i).getAsJsonObject().getAsJsonObject("timeline");
            switch(participantTimeline.get("lane").getAsString()) {
                case "MIDDLE":
                    if(teamId == currTeamId)
                        midTeam = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    else
                        midOpp = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    break;
                case "TOP":
                    if(teamId == currTeamId)
                        topTeam = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    else
                        topOpp = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    break;
                case "JUNGLE":
                    if(teamId == currTeamId)
                        jgTeam = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    else
                        jgOpp = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    break;
                case "BOTTOM":
                    if(participantTimeline.get("role").getAsString().equals("DUO_CARRY")) {
                        if(teamId == currTeamId)
                            adTeam = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                        else
                            adOpp = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    }
                    else {
                        if(teamId == currTeamId)
                            suppTeam = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                        else
                            suppOpp = StaticChampionsDB.getNameById(participant.get("championId").getAsInt());
                    }
                    break;
                // if it reaches the default case, the lane is "NONE"
                default:
                    toBeAssigned.add(i);
            }
        }
        matchups.put("TOP", new MatchupHelper(topTeam, topOpp));
        matchups.put("MIDDLE", new MatchupHelper(midTeam, midOpp));
        matchups.put("JUNGLE", new MatchupHelper(jgTeam, jgOpp));
        matchups.put("BOTTOM", new MatchupHelper(adTeam, adOpp));
        matchups.put("SUPPORT", new MatchupHelper(suppTeam, suppOpp));
        if(toBeAssigned.size() > 1) {
            return new HashMap<>(); // throw game away
        }
        else if(toBeAssigned.size() == 1) {
            JsonObject participant = participants.get(toBeAssigned.get(0)).getAsJsonObject();
            String missingLane = getMissingLane(matchups);
            if(missingLane.equals("NONE") || missingLane.equals("MULTIPLE"))
                return new HashMap<>();
            else
                matchups.get(missingLane).fillMatchup(StaticChampionsDB.getNameById(participant.get("championId").getAsInt()));
        }
        if(!matchupsComplete(matchups))
            return new HashMap<>();
        return getMatchupMap(matchups);
    }

    private static boolean matchupsComplete(HashMap<String, MatchupHelper> matchups) {
        for(MatchupHelper m : matchups.values())
            if(!m.isComplete())
                return false;
        return true;
    }

    private static String getMissingLane(HashMap<String, MatchupHelper> matchups) {
        int missingCount = 0;
        String missingLane = null;
        for(String lane : matchups.keySet()) {
            MatchupHelper matchup = matchups.get(lane);
            if(!matchup.isComplete()) {
                missingCount++;
                missingLane = lane;
            }
        }
        if(missingCount == 1)
            return missingLane;
        else if(missingCount > 1)
            return "MULTIPLE";
        else
            return "NONE";
    }

    private HashMap<String, Pair<String, String>> getMatchupMap(HashMap<String, MatchupHelper> matchups) {
        HashMap<String, Pair<String, String>> ret = new HashMap<>();
        for(String key : matchups.keySet()) {
            MatchupHelper m = matchups.get(key);
            ret.put(key, new Pair<>(m.team, m.opponent));
        }
        return ret;
    }
}
