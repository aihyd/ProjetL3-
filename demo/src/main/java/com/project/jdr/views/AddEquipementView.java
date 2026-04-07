package com.project.jdr.views;

import com.project.jdr.model.Equipement;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AddEquipementView {

    private final StackPane root;

    private ComboBox<Equipement> equipementExistantCombo;
    private TextField            nomField;
    private TextArea             descriptionArea;
    private Button               confirmerButton;
    private Label                messageLabel;
    private Label                backToProfile;

    public AddEquipementView() {

        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(36, 32, 36, 32));
        card.setMinWidth(280);
        card.setMaxWidth(520);
        card.setPrefWidth(Double.MAX_VALUE);

        Label title = new Label("Ajouter un équipement");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        Label subtitle = new Label("Remplis le formulaire ou sélectionne un équipement existant.");
        subtitle.getStyleClass().add("auth-subtitle");
        subtitle.setMaxWidth(Double.MAX_VALUE);
        subtitle.setAlignment(Pos.CENTER);
        subtitle.setWrapText(true);

        Region div1 = buildDivider();

        Label comboLabel = new Label("RÉUTILISER UN ÉQUIPEMENT EXISTANT");
        comboLabel.getStyleClass().add("auth-field-label");

        equipementExistantCombo = new ComboBox<>();
        equipementExistantCombo.setPromptText("Choisir un équipement existant (optionnel)…");
        equipementExistantCombo.setMaxWidth(Double.MAX_VALUE);

        equipementExistantCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Equipement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });
        equipementExistantCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Equipement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        VBox comboBox = new VBox(5, comboLabel, equipementExistantCombo);

        Region div2 = buildDivider();

        Label formLabel = new Label("OU CRÉER UN NOUVEL ÉQUIPEMENT");
        formLabel.getStyleClass().add("auth-field-label");

        Label nomLabel = new Label("NOM");
        nomLabel.getStyleClass().add("auth-field-label");
        nomField = new TextField();
        nomField.setPromptText("Nom de l'équipement");
        nomField.getStyleClass().add("auth-field");
        nomField.setMaxWidth(Double.MAX_VALUE);
        VBox nomBox = new VBox(5, nomLabel, nomField);

        Label descLabel = new Label("DESCRIPTION");
        descLabel.getStyleClass().add("auth-field-label");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Description de l'équipement...");
        descriptionArea.setPrefRowCount(4);
        descriptionArea.setWrapText(true);
        descriptionArea.setMaxWidth(Double.MAX_VALUE);
        descriptionArea.getStyleClass().add("text-area");

        // Force le fond sombre — le ScrollPane interne du TextArea
        // a un fond blanc par défaut en JavaFX
        descriptionArea.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene != null) {
                descriptionArea.lookup(".scroll-pane").setStyle(
                    "-fx-background-color: transparent;"
                );
                descriptionArea.lookup(".scroll-pane .viewport").setStyle(
                    "-fx-background-color: transparent;"
                );
                descriptionArea.lookup(".content").setStyle(
                    "-fx-background-color: transparent;"
                );
            }
        });

        VBox descBox = new VBox(5, descLabel, descriptionArea);

        VBox formBox = new VBox(12, formLabel, nomBox, descBox);

        Region div3 = buildDivider();

        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setAlignment(Pos.CENTER);
        messageLabel.setWrapText(true);

        confirmerButton = new Button("Ajouter l'équipement");
        confirmerButton.getStyleClass().add("btn-primary");
        confirmerButton.setMaxWidth(Double.MAX_VALUE);

        backToProfile = new Label("← Retour au profil");
        backToProfile.getStyleClass().add("action-link");
        backToProfile.setMaxWidth(Double.MAX_VALUE);
        backToProfile.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                title, subtitle,
                div1,
                comboBox,
                div2,
                formBox,
                div3,
                messageLabel,
                confirmerButton,
                backToProfile
        );

        root = new StackPane(card);
        root.getStyleClass().add("auth-root");
        StackPane.setMargin(card, new Insets(40, 60, 40, 60));
    }

    private Region buildDivider() {
        Region d = new Region();
        d.getStyleClass().add("auth-divider");
        d.setPrefHeight(1);
        d.setMaxWidth(Double.MAX_VALUE);
        return d;
    }

    public Parent                getRoot()                    { return root; }
    public ComboBox<Equipement>  getEquipementExistantCombo() { return equipementExistantCombo; }
    public TextField             getNomField()                { return nomField; }
    public TextArea              getDescriptionArea()         { return descriptionArea; }
    public Button                getConfirmerButton()         { return confirmerButton; }
    public Label                 getMessageLabel()            { return messageLabel; }
    public Label                 getBackToProfile()           { return backToProfile; }
}