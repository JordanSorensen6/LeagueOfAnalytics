package classes;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class GamesEntityPK implements Serializable {
    private int matchId;
    private String summoner;

    @Column(name = "match_id")
    @Id
    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    @Column(name = "summoner")
    @Id
    public String getSummoner() {
        return summoner;
    }

    public void setSummoner(String summoner) {
        this.summoner = summoner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamesEntityPK that = (GamesEntityPK) o;

        if (matchId != that.matchId) return false;
        if (summoner != null ? !summoner.equals(that.summoner) : that.summoner != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = matchId;
        result = 31 * result + (summoner != null ? summoner.hashCode() : 0);
        return result;
    }
}
