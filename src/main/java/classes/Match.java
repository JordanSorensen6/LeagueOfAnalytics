package classes;

public class Match {
    private int score;
    private int gameId;
    private String user;

    public Match(int score, int gameId, String user) {
        this.score = score;
        this.gameId = gameId;
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
