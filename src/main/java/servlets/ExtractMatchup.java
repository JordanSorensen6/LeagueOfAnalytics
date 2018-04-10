package servlets;

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
