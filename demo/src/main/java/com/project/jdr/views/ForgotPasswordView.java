package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ForgotPasswordView {

    private final StackPane root;

    private Label title;
    private TextField usernameField;
    private Button verifyButton;
    private Label messageLabel;
    private Label secretQuestionLabel;
    private TextField secretAnswerField;
    private PasswordField newPasswordField;
    private Button resetButton;
    private Label backToLogin;

    private Region divider2;
    private VBox answerBox;
    private VBox newPassBox;
    private Region divider3;

    public ForgotPasswordView() {

        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));
        card.setMinWidth(280);
        card.setMaxWidth(480);
        card.setPrefWidth(Double.MAX_VALUE);

        title = new Label("Forgot Password");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Retrouve l'accès à ton compte");
        subtitle.getStyleClass().add("auth-subtitle");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        VBox userBox = buildFieldGroup("NOM D'UTILISATEUR",
                usernameField = new TextField());
        usernameField.setPromptText("Enter your username");

        verifyButton = new Button("Vérifier");
        verifyButton.getStyleClass().add("btn-primary");
        verifyButton.setMaxWidth(Double.MAX_VALUE);

        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        divider2 = buildDivider();
        divider2.setVisible(false);
        divider2.setManaged(false);

        secretQuestionLabel = new Label("Your secret question will appear here");
        secretQuestionLabel.getStyleClass().add("auth-field-label");
        secretQuestionLabel.setMaxWidth(Double.MAX_VALUE);
        secretQuestionLabel.setWrapText(true);
        secretQuestionLabel.setVisible(false);
        secretQuestionLabel.setManaged(false);

        answerBox = buildFieldGroup("VOTRE RÉPONSE",
                secretAnswerField = new TextField());
        secretAnswerField.setPromptText("Answer to secret question");
        answerBox.setVisible(false);
        answerBox.setManaged(false);

        newPassBox = buildFieldGroup("NOUVEAU MOT DE PASSE",
                newPasswordField = new PasswordField());
        newPasswordField.setPromptText("New password");
        newPassBox.setVisible(false);
        newPassBox.setManaged(false);

        resetButton = new Button("Réinitialiser le mot de passe");
        resetButton.getStyleClass().add("register-button");
        resetButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.setVisible(false);
        resetButton.setManaged(false);

        divider3 = buildDivider();
        divider3.setVisible(false);
        divider3.setManaged(false);

        backToLogin = new Label("← Retour à la connexion");
        backToLogin.getStyleClass().add("action-link");
        backToLogin.setMaxWidth(Double.MAX_VALUE);
        backToLogin.setAlignment(Pos.CENTER);
        backToLogin.setVisible(true);
        backToLogin.setManaged(true);

        card.getChildren().addAll(
                title,
                subtitle,
                buildDivider(),
                userBox,
                verifyButton,
                messageLabel,
                divider2,
                secretQuestionLabel,
                answerBox,
                newPassBox,
                resetButton,
                divider3,
                backToLogin
        );

        root = new StackPane(card);
        root.getStyleClass().add("auth-root");
        StackPane.setMargin(card, new Insets(40, 60, 40, 60));
    }

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

    public void afficherEtapeQuestion(String question) {
        secretQuestionLabel.setText(question);

        divider2.setManaged(true);
        divider2.setVisible(true);

        secretQuestionLabel.setManaged(true);
        secretQuestionLabel.setVisible(true);

        answerBox.setManaged(true);
        answerBox.setVisible(true);

        newPassBox.setManaged(true);
        newPassBox.setVisible(true);

        resetButton.setManaged(true);
        resetButton.setVisible(true);

        divider3.setManaged(true);
        divider3.setVisible(true);
    }

    public void reinitialiserEtapeQuestion() {
        secretQuestionLabel.setText("Your secret question will appear here");
        secretAnswerField.clear();
        newPasswordField.clear();

        divider2.setManaged(false);
        divider2.setVisible(false);

        secretQuestionLabel.setManaged(false);
        secretQuestionLabel.setVisible(false);

        answerBox.setManaged(false);
        answerBox.setVisible(false);

        newPassBox.setManaged(false);
        newPassBox.setVisible(false);

        resetButton.setManaged(false);
        resetButton.setVisible(false);

        divider3.setManaged(false);
        divider3.setVisible(false);
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