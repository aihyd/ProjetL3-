package com.project.jdr.views;

import java.io.File;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CreateCharacterView {

    private final StackPane root;

    // Portrait
    private ImageView portraitImageView;
    private Button    choisirPhotoButton;
    private Label     photoPathLabel;
    private String    cheminPhoto = null;

    // Infos de base
    private TextField        nomField;
    private ComboBox<String> raceCombo;
    private ComboBox<String> classeCombo;
    private Spinner<Integer> niveauSpinner;
    //chatbot
    private Button chatbotButton;
    // Biographie
    private TextArea biographieArea;

    // Stats
    private StarRating forceRating;
    private StarRating agiliteRating;
    private StarRating intelligenceRating;
    private StarRating enduranceRating;

    // Competences
    private TextField competenceNomField;
    private TextArea  competenceDescArea;
    private Button    ajouterCompetenceButton;
    private VBox      competencesListBox;

    // Boutons
    private Button createButton;
    private Button cancelButton;

    // Message
    private Label messageLabel;

    // StarRating avec boutons simples
    public static class StarRating extends HBox {

        private static final int    MAX          = 5;
        private static final String COLOR_ACTIVE = "#ffd700";
        private static final String COLOR_HOVER  = "#ffdd55";
        private static final String COLOR_EMPTY  = "rgba(255,255,255,0.20)";

        private final Label[] stars  = new Label[MAX];
        private int           value  = 1;
        private int           hoverIndex = -1;

        public StarRating()            { this(1); }
        public StarRating(int initial) {
            super(0);
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(0);
            this.value = clamp(initial);

            for (int i = 0; i < MAX; i++) {
                Label star = new Label("[" + (i + 1) + "]");
                star.setPadding(new Insets(2, 5, 2, 5));
                star.setCursor(javafx.scene.Cursor.HAND);
                star.setMouseTransparent(false);
                stars[i] = star;
            }

            getChildren().addAll(stars);

            setOnMouseMoved(e -> {
                int idx = getStarIndexAt(e.getX());
                if (idx != hoverIndex) {
                    hoverIndex = idx;
                    repaint();
                }
            });

            setOnMouseExited(e -> {
                hoverIndex = -1;
                repaint();
            });

            setOnMouseClicked(e -> {
                int idx = getStarIndexAt(e.getX());
                if (idx >= 0) setValue(idx + 1);
            });

            repaint();
        }

        private int getStarIndexAt(double x) {
            for (int i = 0; i < MAX; i++) {
                Label s = stars[i];
                if (x >= s.getBoundsInParent().getMinX()
                        && x <= s.getBoundsInParent().getMaxX()) {
                    return i;
                }
            }
            if (x < stars[0].getBoundsInParent().getMinX()) return 0;
            if (x > stars[MAX-1].getBoundsInParent().getMaxX()) return MAX - 1;
            return -1;
        }

        private void repaint() {
            int fill = (hoverIndex >= 0) ? hoverIndex + 1 : value;
            String color = (hoverIndex >= 0) ? COLOR_HOVER : COLOR_ACTIVE;
            for (int i = 0; i < MAX; i++) {
                stars[i].setStyle(
                    "-fx-font-size: 18px; -fx-font-weight: bold;" +
                    "-fx-font-family: Arial;" +
                    "-fx-text-fill: " + (i < fill ? color : COLOR_EMPTY) + ";"
                );
            }
        }

        public void setValue(int val) {
            this.value = clamp(val);
            hoverIndex = -1;
            repaint();
        }

        public int getValue() { return value; }

        private static int clamp(int v) { return Math.max(1, Math.min(MAX, v)); }
    }

    public CreateCharacterView() {
        root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);

        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setPadding(new Insets(30, 40, 30, 40));
        contentWrapper.setMaxWidth(Double.MAX_VALUE);

        Label pageTitle = new Label("Creer un personnage");
        pageTitle.getStyleClass().add("title-label");
        pageTitle.setMaxWidth(Double.MAX_VALUE);
        pageTitle.setAlignment(Pos.CENTER_LEFT);

        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        contentWrapper.getChildren().addAll(
                pageTitle,
                buildPortraitCard(),
                buildBasicInfoCard(),
                buildBiographieCard(),
                buildStatsCard(),
                buildCompetencesCard(),
                messageLabel,
                buildButtons()
        );

        ScrollPane scrollPane = new ScrollPane(contentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMaxHeight(Double.MAX_VALUE);

        root.getChildren().add(scrollPane);
        root.getStyleClass().add("auth-root");
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #12121f, #1e1e2f, #2b2b3c);");
    }

    private VBox buildPortraitCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Portrait du personnage");
        sectionTitle.getStyleClass().add("profile-card-title");

        portraitImageView = new ImageView();
        portraitImageView.setFitWidth(120);
        portraitImageView.setFitHeight(120);
        portraitImageView.setPreserveRatio(true);

        Label placeholderLabel = new Label("?");
        placeholderLabel.setStyle(
                "-fx-font-size: 48px;" +
                "-fx-font-family: Arial;" +
                "-fx-min-width: 120px; -fx-min-height: 120px;" +
                "-fx-max-width: 120px; -fx-max-height: 120px;" +
                "-fx-alignment: center;" +
                "-fx-background-color: rgba(255,152,0,0.10);" +
                "-fx-border-color: rgba(255,152,0,0.35);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 60;" +
                "-fx-background-radius: 60;"
        );

        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setMinWidth(120);
        imageContainer.setMaxWidth(120);
        imageContainer.getChildren().add(placeholderLabel);

        photoPathLabel = new Label("Aucune photo selectionnee");
        photoPathLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px; -fx-font-family: Arial;");

        choisirPhotoButton = new Button("Choisir une photo");
        choisirPhotoButton.getStyleClass().add("btn-secondary");
        choisirPhotoButton.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir un portrait");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
            Stage stage = (Stage) root.getScene().getWindow();
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                cheminPhoto = file.getAbsolutePath();
                photoPathLabel.setText(file.getName());
                photoPathLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 11px; -fx-font-family: Arial;");
                portraitImageView.setImage(new Image(file.toURI().toString()));
                imageContainer.getChildren().setAll(portraitImageView);
            }
        });

        Button supprimerPhotoButton = new Button("Supprimer");
        supprimerPhotoButton.getStyleClass().add("btn-secondary");
        supprimerPhotoButton.setStyle("-fx-text-fill: #ff6b6b;");
        supprimerPhotoButton.setOnAction(e -> {
            cheminPhoto = null;
            portraitImageView.setImage(null);
            photoPathLabel.setText("Aucune photo selectionnee");
            photoPathLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px; -fx-font-family: Arial;");
            imageContainer.getChildren().setAll(placeholderLabel);
        });

        HBox buttonsRow = new HBox(10, choisirPhotoButton, supprimerPhotoButton);
        buttonsRow.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(10, buttonsRow, photoPathLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox content = new HBox(20, imageContainer, infoBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(sectionTitle, content);
        return card;
    }

    private VBox buildBasicInfoCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Informations de base");
        sectionTitle.getStyleClass().add("profile-card-title");

        nomField = new TextField();
        nomField.setPromptText("Nom du personnage");
        nomField.getStyleClass().add("auth-field");
        nomField.setMaxWidth(Double.MAX_VALUE);

        raceCombo = new ComboBox<>();
        raceCombo.getItems().addAll("Humain", "Elfe", "Nain", "Orc", "Halfelin");
        raceCombo.setPromptText("Choisir une race");
        raceCombo.setMaxWidth(Double.MAX_VALUE);

        classeCombo = new ComboBox<>();
        classeCombo.getItems().addAll("Guerrier", "Mage", "Rodeur", "Paladin", "Voleur", "Druide");
        classeCombo.setPromptText("Choisir une classe");
        classeCombo.setMaxWidth(Double.MAX_VALUE);

        niveauSpinner = new Spinner<>(1, 20, 1);
        niveauSpinner.setEditable(true);
        niveauSpinner.setMaxWidth(Double.MAX_VALUE);
        niveauSpinner.getStyleClass().add("spinner");

        GridPane grid = buildGrid();
        GridPane.setHgrow(nomField,      Priority.ALWAYS);
        GridPane.setHgrow(raceCombo,     Priority.ALWAYS);
        GridPane.setHgrow(classeCombo,   Priority.ALWAYS);
        GridPane.setHgrow(niveauSpinner, Priority.ALWAYS);

        grid.add(buildFieldNode("NOM",    nomField),      0, 0);
        grid.add(buildFieldNode("RACE",   raceCombo),     1, 0);
        grid.add(buildFieldNode("CLASSE", classeCombo),   0, 1);
        grid.add(buildFieldNode("NIVEAU", niveauSpinner), 1, 1);

        card.getChildren().addAll(sectionTitle, grid);
        return card;
    }

    private VBox buildBiographieCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Biographie");
        sectionTitle.getStyleClass().add("profile-card-title");

        biographieArea = new TextArea();
        biographieArea.setPromptText("Decris l'histoire de ton personnage...");
        biographieArea.setPrefRowCount(4);
        biographieArea.setWrapText(true);
        biographieArea.setMaxWidth(Double.MAX_VALUE);
        biographieArea.setStyle(
            "-fx-text-fill: #000000;" +
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13px;" +
            "-fx-font-family: Arial;"
        );

        card.getChildren().addAll(sectionTitle, biographieArea);
        return card;
    }

    private VBox buildStatsCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Statistiques");
        sectionTitle.getStyleClass().add("profile-card-title");

        Label hint = new Label("Clique sur les cases pour noter chaque statistique de 1 a 5.");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.30); -fx-font-family: Arial;");

        forceRating        = new StarRating(1);
        agiliteRating      = new StarRating(1);
        intelligenceRating = new StarRating(1);
        enduranceRating    = new StarRating(1);

        for (StarRating r : new StarRating[]{forceRating, agiliteRating, intelligenceRating, enduranceRating}) {
            r.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(r, Priority.ALWAYS);
        }

        GridPane grid = buildGrid();
        grid.add(buildStatEntry("FORCE",        forceRating),        0, 0);
        grid.add(buildStatEntry("AGILITE",      agiliteRating),      1, 0);
        grid.add(buildStatEntry("INTELLIGENCE", intelligenceRating), 0, 1);
        grid.add(buildStatEntry("ENDURANCE",    enduranceRating),    1, 1);

        card.getChildren().addAll(sectionTitle, hint, grid);
        return card;
    }

    private VBox buildCompetencesCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Competences");
        sectionTitle.getStyleClass().add("profile-card-title");

        competenceNomField = new TextField();
        competenceNomField.setPromptText("Nom de la competence");
        competenceNomField.getStyleClass().add("auth-field");
        competenceNomField.setMaxWidth(Double.MAX_VALUE);

        competenceDescArea = new TextArea();
        competenceDescArea.setPromptText("Description de la competence...");
        competenceDescArea.setPrefRowCount(2);
        competenceDescArea.setWrapText(true);
        competenceDescArea.setMaxWidth(Double.MAX_VALUE);
        competenceDescArea.setStyle(
            "-fx-text-fill: #000000;" +
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13px;" +
            "-fx-font-family: Arial;"
        );

        ajouterCompetenceButton = new Button("+ Ajouter");
        ajouterCompetenceButton.getStyleClass().add("btn-secondary");

        VBox fieldBox = new VBox(8,
                buildFieldNode("NOM",         competenceNomField),
                buildFieldNode("DESCRIPTION", competenceDescArea));
        HBox.setHgrow(fieldBox, Priority.ALWAYS);
        fieldBox.setMaxWidth(Double.MAX_VALUE);

        HBox inputRow = new HBox(12, fieldBox, ajouterCompetenceButton);
        inputRow.setAlignment(Pos.TOP_RIGHT);
        inputRow.setMaxWidth(Double.MAX_VALUE);

        competencesListBox = new VBox(8);
        competencesListBox.setMaxWidth(Double.MAX_VALUE);
        competencesListBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.03);" +
                "-fx-border-color: rgba(255,255,255,0.07);" +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");

        Label emptyLabel = new Label("Aucune competence ajoutee.");
        emptyLabel.getStyleClass().add("profile-empty-label");
        competencesListBox.getChildren().add(emptyLabel);

        card.getChildren().addAll(sectionTitle, inputRow, competencesListBox);
        return card;
    }

    private VBox buildCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(card, Priority.NEVER);
        return card;
    }

    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        grid.setMaxWidth(Double.MAX_VALUE);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHgrow(Priority.ALWAYS);
        col.setMinWidth(10);
        grid.getColumnConstraints().addAll(col, col);
        return grid;
    }

    private VBox buildFieldNode(String labelText, javafx.scene.Node control) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        VBox box = new VBox(4, lbl, control);
        box.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(box, Priority.ALWAYS);
        return box;
    }

    private VBox buildStatEntry(String labelText, StarRating rating) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        rating.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(8, lbl, rating);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(box, Priority.ALWAYS);
        GridPane.setHgrow(box, Priority.ALWAYS);
        box.setPadding(new Insets(12, 16, 12, 16));
        box.setStyle(
                "-fx-background-color: rgba(255,255,255,0.04);" +
                "-fx-border-color: rgba(255,255,255,0.08);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;");
        return box;
    }

    private HBox buildButtons() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("btn-secondary");
        createButton = new Button("Creer le personnage");
        createButton.getStyleClass().add("btn-primary");
        HBox hbox = new HBox(16, spacer, cancelButton, createButton);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(8, 0, 8, 0));
        hbox.setMaxWidth(Double.MAX_VALUE);
        return hbox;
    }

    public Parent getRoot()         { return root; }

    public String getNom()          { return nomField.getText(); }
    public String getRace()         { return raceCombo.getValue(); }
    public String getClasse()       { return classeCombo.getValue(); }
    public int    getNiveau()       { return niveauSpinner.getValue(); }
    public String getBiographie()   { return biographieArea.getText(); }
    public String getCheminPhoto()  { return cheminPhoto; }

    public int getForce()           { return forceRating.getValue(); }
    public int getAgilite()         { return agiliteRating.getValue(); }
    public int getIntelligence()    { return intelligenceRating.getValue(); }
    public int getEndurance()       { return enduranceRating.getValue(); }

    public StarRating getForceRating()        { return forceRating; }
    public StarRating getAgiliteRating()      { return agiliteRating; }
    public StarRating getIntelligenceRating() { return intelligenceRating; }
    public StarRating getEnduranceRating()    { return enduranceRating; }

    public TextField getCompetenceNomField()      { return competenceNomField; }
    public TextArea  getCompetenceDescArea()      { return competenceDescArea; }
    public Button    getAjouterCompetenceButton() { return ajouterCompetenceButton; }
    public VBox      getCompetencesListBox()      { return competencesListBox; }

    public Button getCreateButton() { return createButton; }
    public Button getCancelButton() { return cancelButton; }
    public Label  getMessageLabel() { return messageLabel; }
}