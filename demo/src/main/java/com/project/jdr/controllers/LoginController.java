package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.UtilisateurDAO;
import com.project.jdr.views.LoginView;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    private final UtilisateurDAO utilisateurDAO;

    public LoginController(LoginView view, AppTest app) {
        this.utilisateurDAO = new UtilisateurDAO();

        view.getBtnInscription().setOnAction(e -> app.showRegistration());

        view.getBtnConnexion().setOnAction(e -> {
            String username = view.getUsername().getText().trim();
            String password = view.getPasswordField().getText();

            view.getMessage().setText("");

            if (username.isEmpty() || password.isEmpty()) {
                view.getMessage().setText("Veuillez remplir tous les champs.");
                return;
            }

            String motDePasseHash = hashTexte(password);

            boolean connexionValide = utilisateurDAO.verifierConnexion(username, motDePasseHash);

            if (!connexionValide) {
                view.getMessage().setText("Nom d'utilisateur ou mot de passe incorrect.");
                return;
            }

            Integer idUtilisateur = utilisateurDAO.recupererIdUtilisateur(username);

            if (idUtilisateur == null) {
                view.getMessage().setText("Impossible de récupérer les informations du profil.");
                return;
            }

            app.showProfile(idUtilisateur, username);
        });

        view.getForgotPasswordLink().setOnAction(e -> app.showForgotPassword());
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