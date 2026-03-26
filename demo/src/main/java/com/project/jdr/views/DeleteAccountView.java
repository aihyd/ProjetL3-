package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class DeleteAccountView {

    private final StackPane root;

    private PasswordField passwordField;
    private Button        confirmButton;
    private Label         messageLabel;
    private Label         backToProfile;

    public DeleteAccountView() {

        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));
        card.setMinWidth(280);
        card.setMaxWidth(480);
        card.setPrefWidth(Double.MAX_VALUE);

        // ── Titre ─────────────────────────────────────────────────────────
        Label title = new Label("Supprimer le compte");
        title.getStyleClass().add("title-label-danger");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Cette action est irréversible.");
        subtitle.getStyleClass().add("auth-subtitle-danger");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        // ── Avertissement ─────────────────────────────────────────────────
        Label warning = new Label(
                "Tous tes personnages et données seront définitivement supprimés. " +
                "Entre ton mot de passe pour confirmer."
        );
        warning.getStyleClass().add("delete-warning-label");
        warning.setMaxWidth(Double.MAX_VALUE);
        warning.setWrapText(true);
        warning.setAlignment(Pos.CENTER);

        // ── Champ mot de passe ────────────────────────────────────────────
        VBox passBox = buildFieldGroup("MOT DE PASSE",
                passwordField = new PasswordField());
        passwordField.setPromptText("Confirme ton mot de passe");

        // ── Message retour ────────────────────────────────────────────────
        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        // ── Boutons ───────────────────────────────────────────────────────
        confirmButton = new Button("Confirmer la suppression");
        confirmButton.getStyleClass().add("btn-danger");
        confirmButton.setMaxWidth(Double.MAX_VALUE);

        backToProfile = new Label("← Retour au profil");
        backToProfile.getStyleClass().add("action-link");
        backToProfile.setMaxWidth(Double.MAX_VALUE);
        backToProfile.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                title,
                subtitle,
                buildDivider(),
                warning,
                buildDivider(),
                passBox,
                messageLabel,
                buildDivider(),
                confirmButton,
                backToProfile
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

    public Parent        getRoot()          { return root; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button        getConfirmButton() { return confirmButton; }
    public Label         getMessageLabel()  { return messageLabel; }
    public Label         getBackToProfile() { return backToProfile; }
}