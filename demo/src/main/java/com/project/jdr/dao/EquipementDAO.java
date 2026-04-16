package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Equipement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementDAO {

    public int ajouterEquipement(Equipement equipement, int idFiche) {
        String sql = """
                INSERT INTO equipements(nom, description, x, y, width, height, id_fiche)
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, equipement.getNom());
            pstmt.setString(2, equipement.getDescription());
            pstmt.setInt(3, equipement.getX());
            pstmt.setInt(4, equipement.getY());
            pstmt.setDouble(5, equipement.getWidth());
            pstmt.setDouble(6, equipement.getHeight());
            pstmt.setInt(7, idFiche);
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Erreur ajout équipement : " + e.getMessage());
        }
        return -1;
    }

    public List<Equipement> recupererEquipementsParFiche(int idFiche) {
        List<Equipement> equipements = new ArrayList<>();
        String sql = """
                SELECT id, nom, description, x, y, width, height
                FROM equipements
                WHERE id_fiche = ?
                """;
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFiche);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    equipements.add(new Equipement(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getDouble("width"),
                            rs.getDouble("height")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération équipements : " + e.getMessage());
        }
        return equipements;
    }

    public boolean supprimerEquipement(int id) {
        String sql = "DELETE FROM equipements WHERE id = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur suppression équipement : " + e.getMessage());
            return false;
        }
    }

    public List<Equipement> recupererEquipementsParUtilisateur(int idUtilisateur) {
        List<Equipement> equipements = new ArrayList<>();
        String sql = """
                SELECT DISTINCT e.id, e.nom, e.description, e.x, e.y, e.width, e.height
                FROM equipements e
                JOIN fiches_personnages fp ON e.id_fiche = fp.id
                JOIN personnages p ON fp.id_personnage = p.id
                WHERE p.id_user = ?
                ORDER BY e.nom ASC
                """;
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUtilisateur);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    equipements.add(new Equipement(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getInt("x"),
                            rs.getInt("y"),
                            rs.getDouble("width"),
                            rs.getDouble("height")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur récupération équipements utilisateur : " + e.getMessage());
        }
        return equipements;
    }

   
   public boolean mettreAJourNomEtDescription(int id, String nom, String description) {
    String sql = "UPDATE equipements SET nom = ?, description = ? WHERE id = ?";
    try (Connection conn = ConnectionDb.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, nom);
        pstmt.setString(2, description);
        pstmt.setInt(3, id);
        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Erreur mise a jour equipement : " + e.getMessage());
        return false;
    }
}
   
    public boolean mettreAJourPositionEtTaille(int id, int x, int y, double width, double height) {
        String sql = "UPDATE equipements SET x = ?, y = ?, width = ?, height = ? WHERE id = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, x);
            pstmt.setInt(2, y);
            pstmt.setDouble(3, width);
            pstmt.setDouble(4, height);
            pstmt.setInt(5, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur mise à jour équipement : " + e.getMessage());
            return false;
        }
    }
}