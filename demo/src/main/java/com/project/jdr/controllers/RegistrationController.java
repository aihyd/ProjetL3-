package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.database.UserDAO;
import com.project.jdr.views.RegistrationView;

public class RegistrationController {

    public RegistrationController(RegistrationView view, AppTest app) {

        view.getBackToLogin().setOnMouseClicked(e -> app.showLogin());

        view.getRegisterButton().setOnAction(e -> {
            String username = view.getUsernameField().getText();
            String password = view.getPasswordField().getText();
            String confirm  = view.getConfirmPasswordField().getText();
            String question = view.getSecretQuestionCombo().getValue();
            String answer   = view.getSecretAnswerField().getText();

            // Vérifications
            if (username.isEmpty() || password.isEmpty() || answer.isEmpty() || question == null) {
                view.getMessageLabel().setText("Remplis tous les champs !");
                return;
            }
            if (!password.equals(confirm)) {
                view.getMessageLabel().setText("Les mots de passe ne correspondent pas !");
                return;
            }

            if (UserDAO.register(username, password, question, answer)) {
                view.getMessageLabel().setStyle("-fx-text-fill: green;");
                view.getMessageLabel().setText("Compte créé ! Retourne au login.");
            } else {
                view.getMessageLabel().setStyle("-fx-text-fill: red;");
                view.getMessageLabel().setText("Nom d'utilisateur déjà pris !");
            }
        });
    }
}