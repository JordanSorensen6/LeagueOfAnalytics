package classes;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ChampionMatchupsDB {
    public static String getAllMatchups() {
        Session session = HibernateUtil.getSession();
        String q = "SELECT M FROM ChampionMatchupsEntity M";
        Query query = session.createQuery(q);
        List<ChampionMatchupsEntity> matchups = query.getResultList();
        session.close();
        Gson gson = new Gson();
        return gson.toJson(matchups);
    }

    public static String getById(String id) {
        Session session = HibernateUtil.getSession();
        String q = "FROM ChampionMatchupsEntity as M WHERE M.championId = :champ_id";
        Query query = session.createQuery(q);
        query.setParameter("champ_id", Integer.parseInt(id));
        List<ChampionMatchupsEntity> matchups = query.getResultList();
        session.close();
        Gson gson = new Gson();
        return gson.toJson(matchups);
    }
}
