package servlets;

import classes.LoadConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class ApplicationStats extends HttpServlet {

    private static LoadConfig config = LoadConfig.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        Pattern byRank = Pattern.compile("^/app_stats/by-rank?league=(?:bronze)(?:silver)(?:gold)(?:platinum)(?:diamond)$");
        if(byRank.matcher(uri).matches()) {
            String league = request.getParameter("league");
            request.getRequestDispatcher("/appstats.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/appstats.jsp").forward(request, response);
        }
    }
}
