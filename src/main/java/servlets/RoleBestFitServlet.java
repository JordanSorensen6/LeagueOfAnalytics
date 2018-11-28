package servlets;

import classes.MatchupInfo;
import classes.db.ChampionMatchupsDB;
import classes.db.ChampionMatchupsEntity;
import classes.db.StaticChampionsDB;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class RoleBestFitServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        if(uri.equals("/best-fit/")) {
            String[] champs = request.getParameterValues("champions");
            String elo = request.getParameter("elo");
            HashMap<String, String> roles = bestFit.assignRoles(champs, elo);


            Gson gson = new Gson();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.write(gson.toJson(roles));
            writer.close();
        }
    }

    public HashMap<String, String> assignRoles(String[] champions, String elo) {
        HashMap<String, String> ret = new HashMap<>();
        for(String c : champions) {
            Integer id = StaticChampionsDB.getIdByName(c);
            MatchupInfo matchupInfo = extractMatchupInfo(ChampionMatchupsDB.getByIdAndElo(id, elo));
        }
        return ret;
    }

    private MatchupInfo extractMatchupInfo(List<ChampionMatchupsEntity> matchups) {
        Gson gson = new Gson();
        for(ChampionMatchupsEntity matchup : matchups) {
            if(matchup.getRole() != null && matchup.getMatchupJson() != null) {

            }
        }
    }
}
