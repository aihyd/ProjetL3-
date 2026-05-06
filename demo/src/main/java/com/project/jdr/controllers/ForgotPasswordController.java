package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.UtilisateurDAO;
import com.project.jdr.views.ForgotPasswordView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ForgotPasswordController {

    private final UtilisateurDAO utilisateurDAO;
    private String usernameVerifie;

    public ForgotPasswordController(ForgotPasswordView view, AppTest app) {
        this.utilisateurDAO = new UtilisateurDAO();
        this.usernameVerifie = null;

        
        
        view.getBackToLogin().setOnMouseClicked(e -> app.showLogin());

        view.getVerifyButton().setOnAction(e -> {
            String username = view.getUsernameField().getText().trim();

            view.getMessageLabel().setText("");
            view.reinitialiserEtapeQuestion();
            usernameVerifie = null;

            if (username.isEmpty()) {
                view.getMessageLabel().setText("Veuillez entrer un nom d'utilisateur.");
                return;
            }

            String questionSecrete = utilisateurDAO.recupererQuestionSecrete(username);

            if (questionSecrete == null || questionSecrete.isBlank()) {
                view.getMessageLabel().setText("Utilisateur introuvable.");
                return;
            }

            usernameVerifie = username;
            view.afficherEtapeQuestion(questionSecrete);
            view.getMessageLabel().setText("Répondez à la question pour définir un nouveau mot de passe.");
        });

        view.getResetButton().setOnAction(e -> {
            String reponse = view.getSecretAnswerField().getText().trim();
            String nouveauMotDePasse = view.getNewPasswordField().getText();

            view.getMessageLabel().setText("");

            if (usernameVerifie == null) {
                view.getMessageLabel().setText("Veuillez d'abord vérifier votre nom d'utilisateur.");
                return;
            }

            if (reponse.isEmpty() || nouveauMotDePasse.isEmpty()) {
                view.getMessageLabel().setText("Veuillez remplir tous les champs.");
                return;
            }

            if (nouveauMotDePasse.length() < 6) {
                view.getMessageLabel().setText("Le nouveau mot de passe doit contenir au moins 6 caractères.");
                return;
            }

            String reponseHash = hashTexte(reponse.toLowerCase().trim());
            String reponseAttendueHash = utilisateurDAO.recupererReponseSecreteHash(usernameVerifie);

            if (reponseAttendueHash == null || !reponseAttendueHash.equals(reponseHash)) {
                view.getMessageLabel().setText("Réponse secrète incorrecte.");
                return;
            }

            String nouveauMotDePasseHash = hashTexte(nouveauMotDePasse);
            boolean succes = utilisateurDAO.mettreAJourMotDePasse(usernameVerifie, nouveauMotDePasseHash);

            if (succes) {
                        afficherSucces(view, "Mot de passe mis à jour avec succès.");
                        view.getSecretAnswerField().clear();
                        view.getNewPasswordField().clear();
                        } else {
                               afficherErreur(view, "Erreur lors de la mise à jour du mot de passe.");
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

    private void afficherErreur(ForgotPasswordView view, String message) {
    view.getMessageLabel().setText(message);
    view.getMessageLabel().getStyleClass().removeAll("message-success");
    view.getMessageLabel().getStyleClass().add("message-error");
}

private void afficherSucces(ForgotPasswordView view, String message) {
    view.getMessageLabel().setText(message);
    view.getMessageLabel().getStyleClass().removeAll("message-error");
    view.getMessageLabel().getStyleClass().add("message-success");
}
}