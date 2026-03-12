package com.project.jdr.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegistrationView {

    private VBox root;

    public RegistrationView() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("JDP Registration");
        title.getStyleClass().add("title-label");

       
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("register-field");

     
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("register-field");

       
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("register-field");

  
        Label secretLabel = new Label("Secret Question (for password recovery)");
        secretLabel.getStyleClass().add("secret-question-label");

       
        ComboBox<String> secretQuestionCombo = new ComboBox<>();
        secretQuestionCombo.getItems().addAll(
                "What is your pet's name?",
                "What is your mother's maiden name?",
                "What city were you born in?"
        );
        secretQuestionCombo.setPromptText("Choose a question");
        secretQuestionCombo.getStyleClass().add("secret-question-field");

      
        TextField secretAnswerField = new TextField();
        secretAnswerField.setPromptText("Answer to your secret question");
        secretAnswerField.getStyleClass().add("secret-question-field");

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");

      
        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");

     
        Label backToLogin = new Label("Back to Login");
        backToLogin.getStyleClass().add("action-link");

       
        root.getChildren().addAll(
                title,
                usernameField,
                passwordField,
                confirmPasswordField,
                secretLabel,
                secretQuestionCombo,
                secretAnswerField,
                registerButton,
                messageLabel,
                backToLogin
        );
    }

    public Parent getRoot() {
        return root;
    }
}