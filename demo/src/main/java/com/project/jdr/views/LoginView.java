package com.project.jdr.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {

    private VBox root;

    public LoginView() {

        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label titre = new Label("JDP Login");
        titre.getStyleClass().add("title-label");

        TextField Username = new TextField();
        Username.setPromptText("User Name");
        Username.getStyleClass().add("User Name");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("Password");

        
        Hyperlink forgotPasswordLink = new Hyperlink("Forgot your password?");
        forgotPasswordLink.getStyleClass().add("forgot-password-link");
        forgotPasswordLink.setOnAction(e -> {
            System.out.println("Rediriger vers la récupération de mot de passe...");
         
        });

        Button btnConnexion = new Button("Login");
        btnConnexion.getStyleClass().add("btn-primary");

        Button btnInscription = new Button("Register");
        btnInscription.getStyleClass().add("btn-secondary");

        Label message = new Label();
        message.getStyleClass().add("message-label");

        root.getChildren().addAll(
            titre,
            Username,
            passwordField,
            forgotPasswordLink, 
            btnConnexion,
            btnInscription,
            message
        );
    }

    public Parent getRoot() {
        return root;
    }
}