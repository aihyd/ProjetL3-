package com.project.jdr.views;

import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Portrait;
import com.project.jdr.model.Stats;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;

public class ProfileView {

    private final VBox root;

    private Label                userIdValueLabel;
    private Label                usernameValueLabel;
    private Button               settingsButton;
    private Button               createCharacterButton;
    private ListView<Personnage> personnagesListView;

    private VBox   resumePane;
    private Button ajouterEquipementButton;
    private Button afficherFicheButton;
private Button chatbotButton;
    private final MenuItem changePasswordItem = new MenuItem("Modifier le mot de passe");
    private final MenuItem deleteAccountItem  = new MenuItem("Supprimer le compte");
    private final MenuItem logoutItem         = new MenuItem("Se déconnecter");

    private static final double CELL_HEIGHT  = 78.0;
    private static final double LIST_MAX_H   = CELL_HEIGHT * 5 + 2;

    public ProfileView() {

        VBox pageContent = new VBox(16);
        pageContent.setAlignment(Pos.TOP_CENTER);
        pageContent.setPadding(new Insets(30, 40, 30, 40));
        pageContent.setOnMousePressed(e -> fermerResume());

        Label pageTitle = new Label("Mon Profil");
        pageTitle.getStyleClass().add("title-label");
        pageTitle.setMaxWidth(Double.MAX_VALUE);
        pageTitle.setAlignment(Pos.CENTER_LEFT);

        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setMinWidth(320);
        contentWrapper.setMaxWidth(860);
        contentWrapper.setPrefWidth(Double.MAX_VALUE);

        contentWrapper.getChildren().addAll(
                pageTitle,
                buildAccountCard(),
                buildCharactersCard()
        );

        pageContent.getChildren().add(contentWrapper);

        ScrollPane scrollPane = new ScrollPane(pageContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
        scrollPane.getStyleClass().clear();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root = new VBox();
        root.getStyleClass().add("auth-root");
        root.getChildren().add(scrollPane);
    }

    private VBox buildAccountCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
        card.setOnMousePressed(e -> fermerResume());

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        Label sectionTitle = new Label("Informations du compte");
        sectionTitle.getStyleClass().add("profile-card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        settingsButton = new Button("Paramètres ▾");
        settingsButton.getStyleClass().add("btn-secondary");

        changePasswordItem.getStyleClass().add("settings-menu-item");
        deleteAccountItem.getStyleClass().add("settings-menu-item-danger");
        logoutItem.getStyleClass().add("settings-menu-item");

        ContextMenu settingsMenu = new ContextMenu(
                changePasswordItem, new SeparatorMenuItem(),
                deleteAccountItem,  new SeparatorMenuItem(),
                logoutItem
        );
        settingsMenu.getStyleClass().add("settings-context-menu");
        settingsButton.setOnAction(e ->
                settingsMenu.show(settingsButton, javafx.geometry.Side.BOTTOM, 0, 4));

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

    private VBox buildCharactersCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setOnMousePressed(e -> { fermerResume(); e.consume(); });

        VBox textBlock = new VBox(3);
        Label cardTitle = new Label("Mes personnages");
        cardTitle.getStyleClass().add("profile-card-title");
        Label subtitle = new Label("Clique pour voir le résumé · Glisse pour réordonner.");
        subtitle.getStyleClass().add("auth-subtitle");
        textBlock.getChildren().addAll(cardTitle, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        createCharacterButton = new Button("+ Créer un personnage");
        createCharacterButton.getStyleClass().add("btn-primary");
        chatbotButton = new Button("Assistant JDR");
         chatbotButton.getStyleClass().add("btn-secondary");


        header.getChildren().addAll(textBlock, spacer,chatbotButton ,createCharacterButton);

        Region sep = new Region();
        sep.getStyleClass().add("auth-divider");
        sep.setPrefHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);

        personnagesListView = new ListView<>();
        personnagesListView.getStyleClass().add("profile-list-view");
        personnagesListView.setFixedCellSize(CELL_HEIGHT);
        personnagesListView.prefHeightProperty().bind(
                javafx.beans.binding.Bindings
                        .min(
                            javafx.beans.binding.Bindings.size(personnagesListView.getItems()),
                            5
                        )
                        .multiply(CELL_HEIGHT)
                        .add(2)
        );
        personnagesListView.minHeightProperty().bind(personnagesListView.prefHeightProperty());
        personnagesListView.setMaxHeight(LIST_MAX_H);

        Label emptyLabel = new Label("Aucun personnage pour l'instant.");
        emptyLabel.getStyleClass().add("profile-empty-label");
        personnagesListView.setPlaceholder(emptyLabel);

        personnagesListView.setCellFactory(lv -> new DraggablePersonnageCell(personnagesListView));

        personnagesListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, selected) -> {
                    if (selected != null) afficherResume(selected);
                }
        );

