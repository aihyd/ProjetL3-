package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ProfileView {

    private final VBox root;

    private Label            userIdValueLabel;
    private Label            usernameValueLabel;
    private Button           settingsButton;
    private Button           createCharacterButton;
    private ListView<String> personnagesListView;

    // Items du menu contextuel — accessibles depuis le contrôleur
    private final MenuItem changePasswordItem  = new MenuItem("Modifier le mot de passe");
    private final MenuItem deleteAccountItem   = new MenuItem("Supprimer le compte");
    private final MenuItem logoutItem          = new MenuItem("Se déconnecter");

    public ProfileView() {

        root = new VBox(16);
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("auth-root");
        root.setPadding(new Insets(30, 40, 30, 40));

        Label pageTitle = new Label("Mon Profil");
        pageTitle.getStyleClass().add("title-label");
        pageTitle.setMaxWidth(Double.MAX_VALUE);
        pageTitle.setAlignment(Pos.CENTER_LEFT);

        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setMinWidth(320);
        contentWrapper.setMaxWidth(860);
        contentWrapper.setPrefWidth(Double.MAX_VALUE);
        VBox.setVgrow(contentWrapper, Priority.ALWAYS);

        contentWrapper.getChildren().addAll(
                pageTitle,
                buildAccountCard(),
                buildCharactersCard()
        );

        root.getChildren().add(contentWrapper);
    }

    // ── Carte informations compte ─────────────────────────────────────────
    private VBox buildAccountCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label sectionTitle = new Label("Informations du compte");
        sectionTitle.getStyleClass().add("profile-card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        settingsButton = new Button("Paramètres ▾");
        settingsButton.getStyleClass().add("btn-secondary");

        // ── ContextMenu ───────────────────────────────────────────────────
        changePasswordItem.getStyleClass().add("settings-menu-item");
        deleteAccountItem.getStyleClass().add("settings-menu-item-danger");
        logoutItem.getStyleClass().add("settings-menu-item");

        ContextMenu settingsMenu = new ContextMenu(
                changePasswordItem,
                new SeparatorMenuItem(),
                deleteAccountItem,
                new SeparatorMenuItem(),
                logoutItem
        );
        settingsMenu.getStyleClass().add("settings-context-menu");

        // Afficher le menu juste sous le bouton au clic
        settingsButton.setOnAction(e ->
                settingsMenu.show(
                        settingsButton,
                        javafx.geometry.Side.BOTTOM,
                        0, 4
                )
        );

        header.getChildren().addAll(sectionTitle, spacer, settingsButton);

        HBox fieldsRow = new HBox(32);
        fieldsRow.setAlignment(Pos.CENTER_LEFT);

        userIdValueLabel   = new Label("-");
        usernameValueLabel = new Label("-");

        fieldsRow.getChildren().addAll(
                buildField("ID UTILISATEUR",    userIdValueLabel),
                buildField("NOM D'UTILISATEUR", usernameValueLabel)
        );

        card.getChildren().addAll(header, fieldsRow);
        return card;
    }

    // ── Carte personnages ─────────────────────────────────────────────────
    private VBox buildCharactersCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(card, Priority.ALWAYS);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox textBlock = new VBox(3);
        Label cardTitle = new Label("Mes personnages");
        cardTitle.getStyleClass().add("profile-card-title");
        Label subtitle = new Label("Retrouve ici tous tes personnages créés.");
        subtitle.getStyleClass().add("auth-subtitle");
        textBlock.getChildren().addAll(cardTitle, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        createCharacterButton = new Button("+ Créer un personnage");
        createCharacterButton.getStyleClass().add("btn-primary");

        header.getChildren().addAll(textBlock, spacer, createCharacterButton);

        Region sep = new Region();
        sep.getStyleClass().add("auth-divider");
        sep.setPrefHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);

        personnagesListView = new ListView<>();
        personnagesListView.getStyleClass().add("profile-list-view");
        personnagesListView.setPrefHeight(300);
        VBox.setVgrow(personnagesListView, Priority.ALWAYS);

        Label emptyLabel = new Label("Aucun personnage pour l'instant.");
        emptyLabel.getStyleClass().add("profile-empty-label");
        personnagesListView.setPlaceholder(emptyLabel);

        personnagesListView.setCellFactory(lv -> new ListCell<>() {
            private final HBox  row      = new HBox(12);
            private final Label initials = new Label();
            private final Label name     = new Label();
            {
                row.setAlignment(Pos.CENTER_LEFT);
                row.setPadding(new Insets(6, 0, 6, 0));
                initials.getStyleClass().add("profile-avatar");
                name.getStyleClass().add("profile-list-name");
                row.getChildren().addAll(initials, name);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    initials.setText(item.length() >= 2
                            ? item.substring(0, 2).toUpperCase()
                            : item.toUpperCase());
                    name.setText(item);
                    setGraphic(row);
                }
            }
        });

        card.getChildren().addAll(header, sep, personnagesListView);
        return card;
    }

    // ── Helper champ label + valeur ───────────────────────────────────────
    private VBox buildField(String labelText, Label valueLabel) {
        VBox box = new VBox(4);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        valueLabel.getStyleClass().add("profile-field-value");
        box.getChildren().addAll(lbl, valueLabel);
        return box;
    }

    // ── API publique ──────────────────────────────────────────────────────
    public Parent getRoot() { return root; }

    public void setUserId(int id)        { userIdValueLabel.setText(String.valueOf(id)); }
    public void setUsername(String name) { usernameValueLabel.setText(name); }

    public Button            getSettingsButton()        { return settingsButton; }
    public Button            getCreateCharacterButton() { return createCharacterButton; }
    public ListView<String>  getPersonnagesListView()   { return personnagesListView; }

    // Getters pour brancher les actions dans le contrôleur
    public MenuItem getChangePasswordItem() { return changePasswordItem; }
    public MenuItem getDeleteAccountItem()  { return deleteAccountItem; }
    public MenuItem getLogoutItem()         { return logoutItem; }
}