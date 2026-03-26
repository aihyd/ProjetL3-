package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Portrait;

import java.sql.*;

public class PortraitDAO {

    public int ajouterPortrait(Portrait portrait, int idFiche) {
        String sql = """
                INSERT INTO portraits(chemin_image, x, y, width, height, id_fiche)
                VALUES(?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, portrait.getCheminImage());
            pstmt.setInt(2, portrait.getX());
            pstmt.setInt(3, portrait.getY());
            pstmt.setDouble(4, portrait.getWidth());
            pstmt.setDouble(5, portrait.getHeight());
            pstmt.setInt(6, idFiche);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur ajout portrait : " + e.getMessage());
        }

        return -1;
    }

    public Portrait recupererPortraitParFiche(int idFiche) {
        String sql = """
                SELECT id, chemin_image, x, y, width, height
                FROM portraits
                WHERE id_fiche = ?
                LIMIT 1
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Portrait(
                            rs.getInt("id"),
                            rs.getString("chemin_image"),
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getDouble("width"),
                            rs.getDouble("height")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération portrait : " + e.getMessage());
        }

        return null;
    }

    public boolean mettreAJourPositionEtTaille(int id, int x, int y, double width, double height) {
        String sql = "UPDATE portraits SET x = ?, y = ?, width = ?, height = ? WHERE id = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, x);
            pstmt.setInt(2, y);
            pstmt.setDouble(3, width);
            pstmt.setDouble(4, height);
            pstmt.setInt(5, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour portrait : " + e.getMessage());
            return false;
        }
    }
}