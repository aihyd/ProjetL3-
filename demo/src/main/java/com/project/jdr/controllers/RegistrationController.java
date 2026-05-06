package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.UtilisateurDAO;
import com.project.jdr.model.Utilisateur;
import com.project.jdr.views.RegistrationView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegistrationController {

    private final RegistrationView view;
    private final AppTest app;
    private final UtilisateurDAO utilisateurDAO;

    public RegistrationController(RegistrationView view, AppTest app) {
        this.view = view;
        this.app = app;
        this.utilisateurDAO = new UtilisateurDAO();

        view.getBackToLogin().setOnMouseClicked(e -> app.showLogin());
        view.getRegisterButton().setOnAction(e -> inscrireUtilisateur());
    }

    private void inscrireUtilisateur() {
        String username = view.getUsernameField().getText().trim();
        String password = view.getPasswordField().getText();
        String confirmPassword = view.getConfirmPasswordField().getText();
        String secretQuestion = view.getSecretQuestionCombo().getValue();
        String secretAnswer = view.getSecretAnswerField().getText().trim();

        view.getMessageLabel().setText("");

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || secretQuestion == null || secretQuestion.isBlank() || secretAnswer.isEmpty()) {
            view.getMessageLabel().setText("Veuillez remplir tous les champs.");
            return;
        }

        if (username.length() < 3) {
            view.getMessageLabel().setText("Le nom d'utilisateur doit contenir au moins 3 caractères.");
            return;
        }

        if (password.length() < 6) {
            view.getMessageLabel().setText("Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            view.getMessageLabel().setText("Les mots de passe ne correspondent pas.");
            return;
        }

        if (utilisateurDAO.utilisateurExiste(username)) {
            view.getMessageLabel().setText("Ce nom d'utilisateur existe déjà.");
            return;
        }

        String motDePasseHash = hashTexte(password);
        String reponseSecreteHash = hashTexte(secretAnswer.toLowerCase().trim());

        Utilisateur utilisateur = new Utilisateur(
                username,
                motDePasseHash,
                secretQuestion,
                reponseSecreteHash
        );

        boolean succes = utilisateurDAO.ajouterUtilisateur(utilisateur);

        if (succes) {
            view.getMessageLabel().setText("Compte créé avec succès.");
            viderChamps();
            app.showLogin();
        } else {
            view.getMessageLabel().setText("Erreur lors de la création du compte.");
        }
    }

    private void viderChamps() {
        view.getUsernameField().clear();
        view.getPasswordField().clear();
        view.getConfirmPasswordField().clear();
        view.getSecretQuestionCombo().setValue(null);
        view.getSecretAnswerField().clear();
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
}