package classes;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GamesDB {

    public static List<GamesEntity> getGamesBySummoner(String summoner) {
        Session session = HibernateUtil.getSession();
        String q = "FROM GamesEntity AS G WHERE G.summoner = :user_summoner ORDER BY G.matchId DESC";
        Query query = session.createQuery(q);
        query.setParameter("user_summoner", summoner);
        List<GamesEntity> games = query.getResultList();
        session.close();
        return games;
    }

    public static void saveGame(GamesEntity game) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        try {
            session.saveOrUpdate(game);
        }
        catch(Exception e) {
            // do nothing
        }
        tx.commit();
        session.close();
    }

    public static void saveGames(List<GamesEntity> games) {
        Session session = HibernateUtil.getSession();
        Transaction tx = session.beginTransaction();
        for(GamesEntity game : games) {
            try {
                session.saveOrUpdate(game);
            }
            catch(Exception e) {
                // do nothing
            }
        }
        tx.commit();
        session.close();
    }
}
