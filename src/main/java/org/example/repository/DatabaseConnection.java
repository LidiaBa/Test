package org.example.repository;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {
    private final static String url = "jdbc:postgresql://localhost:5432/journal";
    private final static String user = "postgres";
    private final static String password = "p@proxima";
    private static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @SneakyThrows
    public Statement getStatement() {
        return conn.createStatement();
    }
}
