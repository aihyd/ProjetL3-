package com.project.jdr.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InitialisationDb {

    private InitialisationDb() {
    }

    public static void initialiser() {
        System.out.println("Dossier execution : " + System.getProperty("user.dir"));

        try (Connection conn = ConnectionDb.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id_user INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom_utilisateur TEXT NOT NULL UNIQUE,
                    mot_de_passe_hash TEXT NOT NULL,
                    question_secrete TEXT NOT NULL,
                    reponse_secrete_hash TEXT NOT NULL
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS personnages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    race TEXT NOT NULL,
                    classe TEXT NOT NULL,
                    niveau INTEGER NOT NULL,
                    id_user INTEGER NOT NULL,
                    FOREIGN KEY (id_user) REFERENCES users(id_user)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS fiches_personnages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    biographie TEXT,
                    id_personnage INTEGER NOT NULL UNIQUE,
                    FOREIGN KEY (id_personnage) REFERENCES personnages(id)
                )
            """);
            stmt.execute("""
    CREATE TABLE IF NOT EXISTS chat_messages (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        id_user INTEGER NOT NULL,
        role TEXT NOT NULL,
        message TEXT NOT NULL,
        date_envoi DATETIME DEFAULT CURRENT_TIMESTAMP,
        FOREIGN KEY (id_user) REFERENCES users(id_user)
    )
""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS statistiques (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    valeur INTEGER NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    width REAL NOT NULL,
                    height REAL NOT NULL,
                    id_fiche INTEGER NOT NULL,
                    FOREIGN KEY (id_fiche) REFERENCES fiches_personnages(id)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS competences (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    description TEXT,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    width REAL NOT NULL,
                    height REAL NOT NULL,
                    id_fiche INTEGER NOT NULL,
                    FOREIGN KEY (id_fiche) REFERENCES fiches_personnages(id)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS equipements (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    description TEXT,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    width REAL NOT NULL,
                    height REAL NOT NULL,
                    id_fiche INTEGER NOT NULL,
                    FOREIGN KEY (id_fiche) REFERENCES fiches_personnages(id)
                )
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS portraits (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    chemin_image TEXT NOT NULL,
                    x INTEGER NOT NULL,
                    y INTEGER NOT NULL,
                    width REAL NOT NULL,
                    height REAL NOT NULL,
                    id_fiche INTEGER NOT NULL,
                    FOREIGN KEY (id_fiche) REFERENCES fiches_personnages(id)
                )
            """);

            System.out.println("Base de données initialisée.");

        } catch (SQLException e) {
            System.out.println("Erreur initialisation DB : " + e.getMessage());
        }
    }
}