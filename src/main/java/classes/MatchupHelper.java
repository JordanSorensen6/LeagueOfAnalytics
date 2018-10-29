package classes;

public class MatchupHelper {
    public String team;
    public String opponent;
    public MatchupHelper(String team, String opponent) {
        this.team = team;
        this.opponent = opponent;
    }

    public boolean isComplete() {
        return this.team != null && this.opponent != null;
    }

    public void fillMatchup(String fill) {
        if(this.team != null && this.opponent == null)
            this.opponent = fill;
        else if(this.team == null && this.opponent != null)
            this.team = fill;
    }
}
