package servlets;

import classes.MatchupInfo;
import classes.db.ChampionMatchupsDB;
import classes.db.ChampionMatchupsEntity;
import classes.db.StaticChampionsDB;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javafx.util.Pair;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class RoleBestFitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        if(uri.equals("/best-fit")) {
            String[] champs = request.getParameterValues("champs");
            HashMap<String, String> roles = assignRoles(champs, "gold");


            Gson gson = new Gson();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(gson.toJson(roles));
            writer.close();
        }
    }

    private HashMap<String, String> assignRoles(String[] champions, String elo) {
        HashMap<String, String> ret = new HashMap<>(5);
        ArrayList<MatchupInfo> matchups = new ArrayList<>(5);
        for(String champ : champions) {
            Integer id = StaticChampionsDB.getIdByName(champ);
            if(id != null)
                matchups.add(extractMatchupInfo(champ, elo, ChampionMatchupsDB.getByIdAndElo(id, elo)));
        }
        HashMap<String, MatchupInfo> temp = new HashMap<>(5);
        ArrayList<MatchupInfo> unassigned = new ArrayList<>(5);
        ArrayList<MatchupInfo> unassignedSingle = new ArrayList<>(5); // will be added at the end to fill remaining roles

        for(MatchupInfo info : matchups) { // first assign champions with only 1 possible role to their role
            String singleRole = info.getSingleRole();
            if(singleRole != null) {
                if(temp.containsKey(singleRole)) {
                    if(temp.get(singleRole).getSingleRoleCount() < info.getSingleRoleCount()) {
                        unassignedSingle.add(temp.get(singleRole));
                        temp.put(singleRole, info);
                    }
                    else
                        unassignedSingle.add(info);
                }
                else {
                    temp.put(singleRole, info);
                }
            }
            else {
                unassigned.add(info);
            }
        }

        ArrayList<String> taken = new ArrayList<>(5);
        taken.addAll(temp.keySet());

        ArrayList<String> allRoles = new ArrayList<String>();
        allRoles.add("top");
        allRoles.add("jungle");
        allRoles.add("middle");
        allRoles.add("bottom");
        allRoles.add("support");
        ArrayList<String> remaining = new ArrayList<>(5);
        for(String role : allRoles) {
            if(!taken.contains(role)) {
                remaining.add(role);
            }
        }

        if(unassigned.size() > 0) {
            while (remaining.size() > 0) {
                Pair<String, MatchupInfo> best = highestFromRemaining(unassigned, remaining);
                if (best == null)
                    break;
                temp.put(best.getKey(), best.getValue());
                remaining.remove(best.getKey());
                unassigned.remove(best.getValue());
            }
        }

        for(MatchupInfo info : unassigned) {
            temp.put(remaining.get(0), info);
            remaining.remove(0);
        }
        for(MatchupInfo info : unassignedSingle) {
            temp.put(remaining.get(0), info);
            remaining.remove(0);
        }

        for(Map.Entry<String, MatchupInfo> entry : temp.entrySet()) {
            ret.put(entry.getKey(), entry.getValue().getChampion());
        }

        return ret;
    }

    private MatchupInfo extractMatchupInfo(String champion, String elo, List<ChampionMatchupsEntity> matchups) {
        JsonParser parser = new JsonParser();
        MatchupInfo matchupInfo = new MatchupInfo(champion, elo);
        for(ChampionMatchupsEntity matchup : matchups) {
            if(matchup.getRole() != null && matchup.getMatchupJson() != null) {
                JsonArray json = (JsonArray)parser.parse(matchup.getMatchupJson());
                switch(matchup.getRole()) {
                    case "middle":
                        matchupInfo.setMiddleCount(json.size());
                        break;
                    case "top":
                        matchupInfo.setTopCount(json.size());
                        break;
                    case "jungle":
                        matchupInfo.setJungleCount(json.size());
                        break;
                    case "bottom":
                        matchupInfo.setBottomCount(json.size());
                        break;
                    case "support":
                        matchupInfo.setSupportCount(json.size());
                        break;
                    default:
                        break;
                }
            }
        }
        matchupInfo.populateMatchupsArr();
        return matchupInfo;
    }

    private Pair<String, MatchupInfo> highestFromRemaining(ArrayList<MatchupInfo> unassigned, ArrayList<String> remaining) {
        int max = 0;
        MatchupInfo bestInfo = null;
        String bestRole = null;
        for(MatchupInfo info : unassigned) {
            for(String role : remaining) {
                if(info.getCount(role) > max) {
                    max = info.getCount(role);
                    bestInfo = info;
                    bestRole = role;
                }
            }
        }
        if(bestInfo == null || bestRole == null)
            return null;
        return new Pair<String, MatchupInfo>(bestRole, bestInfo);
    }

    private MatchupInfo getHighestForRole(ArrayList<MatchupInfo> list, String role) {
        HashMap<String, Comparator<MatchupInfo>> comparatorFunctions = new HashMap<>();
        comparatorFunctions.put("top", MatchupInfo.topComparator);
        comparatorFunctions.put("jungle", MatchupInfo.jungleComparator);
        comparatorFunctions.put("middle", MatchupInfo.middleComparator);
        comparatorFunctions.put("bottom", MatchupInfo.bottomComparator);
        comparatorFunctions.put("support", MatchupInfo.supportComparator);
        //ArrayList<MatchupInfo> cp = (ArrayList<MatchupInfo>)list.clone();
        Collections.sort(list, comparatorFunctions.get(role));
        return list.get(0);
    }
}
