package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Stats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatsDAO {

    public int ajouterStat(Stats stat, int idFiche) {
        String sql = """
                INSERT INTO statistiques(nom, valeur, x, y, width, height, id_fiche)
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, stat.getNom());
            pstmt.setInt(2, stat.getValeur());
            pstmt.setInt(3, stat.getX());
            pstmt.setInt(4, stat.getY());
            pstmt.setDouble(5, stat.getWidth());
            pstmt.setDouble(6, stat.getHeight());
            pstmt.setInt(7, idFiche);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur ajout stat : " + e.getMessage());
        }

        return -1;
    }

    public List<Stats> recupererStatsParFiche(int idFiche) {
        List<Stats> stats = new ArrayList<>();

        String sql = """
                SELECT id, nom, valeur, x, y, width, height
                FROM statistiques
                WHERE id_fiche = ?
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stats.add(new Stats(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getInt("valeur"),
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getDouble("width"),
                            rs.getDouble("height")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération stats : " + e.getMessage());
        }

        return stats;
    }

    public boolean mettreAJourValeur(int id, int valeur) {
    String sql = "UPDATE statistiques SET valeur = ? WHERE id = ?";
    try (Connection conn = ConnectionDb.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, valeur);
        pstmt.setInt(2, id);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Erreur mise a jour stat : " + e.getMessage());
        return false;
    }
} 

    public boolean mettreAJourPositionEtTaille(int id, int x, int y, double width, double height) {
        String sql = "UPDATE statistiques SET x = ?, y = ?, width = ?, height = ? WHERE id = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, x);
            pstmt.setInt(2, y);
            pstmt.setDouble(3, width);
            pstmt.setDouble(4, height);
            pstmt.setInt(5, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour stat : " + e.getMessage());
            return false;
        }
    }
}