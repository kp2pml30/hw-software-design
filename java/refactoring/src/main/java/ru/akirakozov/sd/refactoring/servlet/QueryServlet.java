package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
        return Defs.querySql(query, (rs) -> {
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
        });
    }

    private void handleMax(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT NAME, PRICE AS NUM FROM PRODUCT ORDER BY PRICE DESC LIMIT 1", true);
        final PrintWriter w = response.getWriter();

        w.println("<html><body>");
        w.println("<h1>Product with max price: </h1>");
        if (ni != null) {
            w.println(ni.name + "\t" + ni.num + "</br>");
        }
        w.println("</body></html>");
    }

    private void handleMin(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT NAME, PRICE AS NUM FROM PRODUCT ORDER BY PRICE LIMIT 1", true);
        final PrintWriter w = response.getWriter();

        w.println("<html><body>");
        w.println("<h1>Product with min price: </h1>");
        if (ni != null) {
            w.println(ni.name + "\t" + ni.num + "</br>");
        }
        w.println("</body></html>");
    }

    private void handleSum(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT SUM(price) AS NUM FROM PRODUCT", false);
        assert ni != null;
        final PrintWriter w = response.getWriter();

        w.println("<html><body>");
        w.println("Summary price: ");
        w.println(ni.num);
        w.println("</body></html>");
    }

    private void handleCount(HttpServletResponse response) throws IOException, SQLException {
        final NameInt ni = performSingleIntQuery("SELECT COUNT(*) AS NUM FROM PRODUCT", false);
        assert ni != null;
        final PrintWriter w = response.getWriter();

        w.println("<html><body>");
        w.println("Number of products: ");
        w.println(ni.num);
        w.println("</body></html>");
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
