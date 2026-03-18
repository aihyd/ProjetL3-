package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Personnage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO {

    public boolean ajouterPersonnage(Personnage personnage, int idUtilisateur) {
        String sql = "INSERT INTO personnages(nom, race, classe, niveau, id_user) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, personnage.getNom());
            pstmt.setString(2, personnage.getRace());
            pstmt.setString(3, personnage.getClasse());
            pstmt.setInt(4, personnage.getNiveau());
            pstmt.setInt(5, idUtilisateur);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout personnage : " + e.getMessage());
            return false;
        }
    }

    public Personnage rechercherParNomEtUtilisateur(String nom, int idUtilisateur) {
        String sql = "SELECT nom, race, classe, niveau FROM personnages WHERE nom = ? AND id_user = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setInt(2, idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Personnage(
                            rs.getString("nom"),
                            rs.getString("race"),
                            rs.getString("classe"),
                            rs.getInt("niveau")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur recherche personnage : " + e.getMessage());
        }

        return null;
    }

    public List<Personnage> listerPersonnagesParUtilisateur(int idUtilisateur) {
        List<Personnage> personnages = new ArrayList<>();
        String sql = "SELECT nom, race, classe, niveau FROM personnages WHERE id_user = ? ORDER BY nom";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnages.add(new Personnage(
                            rs.getString("nom"),
                            rs.getString("race"),
                            rs.getString("classe"),
                            rs.getInt("niveau")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur liste personnages : " + e.getMessage());
        }

        return personnages;
    }

    public Integer recupererIdPersonnage(String nom, int idUtilisateur) {
        String sql = "SELECT id FROM personnages WHERE nom = ? AND id_user = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setInt(2, idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération id personnage : " + e.getMessage());
        }

        return null;
    }

    public boolean mettreAJourPersonnage(Personnage personnage, int idUtilisateur, String ancienNom) {
        String sql = "UPDATE personnages SET nom = ?, race = ?, classe = ?, niveau = ? WHERE nom = ? AND id_user = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, personnage.getNom());
            pstmt.setString(2, personnage.getRace());
            pstmt.setString(3, personnage.getClasse());
            pstmt.setInt(4, personnage.getNiveau());
            pstmt.setString(5, ancienNom);
            pstmt.setInt(6, idUtilisateur);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour personnage : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerPersonnage(String nom, int idUtilisateur) {
        String sql = "DELETE FROM personnages WHERE nom = ? AND id_user = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setInt(2, idUtilisateur);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression personnage : " + e.getMessage());
            return false;
        }
    }
    public List<String> recupererNomsPersonnagesParUtilisateur(int idUtilisateur) {
        List<String> noms = new ArrayList<>();

        String sql = "SELECT nom FROM personnages WHERE id_user = ? ORDER BY id DESC";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    noms.add(rs.getString("nom"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération personnages : " + e.getMessage());
        }

        return noms;
    }
}