package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.database.UserDAO;
import com.project.jdr.views.LoginView;
import javafx.scene.input.MouseEvent;

public class LoginController {

    public LoginController(LoginView view, AppTest app) {

        view.getBtnInscription().setOnAction(e -> app.showRegistration());

        view.getBtnConnexion().setOnAction(e -> {
            String username = view.getUsernameField().getText();
            String password = view.getPasswordField().getText();

            if (UserDAO.login(username, password)) {
                view.getMessageLabel().setStyle("-fx-text-fill: green;");
                view.getMessageLabel().setText("Connexion réussie !");
                // app.showGame(); // plus tard
            } else {
                view.getMessageLabel().setStyle("-fx-text-fill: red;");
                view.getMessageLabel().setText("Identifiants incorrects.");
            }
        });

        view.getForgotPasswordLink().setOnMouseClicked(e -> app.showForgotPassword());

        view.getBackToLogin().setOnMouseClicked((MouseEvent e) -> app.showLogin());
    }
}