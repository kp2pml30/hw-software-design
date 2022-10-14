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
    private void handleMax(HttpServletResponse response) throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection(Defs.dbAddress)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");

            rs.close();
            stmt.close();
        }
    }

    private void handleMin(HttpServletResponse response) throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection(Defs.dbAddress)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with min price: </h1>");

            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                response.getWriter().println(name + "\t" + price + "</br>");
            }
            response.getWriter().println("</body></html>");

            rs.close();
            stmt.close();
        }
    }

    private void handleSum(HttpServletResponse response) throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection(Defs.dbAddress)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");
            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");

            if (rs.next()) {
                response.getWriter().println(rs.getInt(1));
            }
            response.getWriter().println("</body></html>");

            rs.close();
            stmt.close();
        }
    }

    private void handleCount(HttpServletResponse response) throws IOException, SQLException {
        try (Connection c = DriverManager.getConnection(Defs.dbAddress)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");
            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");

            if (rs.next()) {
                response.getWriter().println(rs.getInt(1));
            }
            response.getWriter().println("</body></html>");

            rs.close();
            stmt.close();
        }
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
