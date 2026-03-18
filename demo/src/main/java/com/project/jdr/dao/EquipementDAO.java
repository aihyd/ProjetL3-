package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Equipement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EquipementDAO {

    public boolean ajouterEquipement(Equipement equipement, int idFiche) {
        String sql = "INSERT INTO equipements(nom, description, x, y, id_fiche) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipement.getNom());
            pstmt.setString(2, equipement.getDescription());
            pstmt.setInt(3, equipement.getX());
            pstmt.setInt(4, equipement.getY());
            pstmt.setInt(5, idFiche);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout équipement : " + e.getMessage());
            return false;
        }
    }

    public List<Equipement> listerEquipementsParFiche(int idFiche) {
        List<Equipement> equipements = new ArrayList<>();
        String sql = "SELECT nom, description, x, y FROM equipements WHERE id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    equipements.add(new Equipement(
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getInt("x"),
                            rs.getInt("y")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lecture équipements : " + e.getMessage());
        }

        return equipements;
    }

    public boolean mettreAJourEquipement(String ancienNom, Equipement equipement, int idFiche) {
        String sql = "UPDATE equipements SET nom = ?, description = ?, x = ?, y = ? WHERE nom = ? AND id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, equipement.getNom());
            pstmt.setString(2, equipement.getDescription());
            pstmt.setInt(3, equipement.getX());
            pstmt.setInt(4, equipement.getY());
            pstmt.setString(5, ancienNom);
            pstmt.setInt(6, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour équipement : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerEquipement(String nom, int idFiche) {
        String sql = "DELETE FROM equipements WHERE nom = ? AND id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setInt(2, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression équipement : " + e.getMessage());
            return false;
        }
    }
}