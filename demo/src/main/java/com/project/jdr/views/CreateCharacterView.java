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
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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
import javafx.util.StringConverter;

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

    // Stats (entiers)
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

        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setPadding(new Insets(30, 40, 30, 40));
        // Pas de maxWidth fixe — on laisse le wrapper prendre toute la largeur
        // et on borne chaque card individuellement
        contentWrapper.setMaxWidth(Double.MAX_VALUE);

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

    // ── Carte portrait ────────────────────────────────────────────────────
    private VBox buildPortraitCard() {
        VBox card = buildCard();

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
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir un portrait");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
            Stage stage = (Stage) root.getScene().getWindow();
            File file = fc.showOpenDialog(stage);
            if (file != null) {
                cheminPhoto = file.getAbsolutePath();
                photoPathLabel.setText(file.getName());
                photoPathLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 11px;");
                portraitImageView.setImage(new Image(file.toURI().toString()));
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

    // ── Carte infos de base ───────────────────────────────────────────────
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
        classeCombo.getItems().addAll("Guerrier", "Mage", "Rôdeur", "Paladin", "Voleur", "Druide");
        classeCombo.setPromptText("Choisir une classe");
        classeCombo.setMaxWidth(Double.MAX_VALUE);

        niveauSpinner = new Spinner<>();
        configureIntegerSpinner(niveauSpinner, 1, 20, 1);
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

    // ── Carte biographie ──────────────────────────────────────────────────
    private VBox buildBiographieCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Biographie");
        sectionTitle.getStyleClass().add("profile-card-title");

        biographieArea = new TextArea();
        biographieArea.setPromptText("Décris l'histoire de ton personnage...");
        biographieArea.setPrefRowCount(4);
        biographieArea.setWrapText(true);
        biographieArea.setMaxWidth(Double.MAX_VALUE);
        biographieArea.getStyleClass().add("text-area");

        card.getChildren().addAll(sectionTitle, biographieArea);
        return card;
    }

    // ── Carte stats ───────────────────────────────────────────────────────
    private VBox buildStatsCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Statistiques");
        sectionTitle.getStyleClass().add("profile-card-title");

        Label hint = new Label("Saisis une valeur entière pour chaque statistique.");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.30); -fx-font-family: Arial;");

        forceSpinner        = createStatSpinner(1);
        agiliteSpinner      = createStatSpinner(1);
        intelligenceSpinner = createStatSpinner(1);
        enduranceSpinner    = createStatSpinner(1);

        for (Spinner<Integer> spinner : new Spinner[]{forceSpinner, agiliteSpinner, intelligenceSpinner, enduranceSpinner}) {
            spinner.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(spinner, Priority.ALWAYS);
        }

        GridPane grid = buildGrid();
        grid.add(buildStatEntry("FORCE",        forceSpinner),        0, 0);
        grid.add(buildStatEntry("AGILITÉ",      agiliteSpinner),      1, 0);
        grid.add(buildStatEntry("INTELLIGENCE", intelligenceSpinner), 0, 1);
        grid.add(buildStatEntry("ENDURANCE",    enduranceSpinner),    1, 1);

        card.getChildren().addAll(sectionTitle, hint, grid);
        return card;
    }

    // ── Carte compétences ─────────────────────────────────────────────────
    private VBox buildCompetencesCard() {
        VBox card = buildCard();

        Label sectionTitle = new Label("Compétences");
        sectionTitle.getStyleClass().add("profile-card-title");

        competenceNomField = new TextField();
        competenceNomField.setPromptText("Nom de la compétence");
        competenceNomField.getStyleClass().add("auth-field");
        competenceNomField.setMaxWidth(Double.MAX_VALUE);

        competenceDescArea = new TextArea();
        competenceDescArea.setPromptText("Description de la compétence...");
        competenceDescArea.setPrefRowCount(2);
        competenceDescArea.setWrapText(true);
        competenceDescArea.setMaxWidth(Double.MAX_VALUE);
        competenceDescArea.getStyleClass().add("text-area");

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

        Label emptyLabel = new Label("Aucune compétence ajoutée.");
        emptyLabel.getStyleClass().add("profile-empty-label");
        competencesListBox.getChildren().add(emptyLabel);

        card.getChildren().addAll(sectionTitle, inputRow, competencesListBox);
        return card;
    }

    // ── Helpers layout ────────────────────────────────────────────────────

    /** Card standard : pleine largeur, padding, style. */
    private VBox buildCard() {
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE); // s'étire avec la fenêtre
        VBox.setVgrow(card, Priority.NEVER);
        return card;
    }

    /** GridPane 2 colonnes 50/50 qui s'étire. */
    private GridPane buildGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(16);
        grid.setVgap(12);
        grid.setMaxWidth(Double.MAX_VALUE);
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHgrow(Priority.ALWAYS);
        col.setMinWidth(10); // évite le dépassement
        grid.getColumnConstraints().addAll(col, col);
        return grid;
    }

    /** Bloc label + contrôle, s'étire horizontalement. */
    private VBox buildFieldNode(String labelText, javafx.scene.Node control) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        VBox box = new VBox(4, lbl, control);
        box.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(box, Priority.ALWAYS);
        return box;
    }

    private Spinner<Integer> createStatSpinner(int initial) {
        Spinner<Integer> spinner = new Spinner<>();
        configureIntegerSpinner(spinner, 0, Integer.MAX_VALUE, Math.max(0, initial));
        spinner.getStyleClass().add("spinner");
        spinner.setMaxWidth(Double.MAX_VALUE);
        return spinner;
    }

    private void configureIntegerSpinner(Spinner<Integer> spinner, int min, int max, int initial) {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial);

        valueFactory.setConverter(new StringConverter<>() {
            @Override
            public String toString(Integer value) {
                return value == null ? String.valueOf(initial) : value.toString();
            }

            @Override
            public Integer fromString(String text) {
                if (text == null || text.isBlank()) {
                    return valueFactory.getValue();
                }
                if (!text.matches("\\d+")) {
                    return valueFactory.getValue();
                }
                try {
                    long parsed = Long.parseLong(text);
                    if (parsed < min) return min;
                    if (parsed > max) return max;
                    return (int) parsed;
                } catch (NumberFormatException ex) {
                    return valueFactory.getValue();
                }
            }
        });

        spinner.setValueFactory(valueFactory);
        spinner.setEditable(true);
        spinner.getEditor().setTextFormatter(new TextFormatter<>(change ->
                change.getControlNewText().matches("\\d*") ? change : null));
        spinner.focusedProperty().addListener((obs, oldValue, hasFocus) -> {
            if (!hasFocus) {
                spinner.increment(0);
            }
        });
    }

    /** Bloc stat : label + spinner entier dans un encadré, pleine largeur. */
    private VBox buildStatEntry(String labelText, Spinner<Integer> rating) {
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

    /** Barre de boutons bas de page. */
    private HBox buildButtons() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        cancelButton = new Button("Annuler");
        cancelButton.getStyleClass().add("btn-secondary");
        createButton = new Button("Créer le personnage");
        createButton.getStyleClass().add("btn-primary");
        HBox hbox = new HBox(16, spacer, cancelButton, createButton);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(8, 0, 8, 0));
        hbox.setMaxWidth(Double.MAX_VALUE);
        return hbox;
    }

    // ── API publique ──────────────────────────────────────────────────────
    public Parent getRoot()         { return root; }

    public String getNom()          { return nomField.getText(); }
    public String getRace()         { return raceCombo.getValue(); }
    public String getClasse()       { return classeCombo.getValue(); }
    public int    getNiveau()       { return niveauSpinner.getValue(); }
    public String getBiographie()   { return biographieArea.getText(); }
    public String getCheminPhoto()  { return cheminPhoto; }

    public int getForce()           { return forceSpinner.getValue(); }
    public int getAgilite()         { return agiliteSpinner.getValue(); }
    public int getIntelligence()    { return intelligenceSpinner.getValue(); }
    public int getEndurance()       { return enduranceSpinner.getValue(); }

    public TextField getCompetenceNomField()      { return competenceNomField; }
    public TextArea  getCompetenceDescArea()      { return competenceDescArea; }
    public Button    getAjouterCompetenceButton() { return ajouterCompetenceButton; }
    public VBox      getCompetencesListBox()      { return competencesListBox; }

    public Button getCreateButton() { return createButton; }
    public Button getCancelButton() { return cancelButton; }
    public Label  getMessageLabel() { return messageLabel; }
}