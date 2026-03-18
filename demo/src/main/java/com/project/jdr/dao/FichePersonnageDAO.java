package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.FichePersonnage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FichePersonnageDAO {

    public boolean ajouterFiche(FichePersonnage fiche, int idPersonnage) {
        String sql = "INSERT INTO fiches_personnages(biographie, id_personnage) VALUES(?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, fiche.getBiographie());
            pstmt.setInt(2, idPersonnage);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout fiche personnage : " + e.getMessage());
            return false;
        }
    }

    public Integer recupererIdFiche(int idPersonnage) {
        String sql = "SELECT id FROM fiches_personnages WHERE id_personnage = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonnage);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération id fiche : " + e.getMessage());
        }

        return null;
    }

    public FichePersonnage chargerFiche(int idPersonnage) {
        String sql = "SELECT biographie FROM fiches_personnages WHERE id_personnage = ?";
        FichePersonnage fiche = null;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonnage);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    fiche = new FichePersonnage();
                    fiche.setBiographie(rs.getString("biographie"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement fiche : " + e.getMessage());
        }

        return fiche;
    }

    public boolean mettreAJourBiographie(int idPersonnage, String biographie) {
        String sql = "UPDATE fiches_personnages SET biographie = ? WHERE id_personnage = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, biographie);
            pstmt.setInt(2, idPersonnage);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour biographie : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerFiche(int idPersonnage) {
        String sql = "DELETE FROM fiches_personnages WHERE id_personnage = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idPersonnage);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression fiche : " + e.getMessage());
            return false;
        }
    }
}