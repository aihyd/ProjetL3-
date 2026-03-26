package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class LoginView {

    private final StackPane root;

    private TextField     Username;
    private PasswordField passwordField;
    private Hyperlink     forgotPasswordLink;
    private Button        btnConnexion;
    private Button        btnInscription;
    private Label         message;

    public LoginView() {

        // ── Card ──────────────────────────────────────────────────────────
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));

        // La card fait entre 280px et 480px, et s'adapte entre les deux
        card.setMinWidth(280);
        card.setMaxWidth(480);
        // La largeur préférée suit 40% de la fenêtre, bornée entre min et max
        card.setPrefWidth(Double.MAX_VALUE);

        // ── Titre ─────────────────────────────────────────────────────────
        Label titre = new Label("JDP Login");
        titre.getStyleClass().add("title-label");
        titre.setMaxWidth(Double.MAX_VALUE);
        titre.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Connecte-toi à ton compte");
        subtitle.getStyleClass().add("auth-subtitle");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        // ── Champs ────────────────────────────────────────────────────────
        VBox userBox = buildFieldGroup("NOM D'UTILISATEUR",
                Username = new TextField());
        Username.setPromptText("User Name");

        VBox passBox = buildFieldGroup("MOT DE PASSE",
                passwordField = new PasswordField());
        passwordField.setPromptText("Password");

        // Lien mot de passe oublié (aligné à droite)
        HBox forgotRow = new HBox();
        forgotRow.setAlignment(Pos.CENTER_RIGHT);
        forgotPasswordLink = new Hyperlink("Mot de passe oublié ?");
        forgotPasswordLink.getStyleClass().add("forgot-password-link");
        forgotPasswordLink.setOnAction(e ->
                System.out.println("Rediriger vers la récupération de mot de passe..."));
        forgotRow.getChildren().add(forgotPasswordLink);

        // ── Boutons ───────────────────────────────────────────────────────
        btnConnexion = new Button("Se connecter");
        btnConnexion.getStyleClass().add("btn-primary");
        btnConnexion.setMaxWidth(Double.MAX_VALUE);

        btnInscription = new Button("Créer un compte");
        btnInscription.getStyleClass().add("btn-secondary");
        btnInscription.setMaxWidth(Double.MAX_VALUE);

        // ── Message ───────────────────────────────────────────────────────
        message = new Label();
        message.getStyleClass().add("message-label");
        message.setMaxWidth(Double.MAX_VALUE);
        message.setAlignment(Pos.CENTER);
        message.setWrapText(true);

        card.getChildren().addAll(
                titre, subtitle,
                buildDivider(),
                userBox, passBox, forgotRow,
                buildDivider(),
                btnConnexion, btnInscription,
                message
        );

        // ── StackPane racine : centre la card et lui laisse de la marge ───
        root = new StackPane(card);
        root.getStyleClass().add("auth-root");
        // Marge horizontale dynamique via padding — la card grossit jusqu'à maxWidth
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
    public Parent        getRoot()               { return root; }
    public TextField     getUsername()           { return Username; }
    public PasswordField getPasswordField()      { return passwordField; }
    public Hyperlink     getForgotPasswordLink() { return forgotPasswordLink; }
    public Button        getBtnConnexion()       { return btnConnexion; }
    public Button        getBtnInscription()     { return btnInscription; }
    public Label         getMessage()            { return message; }
}