package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Portrait;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PortraitDAO {

    public boolean ajouterPortrait(Portrait portrait, int idFiche) {
        String sql = "INSERT INTO portraits(chemin_image, x, y, id_fiche) VALUES(?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, portrait.getCheminImage());
            pstmt.setInt(2, portrait.getX());
            pstmt.setInt(3, portrait.getY());
            pstmt.setInt(4, idFiche);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout portrait : " + e.getMessage());
            return false;
        }
    }

    public Portrait chargerPortrait(int idFiche) {
        String sql = "SELECT chemin_image, x, y FROM portraits WHERE id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Portrait(
                            rs.getString("chemin_image"),
                            rs.getInt("x"),
                            rs.getInt("y")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement portrait : " + e.getMessage());
        }

        return null;
    }

    public boolean mettreAJourPortrait(Portrait portrait, int idFiche) {
        String sql = "UPDATE portraits SET chemin_image = ?, x = ?, y = ? WHERE id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, portrait.getCheminImage());
            pstmt.setInt(2, portrait.getX());
            pstmt.setInt(3, portrait.getY());
            pstmt.setInt(4, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour portrait : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerPortrait(int idFiche) {
        String sql = "DELETE FROM portraits WHERE id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression portrait : " + e.getMessage());
            return false;
        }
    }
}