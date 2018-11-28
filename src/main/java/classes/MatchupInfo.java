package classes;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MatchupInfo {
    private String champion;
    private String elo;
    private ArrayList<Pair<String, Integer>> matchups;

    private int topCount;
    private int middleCount;
    private int jungleCount;
    private int bottomCount;
    private int supportCount;

    public MatchupInfo(String champion) {
        this(champion, "gold");
    }

    public MatchupInfo(String champion, String elo) {
        this.champion = champion;
        this.elo = elo;
        this.topCount = 0;
        this.middleCount = 0;
        this.jungleCount = 0;
        this.bottomCount = 0;
        this.supportCount = 0;
    }

    public boolean singleRole() {
        int roleCount = 0;
        if(topCount > 0)
            roleCount++;
        if(middleCount > 0)
            roleCount++;
        if(jungleCount > 0)
            roleCount++;
        if(bottomCount > 0)
            roleCount++;
        if(supportCount > 0)
            roleCount++;
        return roleCount == 1;
    }

    public String getSingleRole() {
        if(this.singleRole()) {
            if(topCount > 0)
                return "top";
            if(middleCount > 0)
                return "middle";
            if(jungleCount > 0)
                return "jungle";
            if(bottomCount > 0)
                return "bottom";
            if(supportCount > 0)
                return "support";
        }
        return null;
    }

    public int getSingleRoleCount() {
        if(this.getSingleRole() != null) {
            return this.getCount(this.getSingleRole());
        }
        return -1;
    }

    public int getCount(String role) {
        switch(role) {
            case "top": return this.topCount;
            case "jungle": return this.jungleCount;
            case "middle": return this.middleCount;
            case "bottom": return this.bottomCount;
            case "support": return this.supportCount;
            default: return 0;
        }
    }

    public void populateMatchupsArr() {
        this.matchups = new ArrayList<>();
        this.matchups.add(new Pair<>("top", this.topCount));
        this.matchups.add(new Pair<>("jungle", this.topCount));
        this.matchups.add(new Pair<>("middle", this.middleCount));
        this.matchups.add(new Pair<>("bottom", this.bottomCount));
        this.matchups.add(new Pair<>("support", this.supportCount));
        // sort the list in descending order of role count
        Collections.sort(this.matchups, new Comparator<Pair<String, Integer>>() {
            @Override
            public int compare(final Pair<String, Integer> o1, final Pair<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
    }

    public int getTopCount() {
        return topCount;
    }

    public void setTopCount(int topCount) {
        this.topCount = topCount;
    }

    public int getMiddleCount() {
        return middleCount;
    }

    public void setMiddleCount(int middleCount) {
        this.middleCount = middleCount;
    }

    public int getJungleCount() {
        return jungleCount;
    }

    public void setJungleCount(int jungleCount) {
        this.jungleCount = jungleCount;
    }

    public int getBottomCount() {
        return bottomCount;
    }

    public void setBottomCount(int bottomCount) {
        this.bottomCount = bottomCount;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
    }

    public String getElo() {
        return elo;
    }

    public void setElo(String elo) {
        this.elo = elo;
    }

    public ArrayList<Pair<String, Integer>> getMatchups() {
        return matchups;
    }

    public void setMatchups(ArrayList<Pair<String, Integer>> matchups) {
        this.matchups = matchups;
    }
}
