package classes.db;

import classes.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StaticChampionsDB {

    public static List<StaticChampionsEntity> getAllChampions() {
        Session session = HibernateUtil.getSession();
        String q = "SELECT C.id, C.formatted FROM StaticChampionsEntity C";
        Query query = session.createQuery(q);
        List<StaticChampionsEntity> champions = query.getResultList();
        session.close();
        return champions;
    }

    public static List<StaticChampionsEntity> getAllChampionsTags() {
        Session session = HibernateUtil.getSession();
        String q = "SELECT C.formatted, C.tags FROM StaticChampionsEntity C";
        Query query = session.createQuery(q);
        List<StaticChampionsEntity> champions = query.getResultList();
        session.close();
        return champions;
    }

    public static StaticChampionsEntity getById(Integer id) {
        Session session = HibernateUtil.getSession();
        String q = "FROM StaticChampionsEntity as C WHERE C.id = :champ_id";
        Query query = session.createQuery(q);
        query.setParameter("champ_id", id);
        StaticChampionsEntity champion = (StaticChampionsEntity)query.getSingleResult();
        session.close();
        return champion;
    }

    public static String getNameById(Integer id) {
        StaticChampionsEntity champion = getById(id);
        return champion.getName();
    }

    public static String getFormattedById(Integer id) {
        StaticChampionsEntity champion = getById(id);
        return champion.getName();
    }

    public static Integer getIdByName(String name) {
        Session session = HibernateUtil.getSession();
        // make sure name is properly formatted
        name = name.toLowerCase().replaceAll("[^a-z]", "");
        String q = "FROM StaticChampionsEntity as C WHERE C.name = :name";
        Query query = session.createQuery(q);
        query.setParameter("name", name);
        StaticChampionsEntity champion = (StaticChampionsEntity)query.getSingleResult();
        session.close();
        return champion.getId();
    }
}
