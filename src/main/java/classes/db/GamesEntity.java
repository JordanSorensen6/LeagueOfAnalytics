package classes.db;

import javax.persistence.*;

@Entity
@Table(name = "games", schema = "public", catalog = "LeagueOfAnalytics")
@IdClass(GamesEntityPK.class)
public class GamesEntity {
    private long matchId;
    private String summoner;
    private String outcome;
    private Double score;

    @Id
    @Column(name = "match_id")
    public long getMatchId() {
        return matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    @Id
    @Column(name = "summoner")
    public String getSummoner() {
        return summoner;
    }

    public void setSummoner(String summoner) {
        this.summoner = summoner;
    }

    @Basic
    @Column(name = "outcome")
    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Basic
    @Column(name = "score")
    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamesEntity that = (GamesEntity) o;

        if (matchId != that.matchId) return false;
        if (Double.compare(that.score, score) != 0) return false;
        if (summoner != null ? !summoner.equals(that.summoner) : that.summoner != null) return false;
        if (outcome != null ? !outcome.equals(that.outcome) : that.outcome != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (matchId ^ (matchId >>> 32));
        result = 31 * result + (summoner != null ? summoner.hashCode() : 0);
        result = 31 * result + (outcome != null ? outcome.hashCode() : 0);
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
