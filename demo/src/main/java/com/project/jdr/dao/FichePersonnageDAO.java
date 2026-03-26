package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.FichePersonnage;

import java.sql.*;

public class FichePersonnageDAO {

    public int creerFichePourPersonnage(int idPersonnage) {
        String sql = "INSERT INTO fiches_personnages(biographie, id_personnage) VALUES(?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, "");
            pstmt.setInt(2, idPersonnage);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur création fiche : " + e.getMessage());
        }

        return -1;
    }

    public FichePersonnage recupererFicheParPersonnage(int idPersonnage) {
        String sql = "SELECT id, biographie FROM fiches_personnages WHERE id_personnage = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonnage);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new FichePersonnage(
                            rs.getInt("id"),
                            rs.getString("biographie")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération fiche : " + e.getMessage());
        }

        return null;
    }

    public boolean mettreAJourBiographie(int idFiche, String biographie) {
        String sql = "UPDATE fiches_personnages SET biographie = ? WHERE id = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, biographie);
            pstmt.setInt(2, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour biographie : " + e.getMessage());
            return false;
        }
    }
}