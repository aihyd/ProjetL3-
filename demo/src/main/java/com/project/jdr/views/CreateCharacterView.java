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

    // Biographie
    private TextArea biographieArea;

    // Stats
    private Spinner<Integer> forceSpinner;
    private Spinner<Integer> agiliteSpinner;
    private Spinner<Integer> intelligenceSpinner;
    private Spinner<Integer> enduranceSpinner;

    // Compétences
    private TextField competenceNomField;
    private TextArea  competenceDescArea;
    private Button    ajouterCompetenceButton;
    private VBox      competencesListBox;

    // Boutons
    private Button createButton;
    private Button cancelButton;

    // Message
    private Label messageLabel;

    public CreateCharacterView() {
        root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);

        // ── Contenu principal ──────────────────────────────────────────
        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setPadding(new Insets(30, 40, 30, 40));
        contentWrapper.setMaxWidth(900);

        Label pageTitle = new Label("Créer un personnage");
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

        // ── ScrollPane qui prend tout l'espace ────────────────────────
        ScrollPane scrollPane = new ScrollPane(contentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // scrollPane prend tout l'espace du StackPane
        StackPane.setAlignment(scrollPane, Pos.TOP_CENTER);
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMaxHeight(Double.MAX_VALUE);

        root.getChildren().add(scrollPane);
        root.getStyleClass().add("auth-root");
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #12121f, #1e1e2f, #2b2b3c);");
    }

    // ── Carte portrait ────────────────────────────────────────────────────
    private VBox buildPortraitCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label sectionTitle = new Label("Portrait du personnage");
        sectionTitle.getStyleClass().add("profile-card-title");

        portraitImageView = new ImageView();
        portraitImageView.setFitWidth(120);
        portraitImageView.setFitHeight(120);
        portraitImageView.setPreserveRatio(true);

        Label placeholderLabel = new Label("👤");
        placeholderLabel.setStyle(
            "-fx-font-size: 48px;" +
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

        photoPathLabel = new Label("Aucune photo sélectionnée");
        photoPathLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px;");

        choisirPhotoButton = new Button("📁  Choisir une photo");
        choisirPhotoButton.getStyleClass().add("btn-secondary");
        choisirPhotoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un portrait");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );
            Stage stage = (Stage) root.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                cheminPhoto = file.getAbsolutePath();
                photoPathLabel.setText(file.getName());
                photoPathLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 11px;");
                Image image = new Image(file.toURI().toString());
                portraitImageView.setImage(image);
                imageContainer.getChildren().setAll(portraitImageView);
            }
        });

        Button supprimerPhotoButton = new Button("✕  Supprimer");
        supprimerPhotoButton.getStyleClass().add("btn-secondary");
        supprimerPhotoButton.setStyle("-fx-text-fill: #ff6b6b;");
        supprimerPhotoButton.setOnAction(e -> {
            cheminPhoto = null;
            portraitImageView.setImage(null);
            photoPathLabel.setText("Aucune photo sélectionnée");
            photoPathLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px;");
            imageContainer.getChildren().setAll(placeholderLabel);
        });

        HBox buttonsRow = new HBox(10);
        buttonsRow.setAlignment(Pos.CENTER_LEFT);
        buttonsRow.getChildren().addAll(choisirPhotoButton, supprimerPhotoButton);

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        infoBox.getChildren().addAll(buttonsRow, photoPathLabel);

        HBox content = new HBox(20);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMaxWidth(Double.MAX_VALUE);
        content.getChildren().addAll(imageContainer, infoBox);

        card.getChildren().addAll(sectionTitle, content);
        return card;
    }

    // ── Carte infos de base ───────────────────────────────────────────────
    private VBox buildBasicInfoCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label sectionTitle = new Label("Informations de base");
        sectionTitle.getStyleClass().add("profile-card-title");

        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(12);
        grid.setMaxWidth(Double.MAX_VALUE);

        nomField = new TextField();
        nomField.setPromptText("Nom du personnage");
        nomField.getStyleClass().add("register-field");
        nomField.setMaxWidth(Double.MAX_VALUE);

        raceCombo = new ComboBox<>();
        raceCombo.getItems().addAll("Humain", "Elfe", "Nain", "Orc", "Halfelin");
        raceCombo.setPromptText("Choisir une race");
        raceCombo.setMaxWidth(Double.MAX_VALUE);

        classeCombo = new ComboBox<>();
        classeCombo.getItems().addAll("Guerrier", "Mage", "Rôdeur", "Paladin", "Voleur", "Druide");
        classeCombo.setPromptText("Choisir une classe");
        classeCombo.setMaxWidth(Double.MAX_VALUE);

        niveauSpinner = new Spinner<>(1, 20, 1);
        niveauSpinner.setEditable(true);
        niveauSpinner.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(nomField,      Priority.ALWAYS);
        GridPane.setHgrow(raceCombo,     Priority.ALWAYS);
        GridPane.setHgrow(classeCombo,   Priority.ALWAYS);
        GridPane.setHgrow(niveauSpinner, Priority.ALWAYS);

        grid.add(buildField("NOM",    nomField),      0, 0);
        grid.add(buildField("RACE",   raceCombo),     1, 0);
        grid.add(buildField("CLASSE", classeCombo),   0, 1);
        grid.add(buildField("NIVEAU", niveauSpinner), 1, 1);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col, col);

        card.getChildren().addAll(sectionTitle, grid);
        return card;
    }

    // ── Carte biographie ──────────────────────────────────────────────────
    private VBox buildBiographieCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label sectionTitle = new Label("Biographie");
        sectionTitle.getStyleClass().add("profile-card-title");

        biographieArea = new TextArea();
        biographieArea.setPromptText("Décris l'histoire de ton personnage...");
        biographieArea.setPrefRowCount(4);
        biographieArea.setWrapText(true);
        biographieArea.setMaxWidth(Double.MAX_VALUE);
        biographieArea.setStyle(
            "-fx-text-fill: #000000;" +
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13px;"
        );

        card.getChildren().addAll(sectionTitle, biographieArea);
        return card;
    }

    // ── Carte stats ───────────────────────────────────────────────────────
    private VBox buildStatsCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label sectionTitle = new Label("Statistiques");
        sectionTitle.getStyleClass().add("profile-card-title");

        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(12);
        grid.setMaxWidth(Double.MAX_VALUE);

        forceSpinner        = new Spinner<>(1, 20, 5);
        agiliteSpinner      = new Spinner<>(1, 20, 5);
        intelligenceSpinner = new Spinner<>(1, 20, 5);
        enduranceSpinner    = new Spinner<>(1, 20, 5);

        for (Spinner<Integer> s : new Spinner[]{
                forceSpinner, agiliteSpinner, intelligenceSpinner, enduranceSpinner}) {
            s.setEditable(true);
            s.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(s, Priority.ALWAYS);
        }

        grid.add(buildField("FORCE",        forceSpinner),        0, 0);
        grid.add(buildField("AGILITÉ",      agiliteSpinner),      1, 0);
        grid.add(buildField("INTELLIGENCE", intelligenceSpinner), 0, 1);
        grid.add(buildField("ENDURANCE",    enduranceSpinner),    1, 1);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col, col);

        card.getChildren().addAll(sectionTitle, grid);
        return card;
    }

    // ── Carte compétences ─────────────────────────────────────────────────
    private VBox buildCompetencesCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label sectionTitle = new Label("Compétences");
        sectionTitle.getStyleClass().add("profile-card-title");

        competenceNomField = new TextField();
        competenceNomField.setPromptText("Nom de la compétence");
        competenceNomField.getStyleClass().add("register-field");
        competenceNomField.setMaxWidth(Double.MAX_VALUE);

        competenceDescArea = new TextArea();
        competenceDescArea.setPromptText("Description de la compétence...");
        competenceDescArea.setPrefRowCount(2);
        competenceDescArea.setWrapText(true);
        competenceDescArea.setMaxWidth(Double.MAX_VALUE);
        competenceDescArea.setStyle(
            "-fx-text-fill: #000000;" +
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13px;"
        );

        ajouterCompetenceButton = new Button("+ Ajouter");
        ajouterCompetenceButton.getStyleClass().add("btn-secondary");

        VBox fieldBox = new VBox(8);
        HBox.setHgrow(fieldBox, Priority.ALWAYS);
        fieldBox.setMaxWidth(Double.MAX_VALUE);
        fieldBox.getChildren().addAll(
            buildField("NOM", competenceNomField),
            buildField("DESCRIPTION", competenceDescArea)
        );

        HBox inputRow = new HBox(12);
        inputRow.setAlignment(Pos.TOP_RIGHT);
        inputRow.setMaxWidth(Double.MAX_VALUE);
        inputRow.getChildren().addAll(fieldBox, ajouterCompetenceButton);

        competencesListBox = new VBox(8);
        competencesListBox.setMaxWidth(Double.MAX_VALUE);
        competencesListBox.setStyle(
            "-fx-background-color: rgba(255,255,255,0.03);" +
            "-fx-border-color: rgba(255,255,255,0.07);" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8;"
        );

        Label emptyLabel = new Label("Aucune compétence ajoutée.");
        emptyLabel.getStyleClass().add("profile-empty-label");
        competencesListBox.getChildren().add(emptyLabel);

        card.getChildren().addAll(sectionTitle, inputRow, competencesListBox);
        return card;
    }

    // ── Helper champ ──────────────────────────────────────────────────────
    private VBox buildField(String labelText, javafx.scene.Node control) {
        VBox box = new VBox(4);
        box.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(box, Priority.ALWAYS);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        box.getChildren().addAll(lbl, control);
        return box;
    }

    // ── Boutons ───────────────────────────────────────────────────────────
    private HBox buildButtons() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("btn-secondary");

        createButton = new Button("Créer le personnage");
        createButton.getStyleClass().add("btn-primary");

        HBox hbox = new HBox(16);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(8, 0, 8, 0));
        hbox.setMaxWidth(Double.MAX_VALUE);
        hbox.getChildren().addAll(spacer, cancelButton, createButton);
        return hbox;
    }

    // ── API publique ──────────────────────────────────────────────────────
    public Parent getRoot()           { return root; }

    public String getNom()            { return nomField.getText(); }
    public String getRace()           { return raceCombo.getValue(); }
    public String getClasse()         { return classeCombo.getValue(); }
    public int    getNiveau()         { return niveauSpinner.getValue(); }
    public String getBiographie()     { return biographieArea.getText(); }
    public int    getForce()          { return forceSpinner.getValue(); }
    public int    getAgilite()        { return agiliteSpinner.getValue(); }
    public int    getIntelligence()   { return intelligenceSpinner.getValue(); }
    public int    getEndurance()      { return enduranceSpinner.getValue(); }
    public String getCheminPhoto()    { return cheminPhoto; }

    public TextField getCompetenceNomField()      { return competenceNomField; }
    public TextArea  getCompetenceDescArea()      { return competenceDescArea; }
    public Button    getAjouterCompetenceButton() { return ajouterCompetenceButton; }
    public VBox      getCompetencesListBox()      { return competencesListBox; }

    public Button getCreateButton()   { return createButton; }
    public Button getCancelButton()   { return cancelButton; }
    public Label  getMessageLabel()   { return messageLabel; }
}