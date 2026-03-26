package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class RegistrationView {

    private final StackPane root;

    private TextField        usernameField;
    private PasswordField    passwordField;
    private PasswordField    confirmPasswordField;
    private Label            secretLabel;
    private ComboBox<String> secretQuestionCombo;
    private TextField        secretAnswerField;
    private Label            messageLabel;
    private Button           registerButton;
    private Label            backToLogin;

    public RegistrationView() {

        // ── Card ──────────────────────────────────────────────────────────
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));
        card.setMinWidth(280);
        card.setMaxWidth(480);
        card.setPrefWidth(Double.MAX_VALUE);

        // ── Titre ─────────────────────────────────────────────────────────
        Label title = new Label("JDP Registration");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Crée ton compte aventurier");
        subtitle.getStyleClass().add("auth-subtitle");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        // ── Champs identité ───────────────────────────────────────────────
        VBox userBox = buildFieldGroup("NOM D'UTILISATEUR",
                usernameField = new TextField());
        usernameField.setPromptText("Username");

        VBox passBox = buildFieldGroup("MOT DE PASSE",
                passwordField = new PasswordField());
        passwordField.setPromptText("Password");

        VBox confirmBox = buildFieldGroup("CONFIRMER LE MOT DE PASSE",
                confirmPasswordField = new PasswordField());
        confirmPasswordField.setPromptText("Confirm Password");

        // ── Section question secrète ──────────────────────────────────────
        secretLabel = new Label("QUESTION SECRÈTE");
        secretLabel.getStyleClass().add("auth-field-label");

        secretQuestionCombo = new ComboBox<>();
        secretQuestionCombo.getItems().addAll(
                "What is your pet's name?",
                "What is your mother's maiden name?",
                "What city were you born in?"
        );
        secretQuestionCombo.setPromptText("Choisir une question…");
        secretQuestionCombo.getStyleClass().add("auth-field");
        secretQuestionCombo.setMaxWidth(Double.MAX_VALUE);

        VBox secretAnswerBox = buildFieldGroup("RÉPONSE",
                secretAnswerField = new TextField());
        secretAnswerField.setPromptText("Answer to your secret question");

        VBox secretSection = new VBox(8,
                secretLabel, secretQuestionCombo, secretAnswerBox);

        // ── Message + boutons ─────────────────────────────────────────────
        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        registerButton = new Button("S'inscrire");
        registerButton.getStyleClass().add("register-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        backToLogin = new Label("← Retour à la connexion");
        backToLogin.getStyleClass().add("action-link");
        backToLogin.setMaxWidth(Double.MAX_VALUE);
        backToLogin.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                title, subtitle,
                buildDivider(),
                userBox, passBox, confirmBox,
                buildDivider(),
                secretSection,
                buildDivider(),
                messageLabel,
                registerButton,
                backToLogin
        );

        // ── StackPane racine ──────────────────────────────────────────────
        root = new StackPane(card);
        root.getStyleClass().add("auth-root");
        StackPane.setMargin(card, new Insets(40, 60, 40, 60));
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private VBox buildFieldGroup(String labelText, javafx.scene.control.Control field) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        field.getStyleClass().add("auth-field");
        field.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(field, Priority.ALWAYS);
        return new VBox(5, lbl, field);
    }

    private Region buildDivider() {
        Region d = new Region();
        d.getStyleClass().add("auth-divider");
        d.setPrefHeight(1);
        d.setMaxWidth(Double.MAX_VALUE);
        return d;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public Parent            getRoot()                 { return root; }
    public TextField         getUsernameField()        { return usernameField; }
    public PasswordField     getPasswordField()        { return passwordField; }
    public PasswordField     getConfirmPasswordField() { return confirmPasswordField; }
    public ComboBox<String>  getSecretQuestionCombo()  { return secretQuestionCombo; }
    public TextField         getSecretAnswerField()    { return secretAnswerField; }
    public Label             getMessageLabel()         { return messageLabel; }
    public Button            getRegisterButton()       { return registerButton; }
    public Label             getBackToLogin()          { return backToLogin; }
}