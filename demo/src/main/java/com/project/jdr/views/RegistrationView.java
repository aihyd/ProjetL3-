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
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label secretLabel;
    private ComboBox<String> secretQuestionCombo;
    private TextField secretAnswerField;
    private Label messageLabel;
    private Button registerButton;
    private Label backToLogin;

    public RegistrationView() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label title = new Label("JDP Registration");
        title.getStyleClass().add("title-label");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("register-field");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("register-field");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.getStyleClass().add("register-field");

        secretLabel = new Label("Secret Question (for password recovery)");
        secretLabel.getStyleClass().add("secret-question-label");

        secretQuestionCombo = new ComboBox<>();
        secretQuestionCombo.getItems().addAll(
            "What is your pet's name?",
            "What is your mother's maiden name?",
            "What city were you born in?"
        );
        secretQuestionCombo.setPromptText("Choose a question");
        secretQuestionCombo.getStyleClass().add("secret-question-field");

        secretAnswerField = new TextField();
        secretAnswerField.setPromptText("Answer to your secret question");
        secretAnswerField.getStyleClass().add("secret-question-field");

        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");

        registerButton = new Button("Register");
        registerButton.getStyleClass().add("register-button");

        backToLogin = new Label("Back to Login");
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

    // Getters
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public PasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public ComboBox<String> getSecretQuestionCombo() { return secretQuestionCombo; }
    public TextField getSecretAnswerField() { return secretAnswerField; }
    public Label getMessageLabel() { return messageLabel; }
    public Button getRegisterButton() { return registerButton; }
    public Label getBackToLogin() { return backToLogin; }
}