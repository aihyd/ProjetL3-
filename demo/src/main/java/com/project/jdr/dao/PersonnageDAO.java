package com.project.jdr.database;

<<<<<<< HEAD
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
=======
import java.sql.*;
>>>>>>> origin/djilali
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO {

<<<<<<< HEAD
    // Créer un personnage + sa fiche + ses stats
    public static boolean creerPersonnage(String nom, String race, String classe,
                                           int niveau, String biographie,
                                           int force, int agilite, int intelligence, int endurance,
                                           int idUser) {
        try (Connection conn = ConnectionDb.getConnection()) {
            conn.setAutoCommit(false);

            // 1. Insérer le personnage
            int idPersonnage = -1;
            String sqlPersonnage = "INSERT INTO personnages (nom, race, classe, niveau, id_user) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPersonnage, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, nom);
                stmt.setString(2, race);
                stmt.setString(3, classe);
                stmt.setInt(4, niveau);
                stmt.setInt(5, idUser);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) idPersonnage = rs.getInt(1);
            }
=======
    public int ajouterPersonnage(Personnage personnage, int idUtilisateur) {
        String sql = """
                INSERT INTO personnages(nom, race, classe, niveau, id_user)
                VALUES(?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
>>>>>>> origin/djilali

            // 2. Insérer la fiche personnage
            int idFiche = -1;
            String sqlFiche = "INSERT INTO fiches_personnages (biographie, id_personnage) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlFiche, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, biographie);
                stmt.setInt(2, idPersonnage);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) idFiche = rs.getInt(1);
            }

<<<<<<< HEAD
            // 3. Insérer les stats
            String sqlStat = "INSERT INTO statistiques (nom, valeur, x, y, id_fiche) VALUES (?, ?, 0, 0, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlStat)) {
                int[][] stats = {
                    {force, 0}, {agilite, 1}, {intelligence, 2}, {endurance, 3}
                };
                String[] noms = {"Force", "Agilité", "Intelligence", "Endurance"};
                for (int i = 0; i < noms.length; i++) {
                    stmt.setString(1, noms[i]);
                    stmt.setInt(2, stats[i][0]);
                    stmt.setInt(3, idFiche);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur creerPersonnage: " + e.getMessage());
            return false;
        }
    }

    // Récupérer les noms des personnages d'un utilisateur
    public static List<String> getPersonnagesParUser(int idUser) {
=======
            int lignes = pstmt.executeUpdate();

            if (lignes > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur ajout personnage : " + e.getMessage());
        }

        return -1;
    }

    public List<Personnage> recupererPersonnagesParUtilisateur(int idUtilisateur) {
        List<Personnage> personnages = new ArrayList<>();

        String sql = """
                SELECT id, nom, race, classe, niveau
                FROM personnages
                WHERE id_user = ?
                ORDER BY id DESC
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnages.add(new Personnage(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("race"),
                            rs.getString("classe"),
                            rs.getInt("niveau"),
                            null
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération personnages : " + e.getMessage());
        }

        return personnages;
    }

    public List<String> recupererNomsPersonnagesParUtilisateur(int idUtilisateur) {
>>>>>>> origin/djilali
        List<String> noms = new ArrayList<>();
        String sql = "SELECT nom FROM personnages WHERE id_user = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) noms.add(rs.getString("nom"));
        } catch (SQLException e) {
<<<<<<< HEAD
            System.err.println("Erreur getPersonnages: " + e.getMessage());
=======
            System.out.println("Erreur récupération noms personnages : " + e.getMessage());
>>>>>>> origin/djilali
        }
        return noms;
    }
}