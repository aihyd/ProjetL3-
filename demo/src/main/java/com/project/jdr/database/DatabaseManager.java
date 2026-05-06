package com.project.jdr.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:jdr.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    public static void initialize() {
        String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL," +
            "secret_question TEXT NOT NULL," +
            "secret_answer TEXT NOT NULL" +
            ");";

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(createUsers);
            System.out.println("Base de données initialisée.");
        } catch (SQLException e) {
            System.err.println("Erreur init DB: " + e.getMessage());
        }
    }
}