        resumePane = new VBox(14);
        resumePane.setVisible(false);
        resumePane.setManaged(false);
        resumePane.setMaxWidth(Double.MAX_VALUE);
        resumePane.setOnMousePressed(e -> e.consume());

        card.getChildren().addAll(header, sep, personnagesListView, resumePane);
        return card;
    }

    private void afficherResume(Personnage p) {
        resumePane.getChildren().clear();

        Region sepTop = new Region();
        sepTop.getStyleClass().add("auth-divider");
        sepTop.setPrefHeight(1);
        sepTop.setMaxWidth(Double.MAX_VALUE);

        HBox mainRow = new HBox(20);
        mainRow.setAlignment(Pos.TOP_LEFT);
        mainRow.setMaxWidth(Double.MAX_VALUE);

        VBox photoBox = buildResumePhoto(p);

        VBox infoBox = new VBox(6);
        infoBox.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        Label nomLabel = new Label(p.getNom());
        nomLabel.setStyle(
            "-fx-font-size: 18px; -fx-font-weight: bold;" +
            "-fx-text-fill: #ffd700; -fx-font-family: Arial;"
        );

        Label raceClasseLabel = new Label(p.getRace() + " · " + p.getClasse() + " · Niveau " + p.getNiveau());
        raceClasseLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.50);" +
            "-fx-font-family: Arial;"
        );

        FichePersonnage fiche = p.getFiche();
        String bio = (fiche != null && fiche.getBiographie() != null && !fiche.getBiographie().isBlank())
                ? fiche.getBiographie() : "Aucune biographie.";

        Label bioTitle = new Label("BIOGRAPHIE");
        bioTitle.getStyleClass().add("auth-field-label");

        Label bioLabel = new Label(bio);
        bioLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.65);" +
            "-fx-font-family: Arial;"
        );
        bioLabel.setWrapText(true);
        bioLabel.setMaxWidth(Double.MAX_VALUE);

        infoBox.getChildren().addAll(nomLabel, raceClasseLabel, bioTitle, bioLabel);
        mainRow.getChildren().addAll(photoBox, infoBox);

        VBox statsSection = buildResumeStats(fiche);

        ajouterEquipementButton = new Button("+ Ajouter un équipement");
        ajouterEquipementButton.getStyleClass().add("btn-secondary");

        afficherFicheButton = new Button("Afficher la fiche complète");
        afficherFicheButton.getStyleClass().add("btn-primary");

        HBox buttonsRow = new HBox(12, ajouterEquipementButton, afficherFicheButton);
        buttonsRow.setAlignment(Pos.CENTER_RIGHT);

        resumePane.getChildren().addAll(sepTop, mainRow, statsSection, buttonsRow);
        resumePane.setVisible(true);
        resumePane.setManaged(true);
    }

    private void fermerResume() {
        resumePane.setVisible(false);
        resumePane.setManaged(false);
        resumePane.getChildren().clear();
        personnagesListView.getSelectionModel().clearSelection();
    }

    private VBox buildResumePhoto(Personnage p) {
        VBox box = new VBox();
        box.setAlignment(Pos.TOP_CENTER);
        box.setMinWidth(90);
        box.setMaxWidth(90);

        Portrait portrait = p.getFiche() != null ? p.getFiche().getPortrait() : null;
        if (portrait != null && portrait.getCheminImage() != null) {
            try {
                File f = new File(portrait.getCheminImage());
                if (f.exists()) {
                    ImageView iv = new ImageView(new Image(f.toURI().toString(), 88, 88, false, true));
                    iv.setFitWidth(88);
                    iv.setFitHeight(88);
                    box.getChildren().add(iv);
                    return box;
                }
            } catch (Exception ignored) {}
        }

        String nom = p.getNom() != null ? p.getNom() : "?";
        Label placeholder = new Label(nom.length() >= 2
                ? nom.substring(0, 2).toUpperCase() : nom.toUpperCase());
        placeholder.setStyle(
            "-fx-font-size: 28px; -fx-font-weight: bold;" +
            "-fx-text-fill: #ff9800;" +
            "-fx-min-width: 88px; -fx-min-height: 88px;" +
            "-fx-max-width: 88px; -fx-max-height: 88px;" +
            "-fx-alignment: center;" +
            "-fx-background-color: rgba(255,152,0,0.12);" +
            "-fx-border-color: rgba(255,152,0,0.40);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );
        box.getChildren().add(placeholder);
        return box;
    }

    private VBox buildResumeStats(FichePersonnage fiche) {
        VBox section = new VBox(8);
        section.setMaxWidth(Double.MAX_VALUE);

        Label statsTitle = new Label("STATISTIQUES");
        statsTitle.getStyleClass().add("auth-field-label");

        FlowPane statsFlow = new FlowPane(16, 10);
        statsFlow.setMaxWidth(Double.MAX_VALUE);

        if (fiche != null && fiche.getStats() != null && !fiche.getStats().isEmpty()) {
            for (Stats stat : fiche.getStats()) {
                VBox statBox = new VBox(4);
                statBox.setAlignment(Pos.CENTER_LEFT);
                statBox.setPadding(new Insets(8, 14, 8, 14));
                statBox.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.04);" +
                    "-fx-border-color: rgba(255,255,255,0.08);" +
                    "-fx-border-width: 1;" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;"
                );

                Label statNom = new Label(stat.getNom().toUpperCase());
                statNom.setStyle(
                    "-fx-font-size: 10px; -fx-font-weight: bold;" +
                    "-fx-text-fill: rgba(255,215,0,0.60);" +
                    "-fx-font-family: Arial;"
                );

                Label stars = new Label(buildStars(stat.getValeur()));
                stars.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffd700;");

                statBox.getChildren().addAll(statNom, stars);
                statsFlow.getChildren().add(statBox);
            }
        } else {
            Label noStats = new Label("Aucune statistique enregistrée.");
            noStats.getStyleClass().add("profile-empty-label");
            statsFlow.getChildren().add(noStats);
        }

        section.getChildren().addAll(statsTitle, statsFlow);
        return section;
    }

    private String buildStars(int valeur) {
        int v = Math.max(0, Math.min(5, valeur));
        return "★".repeat(v) + "☆".repeat(5 - v);
    }

    private class DraggablePersonnageCell extends ListCell<Personnage> {

        private final ImageView thumb   = new ImageView();
        private final Label     avatar  = new Label();
        private final Label     nomLbl  = new Label();
        private final Label     infoLbl = new Label();
        private final HBox      row     = new HBox(14);

        DraggablePersonnageCell(ListView<Personnage> listView) {
            thumb.setFitWidth(52);
            thumb.setFitHeight(52);
            thumb.setPreserveRatio(false);

            avatar.getStyleClass().add("profile-avatar");
            nomLbl.getStyleClass().add("profile-list-name");
            infoLbl.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: rgba(255,255,255,0.42);" +
                "-fx-font-family: Arial;"
            );

            VBox textBox = new VBox(4, nomLbl, infoLbl);
            textBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(textBox, Priority.ALWAYS);

            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 6, 10, 6));
            row.getChildren().addAll(avatar, textBox);

            setOnDragDetected(e -> {
                if (getItem() == null) return;
                Dragboard db = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent cc = new ClipboardContent();
                cc.putString(String.valueOf(getIndex()));
                db.setContent(cc);
                e.consume();
            });

            setOnDragOver(e -> {
                if (e.getGestureSource() != this && e.getDragboard().hasString()) {
                    e.acceptTransferModes(TransferMode.MOVE);
                    setStyle(baseStyle() + "-fx-border-color: rgba(255,152,0,0.60); -fx-border-width: 0 0 2 0;");
                }
                e.consume();
            });

            setOnDragExited(e -> setStyle(baseStyle()));

            setOnDragDropped(e -> {
                Dragboard db = e.getDragboard();
                if (!db.hasString()) { e.setDropCompleted(false); return; }
                int from = Integer.parseInt(db.getString());
                int to   = getIndex();
                if (from != to) {
                    ObservableList<Personnage> items = listView.getItems();
                    Personnage moved = items.remove(from);
                    items.add(to, moved);
                    listView.getSelectionModel().select(to);
                }
                setStyle(baseStyle());
                e.setDropCompleted(true);
                e.consume();
            });

            setOnDragDone(e -> setStyle(baseStyle()));
        }

        private String baseStyle() { return "-fx-background-color: transparent;"; }

        @Override
        protected void updateItem(Personnage item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
                setText(null);
                setStyle(baseStyle());
                return;
            }

            String nom = item.getNom() != null ? item.getNom() : "Sans nom";
            nomLbl.setText(nom);
            infoLbl.setText(item.getRace() + " · " + item.getClasse() + " · Niv. " + item.getNiveau());

            Portrait portrait = item.getFiche() != null ? item.getFiche().getPortrait() : null;
            if (portrait != null && portrait.getCheminImage() != null) {
                try {
                    File f = new File(portrait.getCheminImage());
                    if (f.exists()) {
                        thumb.setImage(new Image(f.toURI().toString(), 52, 52, false, true));
                        if (!row.getChildren().contains(thumb)) row.getChildren().set(0, thumb);
                        setGraphic(row);
                        setStyle(baseStyle());
                        return;
                    }
                } catch (Exception ignored) {}
            }

            avatar.setText(nom.length() >= 2 ? nom.substring(0, 2).toUpperCase() : nom.toUpperCase());
            if (!row.getChildren().contains(avatar)) row.getChildren().set(0, avatar);
            setGraphic(row);
            setStyle(baseStyle());
        }
    }

    private VBox buildField(String labelText, Label valueLabel) {
        VBox box = new VBox(4);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        valueLabel.getStyleClass().add("profile-field-value");
        box.getChildren().addAll(lbl, valueLabel);
        return box;
    }

    public Parent getRoot()                               { return root; }
    public void setUserId(int id)                         { userIdValueLabel.setText(String.valueOf(id)); }
    public void setUsername(String name)                  { usernameValueLabel.setText(name); }
    public Button               getSettingsButton()       { return settingsButton; }
    public Button               getCreateCharacterButton(){ return createCharacterButton; }
    public ListView<Personnage> getPersonnagesListView()  { return personnagesListView; }
    public Button getAjouterEquipementButton()            { return ajouterEquipementButton; }
    public Button getAfficherFicheButton()                { return afficherFicheButton; }
    public MenuItem getChangePasswordItem()               { return changePasswordItem; }
    public MenuItem getDeleteAccountItem()                { return deleteAccountItem; }
    public MenuItem getLogoutItem()                       { return logoutItem; }
    public Button getChatbotButton() { return chatbotButton; }
}