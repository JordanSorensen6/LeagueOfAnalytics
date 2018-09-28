package classes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scoring {

    public Double calculateScore(String matchup, String mastery, String winRate, String hotStreak)
    {
        Double score = 0.0;
        switch (Integer.parseInt(mastery))//Score champion mastery
        {
            case 1:
                score -= 1;
                break;
            case 2:
                score -= 1;
                break;
            case 3:
                score -= .5;
                break;
            case 4:
                score -= .5;
                break;
            case 5:
                score += 0;
                break;
            case 6:
                score += .5;
                break;
            case 7:
                score += 1;
                break;
        }

        Double mat = Double.parseDouble(matchup);
        if (mat != 0)//Score champion matchup
        {
            if (mat < 50)
                score -= .5;
            else
                score += .5;
        }

        if (!winRate.equals("NaN"))//Score player winrate
        {
            Double wr = Double.parseDouble(winRate);
            if (wr < 50)
                score -= .5;
            else
                score += .5;
        }

        if (hotStreak.equals("True"))
            score += .5;

        return score;
    }

    public Double getMatchupInfo(String c1, String c2, String role, String league)//c1 is team champ c2 is opponent champ.
    {
        //format champion names
        c1 = c1.toLowerCase().replaceAll("[^a-z]", "");
        c2 = c2.toLowerCase().replaceAll("[^a-z]", "");

        Double stat = null;

        Integer c1Id = StaticChampionsDB.getIdByName(c1);

        String matchupJson = ChampionMatchupsDB.getByAll(c1Id, league, role).getMatchupJson();
        if(matchupJson == null) //TODO handle differently?
            return 50.0;

        HashMap<String, Double> allMatches = new HashMap<>();
        Matcher m = Pattern.compile("\"opponentChampion\":(.*?), \"winRate\": (.*?)}")
                .matcher(matchupJson);
        while (m.find()) {
            allMatches.put(m.group(1), Double.parseDouble(m.group(2)));
        }
        for(Map.Entry<String, Double> entry : allMatches.entrySet()) {
            String name = entry.getKey().toLowerCase().replaceAll("[^a-z]", "");
            if(name.equals(c2)) {
                stat = entry.getValue() * 100;
                stat = Math.round(stat * 100) / 100.0;
                break;
            }
        }
        return stat;
    }


    private String getHTML(String urlToRead) {
        URL url; // The URL to read
        HttpURLConnection conn; // The actual connection to the web page
        BufferedReader rd; // Used to read results from the web page
        String line; // An individual line of the web page HTML
        String result = ""; // A long string containing all the HTML
        System.setProperty("http.agent", "Chrome");
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = rd.readLine()) != null) {
                result += line;
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
