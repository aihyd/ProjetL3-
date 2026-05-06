package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.UtilisateurDAO;
import com.project.jdr.views.ChangePasswordView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePasswordController {

    private final UtilisateurDAO utilisateurDAO;

    public ChangePasswordController(ChangePasswordView view, AppTest app, int idUtilisateur, String username) {
        this.utilisateurDAO = new UtilisateurDAO();

        view.getBackToProfile().setOnMouseClicked(e -> app.showProfile(idUtilisateur, username));

        view.getConfirmButton().setOnAction(e -> {
            String currentPassword = view.getCurrentPasswordField().getText();
            String newPassword = view.getNewPasswordField().getText();
            String confirmNewPassword = view.getConfirmNewPasswordField().getText();

            afficherMessageNeutre(view, "");

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                afficherErreur(view, "Veuillez remplir tous les champs.");
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                afficherErreur(view, "Les nouveaux mots de passe ne correspondent pas.");
                return;
            }

            if (newPassword.length() < 6) {
                afficherErreur(view, "Le nouveau mot de passe doit contenir au moins 6 caractères.");
                return;
            }

            String currentPasswordHash = hashTexte(currentPassword);
            boolean connexionValide = utilisateurDAO.verifierConnexion(username, currentPasswordHash);

            if (!connexionValide) {
                afficherErreur(view, "Mot de passe actuel incorrect.");
                return;
            }

            String newPasswordHash = hashTexte(newPassword);
            boolean succes = utilisateurDAO.mettreAJourMotDePasse(username, newPasswordHash);

            if (succes) {
                afficherSucces(view, "Mot de passe modifié avec succès.");
                view.getCurrentPasswordField().clear();
                view.getNewPasswordField().clear();
                view.getConfirmNewPasswordField().clear();
            } else {
                afficherErreur(view, "Erreur lors de la modification du mot de passe.");
            }
        });
    }

    private String hashTexte(String texte) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(texte.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hash", e);
        }
    }

    private void afficherErreur(ChangePasswordView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-success");
        if (!view.getMessageLabel().getStyleClass().contains("message-error")) {
            view.getMessageLabel().getStyleClass().add("message-error");
        }
    }

    private void afficherSucces(ChangePasswordView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-error");
        if (!view.getMessageLabel().getStyleClass().contains("message-success")) {
            view.getMessageLabel().getStyleClass().add("message-success");
        }
    }

    private void afficherMessageNeutre(ChangePasswordView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-error", "message-success");
    }
}