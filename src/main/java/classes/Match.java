package classes;

public class Match {
    private Double score;
    private Long gameId;
    private String user;

    public Match(Double score, Long gameId, String user) {
        this.score = score;
        this.gameId = gameId;
        this.user = user;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
