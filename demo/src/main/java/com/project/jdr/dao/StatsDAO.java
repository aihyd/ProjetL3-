package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsDAO {

    public boolean ajouterStat(Stats stat, int idFiche) {
        String sql = "INSERT INTO statistiques(nom, valeur, x, y, id_fiche) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, stat.getNom());
            pstmt.setInt(2, stat.getValeur());
            pstmt.setInt(3, stat.getX());
            pstmt.setInt(4, stat.getY());
            pstmt.setInt(5, idFiche);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout stat : " + e.getMessage());
            return false;
        }
    }

    public List<Stats> listerStatsParFiche(int idFiche) {
        List<Stats> stats = new ArrayList<>();
        String sql = "SELECT nom, valeur, x, y FROM statistiques WHERE id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idFiche);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stats.add(new Stats(
                            rs.getString("nom"),
                            rs.getInt("valeur"),
                            rs.getInt("x"),
                            rs.getInt("y")
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur lecture stats : " + e.getMessage());
        }

        return stats;
    }

    public boolean mettreAJourStat(String ancienNom, Stats stat, int idFiche) {
        String sql = "UPDATE statistiques SET nom = ?, valeur = ?, x = ?, y = ? WHERE nom = ? AND id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, stat.getNom());
            pstmt.setInt(2, stat.getValeur());
            pstmt.setInt(3, stat.getX());
            pstmt.setInt(4, stat.getY());
            pstmt.setString(5, ancienNom);
            pstmt.setInt(6, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour stat : " + e.getMessage());
            return false;
        }
    }

    public boolean supprimerStat(String nom, int idFiche) {
        String sql = "DELETE FROM statistiques WHERE nom = ? AND id_fiche = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nom);
            pstmt.setInt(2, idFiche);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression stat : " + e.getMessage());
            return false;
        }
    }
}