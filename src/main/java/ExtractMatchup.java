import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractMatchup extends HttpServlet {

    public String teamChampion;
    public String opponentChampion;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoadConfig config = new LoadConfig();
        String apiKey = config.getRiotApiKey();

        System.out.println(apiKey);
        String uri = request.getRequestURI();
        System.out.println("ARRIVING AT SERVLET WITH URI: " + uri);
        if(uri.equals("/matchup/champions"))
        {
            String c1 = request.getParameter("c1");
            String c2 = request.getParameter("c2");
            String role = request.getParameter("role");

            String matchupInfo = getMatchupInfo(c1,c2,role)+"%";
//            System.out.println(matchupInfo);

            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(matchupInfo);
            writer.close();
        }
        else if(uri.equals("/matchup/score"))//Calculate user score.
        {
            String mastery = request.getParameter("mastery");
            String matchup = request.getParameter("matchup");
            String score = calculateScore(matchup, mastery)+"";

            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(score);
            writer.close();
        }

    }

    private Double calculateScore(String matchup, String mastery)
    {
        Double mat = Double.parseDouble(matchup);
        int mas = Integer.parseInt(mastery);

        Double score = 0.0;

        if (mas <= 2)
            score -= 1;
        else if (mas == 3)
            score -= .5;
        else if (mas == 4)
            score += 0;
        else if (mas == 5)
            score += .5;
        else if (mas >= 6)
            score += 1;

        if (mat != 0)
        {
            if (mat < 48)
                score -= 1;
            else if (mat < 50)
                score -= .5;
            else if (mat < 52)
                score += .5;
            else
                score += 1;

            if (mas < 3 && mat < 48)//Lane is looking really bad.
                score -= 1;
            else if (mas == 3 && mat < 50)//Lane is looking bad.
                score -= .5;
            else if (mas == 4 && mat < 45)//Bad
                score -= .5;
            else if (mas == 4 && mat > 55)//Good
                score += .5;
            else if (mas >= 5 && mat >= 50 && mat <= 52)//Lane is looking good.
                score += .5;
            else if (mas > 5 && mat > 52)//Lane is looking really good.
                score += 1;
        }

        return score;
    }

    private Double getMatchupInfo(String c1, String c2, String role)//c1 is team champ c2 is opponent champ.
    {
        teamChampion = c1;
        opponentChampion = c2;

        Double stat = null;
        String siteContent = getHTML("http://champion.gg/champion/"+c1+"/"+role);

        List<String> allMatches = new ArrayList<String>();
        Matcher m = Pattern.compile(",\"winRate\":(.*?),\"statScore\":")
                .matcher(siteContent);
        while (m.find()) {
            allMatches.add(m.group());
        }
        for(String line : allMatches)
        {
            if(line.contains(c2))
            {
                stat = Double.parseDouble(line.replaceAll("[^\\d.]", ""));
                stat *= 100;
                stat = Math.round(stat * 100.0) / 100.0;
                System.out.println(stat);
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
