package ru.akirakozov.sd.refactoring.servlet;

import java.sql.*;
import java.util.function.Function;

public class Defs {
    public static final String dbAddress = "jdbc:sqlite:test.db";

    public interface DBHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }

    public static <T> T querySql(final String query, final DBHandler<T> f, final String dbAddress) throws SQLException {
        try (final Connection c = DriverManager.getConnection(dbAddress)) {
            try (final Statement stmt = c.createStatement()) {
                final ResultSet rs = stmt.executeQuery(query);
                return f.handle(rs);
            }
        }
    }

    public static <T> T querySql(final String query, final DBHandler<T> f) throws SQLException {
        return querySql(query, f, Defs.dbAddress);
    }

    public static <T> T querySqlUnchecked(final String query, final DBHandler<T> f) {
        try {
            return querySql(query, f);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
