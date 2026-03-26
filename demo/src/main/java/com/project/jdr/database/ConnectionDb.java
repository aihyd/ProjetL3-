
package com.project.jdr.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {

    private static final String URL = "jdbc:sqlite:fiche_personnages.db";

    private ConnectionDb() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}