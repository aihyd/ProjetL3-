package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PersonnageView {

    private final StackPane root;
    private final VBox page1;
    private final VBox page2;
    
    // Page 1
    private final TextField nom;
    private final TextField raceField;
    private final TextField classeField;
    private final Button btnSuivant;
    private final Button btnRetour;
    
    // Page 2
    private final TextField portraitField;
    private final TextArea biographieArea;
    private final TextField forceField;
    private final TextField agiliteField;
    private final TextField intelligenceField;
    private final Button btnPrecedent;
    private final Button btnCreer;

    private final Label message;

    public PersonnageView() {
        this.message = new Label();
        message.getStyleClass().add("message-label");
        message.setMaxWidth(Double.MAX_VALUE);
        message.setAlignment(Pos.CENTER);
        message.setWrapText(true);

        // --- PAGE 1 ---
        page1 = createCard();
        Label titre1 = createTitle("Nouveau Personnage (1/2)");
        VBox nomBox = buildFieldGroup("NOM DU PERSONNAGE", nom = new TextField());
        VBox raceBox = buildFieldGroup("RACE", raceField = new TextField());
        VBox classeBox = buildFieldGroup("CLASSE", classeField = new TextField());
        
        btnSuivant = new Button("Suivant");
        btnSuivant.getStyleClass().add("btn-primary");
        btnSuivant.setMaxWidth(Double.MAX_VALUE);
        
        btnRetour = new Button("Retour");
        btnRetour.getStyleClass().add("btn-secondary");
        btnRetour.setMaxWidth(Double.MAX_VALUE);

        page1.getChildren().addAll(titre1, buildDivider(), nomBox, raceBox, classeBox, buildDivider(), btnSuivant, btnRetour);

        // --- PAGE 2 ---
        page2 = createCard();
        page2.setVisible(false);
        Label titre2 = createTitle("Nouveau Personnage (2/2)");
        VBox portraitBox = buildFieldGroup("PORTRAIT (Chemin relatif)", portraitField = new TextField());
        
        Label lblBio = new Label("BIOGRAPHIE");
        lblBio.getStyleClass().add("auth-field-label");
        biographieArea = new TextArea();
        biographieArea.setPrefRowCount(3);
        biographieArea.setWrapText(true);
        biographieArea.getStyleClass().add("auth-field");
        VBox bioBox = new VBox(5, lblBio, biographieArea);

        Label lblStats = new Label("STATISTIQUES");
        lblStats.getStyleClass().add("auth-field-label");
        HBox statsBox = new HBox(10);
        statsBox.setAlignment(Pos.CENTER);
        forceField = createStatField("FOR");
        agiliteField = createStatField("AGI");
        intelligenceField = createStatField("INT");
        
        VBox forB = new VBox(5, new Label("FOR"), forceField);
        VBox agiB = new VBox(5, new Label("AGI"), agiliteField);
        VBox intB = new VBox(5, new Label("INT"), intelligenceField);
        forB.setAlignment(Pos.CENTER);
        agiB.setAlignment(Pos.CENTER);
        intB.setAlignment(Pos.CENTER);
        statsBox.getChildren().addAll(forB, agiB, intB);

        btnCreer = new Button("Créer le personnage");
        btnCreer.getStyleClass().add("btn-primary");
        btnCreer.setMaxWidth(Double.MAX_VALUE);

        btnPrecedent = new Button("Précédent");
        btnPrecedent.getStyleClass().add("btn-secondary");
        btnPrecedent.setMaxWidth(Double.MAX_VALUE);

        page2.getChildren().addAll(titre2, buildDivider(), portraitBox, bioBox, lblStats, statsBox, buildDivider(), btnCreer, btnPrecedent);

        // --- Navigation ---
        btnSuivant.setOnAction(e -> {
            page1.setVisible(false);
            page2.setVisible(true);
        });
        btnPrecedent.setOnAction(e -> {
            page2.setVisible(false);
            page1.setVisible(true);
        });

        root = new StackPane(page1, page2);
        root.getStyleClass().add("auth-root");
        root.setAlignment(Pos.CENTER);
    }

    private VBox createCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(30, 32, 30, 32));
        card.setMinWidth(350);
        card.setMaxWidth(450);
        card.setPrefWidth(Double.MAX_VALUE);
        return card;
    }

    private Label createTitle(String text) {
        Label t = new Label(text);
        t.getStyleClass().add("title-label");
        t.setMaxWidth(Double.MAX_VALUE);
        t.setAlignment(Pos.CENTER);
        return t;
    }

    private TextField createStatField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setPrefWidth(60);
        tf.getStyleClass().add("auth-field");
        tf.setAlignment(Pos.CENTER);
        return tf;
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

    public Parent getRoot() { return root; }
    public TextField getNom() { return nom; }
    public TextField getRaceField() { return raceField; }
    public TextField getClasseField() { return classeField; }
    public TextField getPortraitField() { return portraitField; }
    public TextArea getBiographieArea() { return biographieArea; }
    public TextField getForceField() { return forceField; }
    public TextField getAgiliteField() { return agiliteField; }
    public TextField getIntelligenceField() { return intelligenceField; }
    public Button getBtnCreer() { return btnCreer; }
    public Button getBtnRetour() { return btnRetour; }
    public Label getMessage() { return message; }
}