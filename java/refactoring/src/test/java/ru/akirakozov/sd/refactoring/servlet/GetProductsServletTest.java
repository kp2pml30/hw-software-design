package ru.akirakozov.sd.refactoring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Clock;
import java.time.Instant;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetProductsServletTest {

    static HttpServletResponse getMockResponse(final StringWriter out) throws IOException {
        final PrintWriter pWriter = new PrintWriter(out);
        final HttpServletResponse resp = mock(HttpServletResponse.class);
        when(resp.getWriter()).thenReturn(pWriter);
        return resp;
    }

    @org.junit.jupiter.api.Test
    void getProducts() throws Exception {
        final GetProductsServlet getProducts = new GetProductsServlet();

        final StringWriter out = new StringWriter();
        final HttpServletResponse resp = getMockResponse(out);

        assertDoesNotThrow(() -> getProducts.doGet(null, resp));
        final String res = out.toString();
        assertTrue(res.contains("<html>"));
        assertTrue(res.contains("<body>"));
        assertTrue(res.contains("</body>"));
        assertTrue(res.contains("</html>"));
    }

    static class Params {
        int min;
        int max;
        int sum;
        int count;
    }

    static int getParam(final QueryServlet queryServlet, final String command, final int dflt) throws IOException {
        final StringWriter out = new StringWriter();
        final HttpServletResponse resp = getMockResponse(out);

        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("command")).thenReturn(command);
        queryServlet.doGet(req, resp);
        final String res = out.toString();
        final Pattern p = Pattern.compile("\\b[0-9]+\\b");
        final Matcher ans = p.matcher(res);
        if (!ans.find()) {
            return dflt;
        }
        return Integer.parseInt(ans.group(0));
    }

    static Params getParams(final QueryServlet queryServlet) throws IOException {
        final Params ret = new Params();
        ret.min = getParam(queryServlet, "min", Integer.MAX_VALUE);
        ret.max = getParam(queryServlet, "max", Integer.MIN_VALUE);
        ret.sum = getParam(queryServlet, "sum", 0);
        ret.count = getParam(queryServlet, "count", 0);
        return ret;
    }

    @org.junit.jupiter.api.Test
    void addAndQuery() throws Exception {
        final QueryServlet queryServlet = new QueryServlet();
        final Random rnd = new Random();

        for (int i = 0; i < 3; i++) {
            final Params initial = getParams(queryServlet);

            final AddProductServlet addServlet = new AddProductServlet();
            final HttpServletRequest req = mock(HttpServletRequest.class);
            final Clock clock = Clock.systemDefaultZone();
            final Instant instant = clock.instant();
            final String name = "test_" + i + "_" + instant.getNano();
            final int price = rnd.nextInt(100);
            when(req.getParameter("name")).thenReturn(name);
            when(req.getParameter("price")).thenReturn(Integer.toString(price));
            final HttpServletResponse resp = mock(HttpServletResponse.class);
            when(resp.getWriter()).thenReturn(mock(PrintWriter.class));
            addServlet.doGet(req, resp);

            final Params end = getParams(queryServlet);

            assertEquals(initial.count + 1, end.count);
            assertEquals(Integer.max(price, initial.max), end.max);
            assertEquals(Integer.min(price, initial.min), end.min);
            assertEquals(initial.sum + price, end.sum);

            Database.withStatement((stmt) -> {
                final String sql = "DELETE FROM PRODUCT WHERE name LIKE 'test_%'";
                stmt.executeUpdate(sql);
                return null;
            });
        }
    }
}