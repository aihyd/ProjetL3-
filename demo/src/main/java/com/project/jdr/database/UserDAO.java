package com.project.jdr.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Inscription
    public static boolean register(String username, String password,
                                   String secretQuestion, String secretAnswer) {
        String sql = "INSERT INTO users (username, password, secret_question, secret_answer) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, secretQuestion);
            stmt.setString(4, secretAnswer);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur register: " + e.getMessage());
            return false;
        }
    }

    // Connexion
    public static boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Erreur login: " + e.getMessage());
            return false;
        }
    }

    // Récupérer la question secrète d'un utilisateur
    public static String getSecretQuestion(String username) {
        String sql = "SELECT secret_question FROM users WHERE username = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("secret_question");
        } catch (SQLException e) {
            System.err.println("Erreur getSecretQuestion: " + e.getMessage());
        }
        return null;
    }

    // Réinitialiser le mot de passe
    public static boolean resetPassword(String username, String secretAnswer, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE username = ? AND secret_answer = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.setString(3, secretAnswer);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Erreur resetPassword: " + e.getMessage());
            return false;
        }
    }
}