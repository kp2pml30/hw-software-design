package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private static class NameInt {
        String name;
        int num;
    }

    private NameInt performSingleIntQuery(final String query, final boolean hasName) throws SQLException {
        try (final Connection c = DriverManager.getConnection(Defs.dbAddress)) {
            try (final Statement stmt = c.createStatement()) {
                final ResultSet rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    return null;
                }
                final NameInt ret = new NameInt();
                if (hasName) {
                    ret.name = rs.getString("name");
                }
                ret.num = rs.getInt("num");
                assert !rs.next();
                return ret;
            }
        }
    }

    private void handleMax(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT NAME, PRICE AS NUM FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", true);

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Product with max price: </h1>");
        if (ni != null) {
            response.getWriter().println(ni.name + "\t" + ni.num + "</br>");
        }
        response.getWriter().println("</body></html>");
    }

    private void handleMin(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT NAME, PRICE AS NUM FROM PRODUCT ORDER BY PRICE LIMIT 1", true);

        response.getWriter().println("<html><body>");
        response.getWriter().println("<h1>Product with min price: </h1>");
        if (ni != null) {
            response.getWriter().println(ni.name + "\t" + ni.num + "</br>");
        }
        response.getWriter().println("</body></html>");
    }

    private void handleSum(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT SUM(price) AS NUM FROM PRODUCT", false);
        assert ni != null;
        response.getWriter().println("<html><body>");
        response.getWriter().println("Summary price: ");
        response.getWriter().println(ni.num);
        response.getWriter().println("</body></html>");
    }

    private void handleCount(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT COUNT(*) AS NUM FROM PRODUCT", false);
        assert ni != null;
        response.getWriter().println("<html><body>");
        response.getWriter().println("Number of products: ");
        response.getWriter().println(ni.num);
        response.getWriter().println("</body></html>");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        try {
            if ("max".equals(command)) {
                handleMax(response);
            } else if ("min".equals(command)) {
                handleMin(response);
            } else if ("sum".equals(command)) {
                handleSum(response);
            } else if ("count".equals(command)) {
                handleCount(response);
            } else {
                response.getWriter().println("Unknown command: " + command);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
