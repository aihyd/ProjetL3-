package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ForgotPasswordView {

    private VBox root;
    private Label title;
    private TextField usernameField;
    private Button verifyButton;
    private Label messageLabel;
    private Label secretQuestionLabel;
    private TextField secretAnswerField;
    private PasswordField newPasswordField;
    private Button resetButton;
    private Label backToLogin;

    public ForgotPasswordView() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        // Titre identique à Registration
        title = new Label("Forgot Password");
        title.getStyleClass().add("title-label");

        // Champ username
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("register-field");

        // Bouton Verify (orange)
        verifyButton = new Button("Verify");
        verifyButton.getStyleClass().add("button");

        // Message label
        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");

        // Question secrète label
        secretQuestionLabel = new Label("Your secret question will appear here");
        secretQuestionLabel.getStyleClass().add("secret-question-label");
        secretQuestionLabel.setVisible(false);

        // Réponse à la question secrète
        secretAnswerField = new TextField();
        secretAnswerField.setPromptText("Answer to secret question");
        secretAnswerField.getStyleClass().add("register-field");
        secretAnswerField.setVisible(false);

        // Nouveau mot de passe
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New password");
        newPasswordField.getStyleClass().add("register-field");
        newPasswordField.setVisible(false);

        // Bouton Reset (vert comme Login/Register)
        resetButton = new Button("Reset Password");
        resetButton.getStyleClass().add("register-button");
        resetButton.setVisible(false);

        // Lien retour
        backToLogin = new Label("Back to Login");
        backToLogin.getStyleClass().add("action-link");

        root.getChildren().addAll(
            title,
            usernameField,
            verifyButton,
            messageLabel,
            secretQuestionLabel,
            secretAnswerField,
            newPasswordField,
            resetButton,
            backToLogin
        );
    }

    public Parent getRoot() { return root; }
    public TextField getUsernameField() { return usernameField; }
    public Button getVerifyButton() { return verifyButton; }
    public Label getMessageLabel() { return messageLabel; }
    public Label getSecretQuestionLabel() { return secretQuestionLabel; }
    public TextField getSecretAnswerField() { return secretAnswerField; }
    public PasswordField getNewPasswordField() { return newPasswordField; }
    public Button getResetButton() { return resetButton; }
    public Label getBackToLogin() { return backToLogin; }
}