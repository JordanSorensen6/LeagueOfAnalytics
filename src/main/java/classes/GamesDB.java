package classes;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GamesDB {

    public static List<GamesEntity> getGamesBySummoner(String summoner) {
        Session session = HibernateUtil.getSession();
        String q = "FROM GamesEntity AS G WHERE G.summoner = :user_summoner ORDER BY G.matchId DESC";
        Query query = session.createQuery(q);
        query.setMaxResults(5);
        query.setParameter("user_summoner", summoner);
        List<GamesEntity> games = query.getResultList();
        session.close();
        return games;
    }

    public static List<GamesEntity> getGamesInRange(String summoner, Long begin, Long end) {
        Session session = HibernateUtil.getSession();
        String q = "FROM GamesEntity AS G WHERE G.summoner = :summoner AND G.matchId <= :begin_id AND G.matchId >= :end_id";
        Query query = session.createQuery(q);
        query.setParameter("summoner", summoner);
        query.setParameter("begin_id", begin);
        query.setParameter("end_id", end);
        List<GamesEntity> games = query.getResultList();
        session.close();
        return games;
    }

    public static GamesEntity getGameByMatchId(String summoner, Long matchId) {
        Session session = HibernateUtil.getSession();
        String q = "FROM GamesEntity AS G WHERE G.summoner = :summoner AND G.matchId = :match_id";
        Query query = session.createQuery(q);
        query.setParameter("summoner", summoner);
        query.setParameter("match_id", matchId);
        GamesEntity game = (GamesEntity)query.getSingleResult();
        session.close();
        return game;
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
