package com.project.jdr.dao;

import com.project.jdr.database.ConnectionDb;
import com.project.jdr.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    public boolean ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = """
                INSERT INTO users(
                    nom_utilisateur,
                    mot_de_passe_hash,
                    question_secrete,
                    reponse_secrete_hash
                ) VALUES(?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getNomUtilisateur());
            pstmt.setString(2, utilisateur.getMotDePasseHash());
            pstmt.setString(3, utilisateur.getQuestionSecrete());
            pstmt.setString(4, utilisateur.getReponseSecreteHash());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erreur ajout utilisateur : " + e.getMessage());
            return false;
        }
    }

    public Utilisateur rechercherParNom(String nomUtilisateur) {
        String sql = """
                SELECT nom_utilisateur, mot_de_passe_hash,
                       question_secrete, reponse_secrete_hash
                FROM users
                WHERE nom_utilisateur = ?
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Utilisateur(
                            rs.getString("nom_utilisateur"),
                            rs.getString("mot_de_passe_hash"),
                            rs.getString("question_secrete"),
                            rs.getString("reponse_secrete_hash")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur recherche utilisateur : " + e.getMessage());
        }

        return null;
    }

    public boolean utilisateurExiste(String nomUtilisateur) {
        return rechercherParNom(nomUtilisateur) != null;
    }

    public boolean verifierConnexion(String nomUtilisateur, String motDePasseHash) {
        String sql = "SELECT 1 FROM users WHERE nom_utilisateur = ? AND mot_de_passe_hash = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);
            pstmt.setString(2, motDePasseHash);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Erreur connexion utilisateur : " + e.getMessage());
            return false;
        }
    }

    public Integer recupererIdUtilisateur(String nomUtilisateur) {
        String sql = "SELECT id_user FROM users WHERE nom_utilisateur = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_user");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération id utilisateur : " + e.getMessage());
        }

        return null;
    }

    public boolean supprimerUtilisateur(String nomUtilisateur) {
        String sql = "DELETE FROM users WHERE nom_utilisateur = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur suppression utilisateur : " + e.getMessage());
            return false;
        }
    }

    public boolean verifierReponseSecrete(String nomUtilisateur, String reponseSecreteHash) {
        String sql = """
                SELECT 1 FROM users
                WHERE nom_utilisateur = ? AND reponse_secrete_hash = ?
                """;

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);
            pstmt.setString(2, reponseSecreteHash);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Erreur vérification réponse secrète : " + e.getMessage());
            return false;
        }
    }

    public String recupererQuestionSecrete(String nomUtilisateur) {
        String sql = "SELECT question_secrete FROM users WHERE nom_utilisateur = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("question_secrete");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération question secrète : " + e.getMessage());
        }

        return null;
    }

    public String recupererReponseSecreteHash(String nomUtilisateur) {
        String sql = "SELECT reponse_secrete_hash FROM users WHERE nom_utilisateur = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomUtilisateur);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("reponse_secrete_hash");
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur récupération réponse secrète : " + e.getMessage());
        }

        return null;
    }

    public boolean mettreAJourMotDePasse(String nomUtilisateur, String nouveauMotDePasseHash) {
        String sql = "UPDATE users SET mot_de_passe_hash = ? WHERE nom_utilisateur = ?";

        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nouveauMotDePasseHash);
            pstmt.setString(2, nomUtilisateur);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erreur mise à jour mot de passe : " + e.getMessage());
            return false;
        }
    }
}