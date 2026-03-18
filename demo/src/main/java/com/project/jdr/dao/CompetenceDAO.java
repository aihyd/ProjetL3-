package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Competence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompetenceDAO {

    public boolean ajouterCompetence(Competence competence, int idFiche) {
        String sql = "INSERT INTO competences(nom, description, x, y, id_fiche) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, competence.getNom());
            pstmt.setString(2, competence.getDescription());
            pstmt.setInt(3, competence.getX());
            pstmt.setInt(4, competence.getY());
            pstmt.setInt(5, idFiche);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout compétence : " + e.getMessage());
            return false;
        }
    }

    public List<Competence> listerCompetencesParFiche(int idFiche) {
        List<Competence> competences = new ArrayList<>();
        String sql = "SELECT nom, description, x, y FROM competences WHERE id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    competences.add(new Competence(
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getInt("x"),
                            rs.getInt("y")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lecture compétences : " + e.getMessage());
        }

        return competences;
    }

    public boolean mettreAJourCompetence(String ancienNom, Competence competence, int idFiche) {
        String sql = "UPDATE competences SET nom = ?, description = ?, x = ?, y = ? WHERE nom = ? AND id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, competence.getNom());
            pstmt.setString(2, competence.getDescription());
            pstmt.setInt(3, competence.getX());
            pstmt.setInt(4, competence.getY());
            pstmt.setString(5, ancienNom);
            pstmt.setInt(6, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour compétence : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerCompetence(String nom, int idFiche) {
        String sql = "DELETE FROM competences WHERE nom = ? AND id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setInt(2, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression compétence : " + e.getMessage());
            return false;
        }
    }
}