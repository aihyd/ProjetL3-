package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Personnage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO {

    public int ajouterPersonnage(Personnage personnage, int idUtilisateur) {
        String sql = """
                INSERT INTO personnages(nom, race, classe, niveau, id_user)
                VALUES(?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, personnage.getNom());
            pstmt.setString(2, personnage.getRace());
            pstmt.setString(3, personnage.getClasse());
            pstmt.setInt(4, personnage.getNiveau());
            pstmt.setInt(5, idUtilisateur);

            int lignes = pstmt.executeUpdate();

            if (lignes > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur ajout personnage : " + e.getMessage());
        }

        return -1;
    }

    public List<Personnage> recupererPersonnagesParUtilisateur(int idUtilisateur) {
        List<Personnage> personnages = new ArrayList<>();

        String sql = """
                SELECT id, nom, race, classe, niveau
                FROM personnages
                WHERE id_user = ?
                ORDER BY id DESC
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    personnages.add(new Personnage(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("race"),
                            rs.getString("classe"),
                            rs.getInt("niveau"),
                            null
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération personnages : " + e.getMessage());
        }

        return personnages;
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
            System.out.println("Erreur récupération noms personnages : " + e.getMessage());
        }

        return noms;
    }
}