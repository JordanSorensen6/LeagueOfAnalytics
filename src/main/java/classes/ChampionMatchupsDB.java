package classes;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ChampionMatchupsDB {

    public static List<ChampionMatchupsEntity> getAllMatchups() {
        Session session = HibernateUtil.getSession();
        String q = "SELECT M FROM ChampionMatchupsEntity M";
        Query query = session.createQuery(q);
        List<ChampionMatchupsEntity> matchups = query.getResultList();
        session.close();
        return matchups;
    }

    public static List<ChampionMatchupsEntity> getById(Integer id) {
        Session session = HibernateUtil.getSession();
        String q = "FROM ChampionMatchupsEntity as M WHERE M.championId = :champ_id";
        Query query = session.createQuery(q);
        query.setParameter("champ_id", id);
        List<ChampionMatchupsEntity> matchups = query.getResultList();
        session.close();
        return matchups;
    }

    public static ChampionMatchupsEntity getByAll(Integer id, String elo, String role) {
        Session session = HibernateUtil.getSession();
        String q = "FROM ChampionMatchupsEntity as M WHERE M.championId = :champ_id AND M.elo = :elo AND M.role = :role";
        Query query = session.createQuery(q);
        query.setParameter("champ_id", id);
        query.setParameter("elo", elo);
        query.setParameter("role", role.toLowerCase());
        List<ChampionMatchupsEntity> matchup = query.getResultList();
        session.close();
        if(matchup.size() == 0)
            return null;
        return matchup.get(0);
    }
}
