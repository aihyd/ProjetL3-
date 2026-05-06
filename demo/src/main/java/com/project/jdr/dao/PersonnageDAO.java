package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Competence;
import com.project.jdr.model.Equipement;
import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Portrait;
import com.project.jdr.model.Stats;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnageDAO {

    private final FichePersonnageDAO ficheDAO      = new FichePersonnageDAO();
    private final StatsDAO           statsDAO      = new StatsDAO();
    private final PortraitDAO        portraitDAO   = new PortraitDAO();
    private final CompetenceDAO      competenceDAO = new CompetenceDAO();
    private final EquipementDAO      equipementDAO = new EquipementDAO();

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
                    if (rs.next()) return rs.getInt(1);
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
                    int idPersonnage = rs.getInt("id");

                    FichePersonnage fiche = ficheDAO.recupererFicheParPersonnage(idPersonnage);

                    if (fiche != null) {
                        List<Stats> stats = statsDAO.recupererStatsParFiche(fiche.getId());
                        for (Stats stat : stats) fiche.addStats(stat);

                        Portrait portrait = portraitDAO.recupererPortraitParFiche(fiche.getId());
                        if (portrait != null) fiche.setPortrait(portrait);

                        List<Competence> competences = competenceDAO.recupererCompetencesParFiche(fiche.getId());
                        for (Competence c : competences) fiche.addCompetence(c);

                        List<Equipement> equipements = equipementDAO.recupererEquipementsParFiche(fiche.getId());
                        for (Equipement e : equipements) fiche.addEquipement(e);
                    }

                    personnages.add(new Personnage(
                            idPersonnage,
                            rs.getString("nom"),
                            rs.getString("race"),
                            rs.getString("classe"),
                            rs.getInt("niveau"),
                            fiche
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur recuperation personnages : " + e.getMessage());
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
                while (rs.next()) noms.add(rs.getString("nom"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur recuperation noms personnages : " + e.getMessage());
        }
        return noms;
    }

    public boolean supprimerPersonnage(int idPersonnage) {
        try (Connection conn = ConnectionDb.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int idFiche = -1;
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT id FROM fiches_personnages WHERE id_personnage = ?")) {
                    ps.setInt(1, idPersonnage);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) idFiche = rs.getInt("id");
                    }
                }

                if (idFiche != -1) {
                    for (String table : new String[]{"statistiques", "competences", "equipements", "portraits"}) {
                        try (PreparedStatement ps = conn.prepareStatement(
                                "DELETE FROM " + table + " WHERE id_fiche = ?")) {
                            ps.setInt(1, idFiche);
                            ps.executeUpdate();
                        }
                    }
                    try (PreparedStatement ps = conn.prepareStatement(
                            "DELETE FROM fiches_personnages WHERE id = ?")) {
                        ps.setInt(1, idFiche);
                        ps.executeUpdate();
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM personnages WHERE id = ?")) {
                    ps.setInt(1, idPersonnage);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Erreur suppression personnage : " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Erreur connexion suppression : " + e.getMessage());
            return false;
        }
    }

    public boolean mettreAJourPersonnage(Personnage personnage) {
        String sql = """
                UPDATE personnages SET nom = ?, race = ?, classe = ?, niveau = ?
                WHERE id = ?
                """;
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, personnage.getNom());
            pstmt.setString(2, personnage.getRace());
            pstmt.setString(3, personnage.getClasse());
            pstmt.setInt(4, personnage.getNiveau());
            pstmt.setInt(5, personnage.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erreur mise a jour personnage : " + e.getMessage());
            return false;
        }
    }
}