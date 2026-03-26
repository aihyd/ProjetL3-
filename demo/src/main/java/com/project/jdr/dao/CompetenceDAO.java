package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Competence;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompetenceDAO {

    public int ajouterCompetence(Competence competence, int idFiche) {
        String sql = """
                INSERT INTO competences(nom, description, x, y, width, height, id_fiche)
                VALUES(?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, competence.getNom());
            pstmt.setString(2, competence.getDescription());
            pstmt.setInt(3, competence.getX());
            pstmt.setInt(4, competence.getY());
            pstmt.setDouble(5, competence.getWidth());
            pstmt.setDouble(6, competence.getHeight());
            pstmt.setInt(7, idFiche);

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur ajout compétence : " + e.getMessage());
        }

        return -1;
    }

    public List<Competence> recupererCompetencesParFiche(int idFiche) {
        List<Competence> competences = new ArrayList<>();

        String sql = """
                SELECT id, nom, description, x, y, width, height
                FROM competences
                WHERE id_fiche = ?
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    competences.add(new Competence(
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
            System.out.println("Erreur récupération compétences : " + e.getMessage());
        }

        return competences;
    }

    public boolean mettreAJourPositionEtTaille(int id, int x, int y, double width, double height) {
        String sql = "UPDATE competences SET x = ?, y = ?, width = ?, height = ? WHERE id = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, x);
            pstmt.setInt(2, y);
            pstmt.setDouble(3, width);
            pstmt.setDouble(4, height);
            pstmt.setInt(5, id);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour compétence : " + e.getMessage());
            return false;
        }
    }
}