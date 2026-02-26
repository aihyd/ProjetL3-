package com.project.jdr.views;

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
    private Label  backToLogin;

    public ForgotPasswordView() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        title = new Label("Forgot Password");
        title.getStyleClass().add("title-label");

        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("text-field");

        verifyButton = new Button("Verify");
        verifyButton.getStyleClass().add("btn-primary");

        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

        secretQuestionLabel = new Label("Your secret question will appear here");
        secretQuestionLabel.getStyleClass().add("secret-question-label");
        secretQuestionLabel.setVisible(false);

        secretAnswerField = new TextField();
        secretAnswerField.setPromptText("Answer to secret question");
        secretAnswerField.getStyleClass().add("text-field");
        secretAnswerField.setVisible(false);

        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New password");
        newPasswordField.getStyleClass().add("text-field");
        newPasswordField.setVisible(false);

        resetButton = new Button("Reset Password");
        resetButton.getStyleClass().add("btn-primary");
        resetButton.setVisible(false);

         backToLogin = new Label("Back to Login");
        backToLogin.getStyleClass().add("action-link");
        backToLogin.setVisible(false);

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


    public Label getTitle() { return title; }
    public TextField getUsernameField() { return usernameField; }
    public Button getVerifyButton() { return verifyButton; }
    public Label getMessageLabel() { return messageLabel; }
    public Label getSecretQuestionLabel() { return secretQuestionLabel; }
    public TextField getSecretAnswerField() { return secretAnswerField; }
    public PasswordField getNewPasswordField() { return newPasswordField; }
    public Button getResetButton() { return resetButton; }
    public Label getBackToLogin() { return backToLogin; }
}