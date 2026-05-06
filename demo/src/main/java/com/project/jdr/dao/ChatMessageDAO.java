package com.project.jdr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.project.jdr.database.ConnectionDb;

public class ChatMessageDAO {

    // Sauvegarder un message
    public static void sauvegarderMessage(int idUser, String role, String message) {
        String sql = "INSERT INTO chat_messages (id_user, role, message) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.setString(2, role);
            stmt.setString(3, message);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur sauvegarde message : " + e.getMessage());
        }
    }

    // Récupérer tous les messages d'un utilisateur
    public static List<String[]> recupererMessages(int idUser) {
        List<String[]> messages = new ArrayList<>();
        String sql = "SELECT role, message FROM chat_messages WHERE id_user = ? ORDER BY date_envoi ASC";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new String[]{rs.getString("role"), rs.getString("message")});
            }
        } catch (SQLException e) {
            System.err.println("Erreur recuperation messages : " + e.getMessage());
        }
        return messages;
    }

    // Supprimer tous les messages d'un utilisateur
    public static void supprimerMessages(int idUser) {
        String sql = "DELETE FROM chat_messages WHERE id_user = ?";
        try (Connection conn = ConnectionDb.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUser);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur suppression messages : " + e.getMessage());
        }
    }
}