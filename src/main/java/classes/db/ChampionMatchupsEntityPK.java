package classes.db;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class ChampionMatchupsEntityPK implements Serializable {
    private int championId;
    private String elo;
    private String role;

    @Column(name = "champion_id", nullable = false)
    @Id
    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    @Column(name = "elo", nullable = false, length = -1)
    @Id
    public String getElo() {
        return elo;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }

    @Column(name = "role", nullable = false, length = -1)
    @Id
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChampionMatchupsEntityPK that = (ChampionMatchupsEntityPK) o;

        if (championId != that.championId) return false;
        if (elo != null ? !elo.equals(that.elo) : that.elo != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = championId;
        result = 31 * result + (elo != null ? elo.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
