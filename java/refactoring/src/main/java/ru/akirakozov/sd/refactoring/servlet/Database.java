package ru.akirakozov.sd.refactoring.servlet;

import java.sql.*;

public class Database {
    public static final String dbAddress = "jdbc:sqlite:test.db";

    public interface WithStatement<T> {
        T handle(Statement s) throws SQLException;
    }

    public interface DBHandler<T> {
        T handle(ResultSet rs) throws SQLException;
    }

    public static <T> T withStatement(final WithStatement<T> f, final String dbAddress) throws SQLException {
        try (final Connection c = DriverManager.getConnection(dbAddress)) {
            try (final Statement stmt = c.createStatement()) {
                return f.handle(stmt);
            }
        }
    }

    public static <T> T withStatement(final WithStatement<T> f) throws SQLException {
        return withStatement(f, dbAddress);
    }

    public static <T> T querySql(final String query, final DBHandler<T> f, final String dbAddress) throws SQLException {
        return withStatement((stmt) -> {
            final ResultSet rs = stmt.executeQuery(query);
            return f.handle(rs);
        }, dbAddress);
    }

    public static <T> T querySql(final String query, final DBHandler<T> f) throws SQLException {
        return querySql(query, f, Database.dbAddress);
    }

    public static <T> T querySqlUnchecked(final String query, final DBHandler<T> f) {
        try {
            return querySql(query, f);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
