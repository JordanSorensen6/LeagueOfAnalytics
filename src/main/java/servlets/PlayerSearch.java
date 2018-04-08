package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PlayerSearch extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();

        if(uri.equals("/search/"))
            request.getRequestDispatcher("/playersearch.jsp").forward(request, response);
        else {
            request.setAttribute("username", request.getParameter("name"));
            request.getRequestDispatcher("/playerstats.jsp").forward(request, response);
        }
    }
}
