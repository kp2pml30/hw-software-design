package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            Database.withStatement((stmt) -> {
                final String sql = "INSERT INTO PRODUCT " +
                        "(NAME, PRICE) VALUES (\"" + name + "\"," + price + ")";
                stmt.executeUpdate(sql);
                return null;
            });
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}
