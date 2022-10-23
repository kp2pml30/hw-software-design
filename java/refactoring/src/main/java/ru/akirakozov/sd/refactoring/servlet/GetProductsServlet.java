package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final PrintWriter w = response.getWriter();

        w.println("<html><body>");
        Database.querySqlUnchecked("SELECT * FROM PRODUCT", (rs) -> {
            while (rs.next()) {
                String name = rs.getString("name");
                int price = rs.getInt("price");
                w.println(name + "\t" + price + "</br>");
            }
            return null;
        });
        w.println("</body></html>");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
