package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.UtilisateurDAO;
import com.project.jdr.views.DeleteAccountView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DeleteAccountController {

    private final UtilisateurDAO utilisateurDAO;

    public DeleteAccountController(DeleteAccountView view, AppTest app, int idUtilisateur, String username) {
        this.utilisateurDAO = new UtilisateurDAO();

        view.getBackToProfile().setOnMouseClicked(e -> app.showProfile(idUtilisateur, username));

        view.getConfirmButton().setOnAction(e -> {
            String password = view.getPasswordField().getText();

            afficherMessageNeutre(view, "");

            if (password.isEmpty()) {
                afficherErreur(view, "Veuillez entrer votre mot de passe.");
                return;
            }

            String passwordHash = hashTexte(password);
            boolean connexionValide = utilisateurDAO.verifierConnexion(username, passwordHash);

            if (!connexionValide) {
                afficherErreur(view, "Mot de passe incorrect.");
                return;
            }

            boolean succes = utilisateurDAO.supprimerUtilisateur(username);

            if (succes) {
                app.showLogin();
            } else {
                afficherErreur(view, "Erreur lors de la suppression du compte.");
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

    private void afficherErreur(DeleteAccountView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-success");
        if (!view.getMessageLabel().getStyleClass().contains("message-error")) {
            view.getMessageLabel().getStyleClass().add("message-error");
        }
    }

    private void afficherMessageNeutre(DeleteAccountView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-error", "message-success");
    }
}