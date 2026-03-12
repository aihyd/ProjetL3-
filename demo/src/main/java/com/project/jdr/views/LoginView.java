package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {

    private VBox root;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button btnConnexion;
    private Button btnInscription;
    private Label forgotPasswordLink;
    private Label backToLogin;
    private Label messageLabel;

    public LoginView() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        Label title = new Label("JDP Login");
        title.getStyleClass().add("title-label");

        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("register-field");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("register-field");

        btnConnexion = new Button("Login");
        btnConnexion.getStyleClass().add("register-button");

        btnInscription = new Button("Register");
        btnInscription.getStyleClass().add("button");

        forgotPasswordLink = new Label("Forgot Password?");
        forgotPasswordLink.getStyleClass().add("action-link");

        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");

        backToLogin = new Label("Back to Login");
        backToLogin.getStyleClass().add("action-link");
        backToLogin.setVisible(false);

        root.getChildren().addAll(
            title,
            usernameField,
            passwordField,
            btnConnexion,
            btnInscription,
            forgotPasswordLink,
            messageLabel,
            backToLogin
        );
    }

    public Parent getRoot() { return root; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getBtnConnexion() { return btnConnexion; }
    public Button getBtnInscription() { return btnInscription; }
    public Label getForgotPasswordLink() { return forgotPasswordLink; }
    public Label getMessageLabel() { return messageLabel; }
    public Label getBackToLogin() { return backToLogin; }
}