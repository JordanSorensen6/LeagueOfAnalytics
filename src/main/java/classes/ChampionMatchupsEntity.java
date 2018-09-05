package classes;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "champion_matchups", schema = "public", catalog = "LeagueOfAnalytics")
@IdClass(ChampionMatchupsEntityPK.class)
public class ChampionMatchupsEntity {
    private int championId;
    private String elo;
    private String role;
    private Serializable matchupJson;

    @Id
    @Column(name = "champion_id", nullable = false)
    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    @Id
    @Column(name = "elo", nullable = false, length = -1)
    public String getElo() {
        return elo;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }

    @Id
    @Column(name = "role", nullable = false, length = -1)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "matchup_json", nullable = true)
    public Serializable getMatchupJson() {
        return matchupJson;
    }

    public void setMatchupJson(Serializable matchupJson) {
        this.matchupJson = matchupJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChampionMatchupsEntity that = (ChampionMatchupsEntity) o;

        if (championId != that.championId) return false;
        if (elo != null ? !elo.equals(that.elo) : that.elo != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;
        if (matchupJson != null ? !matchupJson.equals(that.matchupJson) : that.matchupJson != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = championId;
        result = 31 * result + (elo != null ? elo.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (matchupJson != null ? matchupJson.hashCode() : 0);
        return result;
    }
}
