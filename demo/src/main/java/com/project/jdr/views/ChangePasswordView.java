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

public class ChangePasswordView {

    private final StackPane root;

    private PasswordField currentPasswordField;
    private PasswordField newPasswordField;
    private PasswordField confirmNewPasswordField;
    private Button        confirmButton;
    private Label         messageLabel;
    private Label         backToProfile;

    public ChangePasswordView() {

        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));
        card.setMinWidth(280);
        card.setMaxWidth(480);
        card.setPrefWidth(Double.MAX_VALUE);

        // ── Titre ─────────────────────────────────────────────────────────
        Label title = new Label("Modifier le mot de passe");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Choisis un nouveau mot de passe sécurisé");
        subtitle.getStyleClass().add("auth-subtitle");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);

        // ── Champs ────────────────────────────────────────────────────────
        VBox currentBox = buildFieldGroup("MOT DE PASSE ACTUEL",
                currentPasswordField = new PasswordField());
        currentPasswordField.setPromptText("Mot de passe actuel");

        VBox newBox = buildFieldGroup("NOUVEAU MOT DE PASSE",
                newPasswordField = new PasswordField());
        newPasswordField.setPromptText("Nouveau mot de passe");

        VBox confirmBox = buildFieldGroup("CONFIRMER LE NOUVEAU MOT DE PASSE",
                confirmNewPasswordField = new PasswordField());
        confirmNewPasswordField.setPromptText("Confirmer le nouveau mot de passe");

        // ── Message ───────────────────────────────────────────────────────
        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        // ── Boutons ───────────────────────────────────────────────────────
        confirmButton = new Button("Modifier le mot de passe");
        confirmButton.getStyleClass().add("btn-primary");
        confirmButton.setMaxWidth(Double.MAX_VALUE);

        backToProfile = new Label("← Retour au profil");
        backToProfile.getStyleClass().add("action-link");
        backToProfile.setMaxWidth(Double.MAX_VALUE);
        backToProfile.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                title,
                subtitle,
                buildDivider(),
                currentBox,
                buildDivider(),
                newBox,
                confirmBox,
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

    public Parent        getRoot()                   { return root; }
    public PasswordField getCurrentPasswordField()   { return currentPasswordField; }
    public PasswordField getNewPasswordField()       { return newPasswordField; }
    public PasswordField getConfirmNewPasswordField(){ return confirmNewPasswordField; }
    public Button        getConfirmButton()          { return confirmButton; }
    public Label         getMessageLabel()           { return messageLabel; }
    public Label         getBackToProfile()          { return backToProfile; }
}