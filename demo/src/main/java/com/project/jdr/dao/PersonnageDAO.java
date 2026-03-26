package com.project.jdr.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO {

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
        List<String> noms = new ArrayList<>();
        String sql = "SELECT nom FROM personnages WHERE id_user = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) noms.add(rs.getString("nom"));
        } catch (SQLException e) {
            System.err.println("Erreur getPersonnages: " + e.getMessage());
        }
        return noms;
    }
}