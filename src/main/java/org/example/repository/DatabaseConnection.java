package org.example.repository;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {  // 4 usages | related problem
    @Getter
    private final static DatabaseConnection instance = new DatabaseConnection();
    private final static String url = "jdbc:postgresql://localhost:5432/wishlist"; // usage
    private final static String user = "postgres"; // usage
    private final static String password = "1234"; // usage
    private static Connection conn; // 3 usages

    static {
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private DatabaseConnection() { // 1 usage | related problem
    }

    @SneakyThrows
    public Statement getStatement() {
        return conn.createStatement();
    }

    @SneakyThrows
    public void close() {
        conn.close();
    }
}
