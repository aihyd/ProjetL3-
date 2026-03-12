package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.database.UserDAO;
import com.project.jdr.views.ForgotPasswordView;
import javafx.scene.input.MouseEvent;

public class ForgotPasswordController {

    public ForgotPasswordController(ForgotPasswordView view, AppTest app) {

        view.getBackToLogin().setOnMouseClicked((MouseEvent e) -> app.showLogin());

        view.getVerifyButton().setOnAction(e -> {
            String username = view.getUsernameField().getText();
            String question = UserDAO.getSecretQuestion(username);

            if (question != null) {
                view.getSecretQuestionLabel().setText(question);
                view.getSecretQuestionLabel().setVisible(true);
                view.getSecretAnswerField().setVisible(true);
                view.getNewPasswordField().setVisible(true);
                view.getResetButton().setVisible(true);
                view.getMessageLabel().setText("");
            } else {
                view.getMessageLabel().setText("Utilisateur introuvable !");
            }
        });

        view.getResetButton().setOnAction(e -> {
            String username = view.getUsernameField().getText();
            String answer   = view.getSecretAnswerField().getText();
            String newPass  = view.getNewPasswordField().getText();

            if (UserDAO.resetPassword(username, answer, newPass)) {
                view.getMessageLabel().setStyle("-fx-text-fill: green;");
                view.getMessageLabel().setText("Mot de passe réinitialisé !");
            } else {
                view.getMessageLabel().setStyle("-fx-text-fill: red;");
                view.getMessageLabel().setText("Réponse incorrecte !");
            }
        });
    }
}