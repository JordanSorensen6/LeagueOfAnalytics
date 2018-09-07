package classes;

import com.google.gson.Gson;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StaticChampionsDB {
    public static String getAllChampions() {
        Session session = HibernateUtil.getSession();
        String q = "SELECT C.id, C.formatted FROM StaticChampionsEntity C";
        Query query = session.createQuery(q);
        List<StaticChampionsEntity> champions = query.getResultList();
        session.close();
        Gson gson = new Gson();
        return gson.toJson(champions);
    }

    public static String getById(String id) {
        Session session = HibernateUtil.getSession();
        String q = "FROM StaticChampionsEntity as C WHERE C.id = :champ_id";
        Query query = session.createQuery(q);
        query.setParameter("champ_id", Integer.parseInt(id));
        StaticChampionsEntity champion = (StaticChampionsEntity)query.getSingleResult();
        session.close();
        Gson gson = new Gson();
        return gson.toJson(champion);
    }
}