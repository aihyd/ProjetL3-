package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
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

        // ── Card ──────────────────────────────────────────────────────
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));
        card.setMinWidth(280);
        card.setMaxWidth(500);
        card.setPrefWidth(Double.MAX_VALUE);

        // ── Titre ──────────────────────────────────────────────────────
        Label title = new Label("JDP Registration");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Cree ton compte aventurier");
        subtitle.getStyleClass().add("auth-subtitle");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        // ── Champs ─────────────────────────────────────────────────────
        VBox userBox = buildFieldGroup("NOM D'UTILISATEUR",
                usernameField = new TextField());
        usernameField.setPromptText("Username");

        VBox passBox = buildFieldGroup("MOT DE PASSE",
                passwordField = new PasswordField());
        passwordField.setPromptText("Password");

        VBox confirmBox = buildFieldGroup("CONFIRMER LE MOT DE PASSE",
                confirmPasswordField = new PasswordField());
        confirmPasswordField.setPromptText("Confirm Password");

        // ── Question secrete ───────────────────────────────────────────
        secretLabel = new Label("QUESTION SECRETE");
        secretLabel.getStyleClass().add("auth-field-label");

        secretQuestionCombo = new ComboBox<>();
        secretQuestionCombo.getItems().addAll(
                "What is your pet's name?",
                "What is your mother's maiden name?",
                "What city were you born in?"
        );
        secretQuestionCombo.setPromptText("Choisir une question...");
        secretQuestionCombo.getStyleClass().add("auth-field");
        secretQuestionCombo.setMaxWidth(Double.MAX_VALUE);

        VBox secretAnswerBox = buildFieldGroup("REPONSE",
                secretAnswerField = new TextField());
        secretAnswerField.setPromptText("Answer to your secret question");

        VBox secretSection = new VBox(8,
                secretLabel, secretQuestionCombo, secretAnswerBox);

        // ── Message + boutons ──────────────────────────────────────────
        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        registerButton = new Button("S'inscrire");
        registerButton.getStyleClass().add("register-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);

        backToLogin = new Label("Retour au login");
        backToLogin.getStyleClass().add("action-link");
        backToLogin.setMaxWidth(Double.MAX_VALUE);
        backToLogin.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                title,
                subtitle,
                userBox,
                passBox,
                confirmBox,
                secretSection,
                messageLabel,
                registerButton,
                backToLogin
        );

        // ── Wrapper centré responsive ──────────────────────────────────
        VBox wrapper = new VBox(card);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setPadding(new Insets(40, 20, 40, 20));
        wrapper.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(card, Priority.NEVER);

        // ── ScrollPane pour petites fenêtres ───────────────────────────
        ScrollPane scrollPane = new ScrollPane(wrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );

        root = new StackPane(scrollPane);
        root.getStyleClass().add("auth-root");
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #12121f, #1e1e2f, #2b2b3c);"
        );
    }

    private VBox buildFieldGroup(String labelText, javafx.scene.control.Control field) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        field.getStyleClass().add("auth-field");
        field.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(6, lbl, field);
        box.setMaxWidth(Double.MAX_VALUE);
        return box;
    }

    public Parent          getRoot()                  { return root; }
    public TextField       getUsernameField()          { return usernameField; }
    public PasswordField   getPasswordField()          { return passwordField; }
    public PasswordField   getConfirmPasswordField()   { return confirmPasswordField; }
    public ComboBox<String> getSecretQuestionCombo()  { return secretQuestionCombo; }
    public TextField       getSecretAnswerField()      { return secretAnswerField; }
    public Label           getMessageLabel()           { return messageLabel; }
    public Button          getRegisterButton()         { return registerButton; }
    public Label           getBackToLogin()            { return backToLogin; }
}