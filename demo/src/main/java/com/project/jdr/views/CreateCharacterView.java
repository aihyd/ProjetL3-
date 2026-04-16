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

<<<<<<< HEAD
    // Stats
    private Spinner<Integer> forceSpinner;
    private Spinner<Integer> agiliteSpinner;
    private Spinner<Integer> intelligenceSpinner;
    private Spinner<Integer> enduranceSpinner;
=======
    // Stats (étoiles)
    private StarRating forceRating;
    private StarRating agiliteRating;
    private StarRating intelligenceRating;
    private StarRating enduranceRating;
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

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

<<<<<<< HEAD
=======
    // ═════════════════════════════════════════════════════════════════════
    // StarRating — hover géré sur le HBox conteneur, pas sur chaque étoile
    // ═════════════════════════════════════════════════════════════════════
    public static class StarRating extends HBox {

        private static final int    MAX          = 5;
        private static final String COLOR_ACTIVE = "#ffd700";
        private static final String COLOR_HOVER  = "#ffdd55";
        private static final String COLOR_EMPTY  = "rgba(255,255,255,0.20)";

        private final Label[] stars  = new Label[MAX];
        private int           value  = 1;
        private int           hoverIndex = -1; // -1 = pas de hover

        public StarRating()              { this(1); }
        public StarRating(int initial)   {
            super(0);
            setAlignment(Pos.CENTER_LEFT);
            // Padding interne pour que la zone de hover soit continue entre étoiles
            setSpacing(0);
            this.value = clamp(initial);

            for (int i = 0; i < MAX; i++) {
                Label star = new Label("★");
                star.setPadding(new Insets(2, 5, 2, 5)); // zone cliquable généreuse
                star.setCursor(javafx.scene.Cursor.HAND);
                star.setMouseTransparent(false);
                stars[i] = star;
            }

            getChildren().addAll(stars);

            // ── Hover sur le HBox entier, pas sur chaque Label ────────────
            // Comme le HBox couvre tout l'espace, on détecte la position X
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

        /** Retourne l'index de l'étoile sous la position X dans le HBox. */
        private int getStarIndexAt(double x) {
            for (int i = 0; i < MAX; i++) {
                Label s = stars[i];
                if (x >= s.getBoundsInParent().getMinX()
                        && x <= s.getBoundsInParent().getMaxX()) {
                    return i;
                }
            }
            // Fallback : si entre deux étoiles, prendre la plus proche
            if (x < stars[0].getBoundsInParent().getMinX()) return 0;
            if (x > stars[MAX-1].getBoundsInParent().getMaxX()) return MAX - 1;
            return -1;
        }

        private void repaint() {
            int fill = (hoverIndex >= 0) ? hoverIndex + 1 : value;
            String color = (hoverIndex >= 0) ? COLOR_HOVER : COLOR_ACTIVE;
            for (int i = 0; i < MAX; i++) {
                stars[i].setStyle(
                    "-fx-font-size: 28px;" +
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

    // ═════════════════════════════════════════════════════════════════════

>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
    public CreateCharacterView() {
        root = new StackPane();
        root.setAlignment(Pos.TOP_CENTER);

<<<<<<< HEAD
        // ── Contenu principal ──────────────────────────────────────────
        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setPadding(new Insets(30, 40, 30, 40));
        contentWrapper.setMaxWidth(900);
=======
        VBox contentWrapper = new VBox(16);
        contentWrapper.setAlignment(Pos.TOP_CENTER);
        contentWrapper.setPadding(new Insets(30, 40, 30, 40));
        // Pas de maxWidth fixe — on laisse le wrapper prendre toute la largeur
        // et on borne chaque card individuellement
        contentWrapper.setMaxWidth(Double.MAX_VALUE);
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label pageTitle = new Label("Créer un personnage");
        pageTitle.getStyleClass().add("title-label");
        pageTitle.setMaxWidth(Double.MAX_VALUE);
        pageTitle.setAlignment(Pos.CENTER_LEFT);

        messageLabel = new Label();
        messageLabel.getStyleClass().add("register-message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);

        contentWrapper.getChildren().addAll(
<<<<<<< HEAD
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
=======
                pageTitle,
                buildPortraitCard(),
                buildBasicInfoCard(),
                buildBiographieCard(),
                buildStatsCard(),
                buildCompetencesCard(),
                messageLabel,
                buildButtons()
        );

>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
        ScrollPane scrollPane = new ScrollPane(contentWrapper);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
<<<<<<< HEAD

        // scrollPane prend tout l'espace du StackPane
        StackPane.setAlignment(scrollPane, Pos.TOP_CENTER);
=======
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
        scrollPane.setMaxWidth(Double.MAX_VALUE);
        scrollPane.setMaxHeight(Double.MAX_VALUE);

        root.getChildren().add(scrollPane);
        root.getStyleClass().add("auth-root");
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #12121f, #1e1e2f, #2b2b3c);");
    }

    // ── Carte portrait ────────────────────────────────────────────────────
    private VBox buildPortraitCard() {
<<<<<<< HEAD
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
=======
        VBox card = buildCard();
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label sectionTitle = new Label("Portrait du personnage");
        sectionTitle.getStyleClass().add("profile-card-title");

        portraitImageView = new ImageView();
        portraitImageView.setFitWidth(120);
        portraitImageView.setFitHeight(120);
        portraitImageView.setPreserveRatio(true);

        Label placeholderLabel = new Label("👤");
        placeholderLabel.setStyle(
<<<<<<< HEAD
            "-fx-font-size: 48px;" +
            "-fx-min-width: 120px; -fx-min-height: 120px;" +
            "-fx-max-width: 120px; -fx-max-height: 120px;" +
            "-fx-alignment: center;" +
            "-fx-background-color: rgba(255,152,0,0.10);" +
            "-fx-border-color: rgba(255,152,0,0.35);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 60;" +
            "-fx-background-radius: 60;"
=======
                "-fx-font-size: 48px;" +
                "-fx-min-width: 120px; -fx-min-height: 120px;" +
                "-fx-max-width: 120px; -fx-max-height: 120px;" +
                "-fx-alignment: center;" +
                "-fx-background-color: rgba(255,152,0,0.10);" +
                "-fx-border-color: rgba(255,152,0,0.35);" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 60;" +
                "-fx-background-radius: 60;"
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
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
<<<<<<< HEAD
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir un portrait");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );
            Stage stage = (Stage) root.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
=======
            FileChooser fc = new FileChooser();
            fc.setTitle("Choisir un portrait");
            fc.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"));
            Stage stage = (Stage) root.getScene().getWindow();
            File file = fc.showOpenDialog(stage);
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
            if (file != null) {
                cheminPhoto = file.getAbsolutePath();
                photoPathLabel.setText(file.getName());
                photoPathLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 11px;");
<<<<<<< HEAD
                Image image = new Image(file.toURI().toString());
                portraitImageView.setImage(image);
=======
                portraitImageView.setImage(new Image(file.toURI().toString()));
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
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

<<<<<<< HEAD
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
=======
        HBox buttonsRow = new HBox(10, choisirPhotoButton, supprimerPhotoButton);
        buttonsRow.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(10, buttonsRow, photoPathLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox content = new HBox(20, imageContainer, infoBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMaxWidth(Double.MAX_VALUE);
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        card.getChildren().addAll(sectionTitle, content);
        return card;
    }

    // ── Carte infos de base ───────────────────────────────────────────────
    private VBox buildBasicInfoCard() {
<<<<<<< HEAD
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
=======
        VBox card = buildCard();
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label sectionTitle = new Label("Informations de base");
        sectionTitle.getStyleClass().add("profile-card-title");

<<<<<<< HEAD
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(12);
        grid.setMaxWidth(Double.MAX_VALUE);

        nomField = new TextField();
        nomField.setPromptText("Nom du personnage");
        nomField.getStyleClass().add("register-field");
=======
        nomField = new TextField();
        nomField.setPromptText("Nom du personnage");
        nomField.getStyleClass().add("auth-field");
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
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
<<<<<<< HEAD

=======
        niveauSpinner.getStyleClass().add("spinner");

        GridPane grid = buildGrid();
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
        GridPane.setHgrow(nomField,      Priority.ALWAYS);
        GridPane.setHgrow(raceCombo,     Priority.ALWAYS);
        GridPane.setHgrow(classeCombo,   Priority.ALWAYS);
        GridPane.setHgrow(niveauSpinner, Priority.ALWAYS);

<<<<<<< HEAD
        grid.add(buildField("NOM",    nomField),      0, 0);
        grid.add(buildField("RACE",   raceCombo),     1, 0);
        grid.add(buildField("CLASSE", classeCombo),   0, 1);
        grid.add(buildField("NIVEAU", niveauSpinner), 1, 1);

        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        col.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(col, col);
=======
        grid.add(buildFieldNode("NOM",    nomField),      0, 0);
        grid.add(buildFieldNode("RACE",   raceCombo),     1, 0);
        grid.add(buildFieldNode("CLASSE", classeCombo),   0, 1);
        grid.add(buildFieldNode("NIVEAU", niveauSpinner), 1, 1);
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        card.getChildren().addAll(sectionTitle, grid);
        return card;
    }

    // ── Carte biographie ──────────────────────────────────────────────────
    private VBox buildBiographieCard() {
<<<<<<< HEAD
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
=======
        VBox card = buildCard();
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label sectionTitle = new Label("Biographie");
        sectionTitle.getStyleClass().add("profile-card-title");

        biographieArea = new TextArea();
        biographieArea.setPromptText("Décris l'histoire de ton personnage...");
        biographieArea.setPrefRowCount(4);
        biographieArea.setWrapText(true);
        biographieArea.setMaxWidth(Double.MAX_VALUE);
<<<<<<< HEAD
        biographieArea.setStyle(
            "-fx-text-fill: #000000;" +
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13px;"
        );
=======
        biographieArea.getStyleClass().add("text-area");
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        card.getChildren().addAll(sectionTitle, biographieArea);
        return card;
    }

    // ── Carte stats ───────────────────────────────────────────────────────
    private VBox buildStatsCard() {
<<<<<<< HEAD
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
=======
        VBox card = buildCard();
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label sectionTitle = new Label("Statistiques");
        sectionTitle.getStyleClass().add("profile-card-title");

<<<<<<< HEAD
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
=======
        Label hint = new Label("Clique sur les étoiles pour noter chaque statistique de 1 à 5.");
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.30); -fx-font-family: Arial;");

        forceRating        = new StarRating(1);
        agiliteRating      = new StarRating(1);
        intelligenceRating = new StarRating(1);
        enduranceRating    = new StarRating(1);

        // Les StarRating s'étirent avec leur cellule
        for (StarRating r : new StarRating[]{forceRating, agiliteRating, intelligenceRating, enduranceRating}) {
            r.setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(r, Priority.ALWAYS);
        }

        GridPane grid = buildGrid();
        grid.add(buildStatEntry("FORCE",        forceRating),        0, 0);
        grid.add(buildStatEntry("AGILITÉ",      agiliteRating),      1, 0);
        grid.add(buildStatEntry("INTELLIGENCE", intelligenceRating), 0, 1);
        grid.add(buildStatEntry("ENDURANCE",    enduranceRating),    1, 1);

        card.getChildren().addAll(sectionTitle, hint, grid);
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
        return card;
    }

    // ── Carte compétences ─────────────────────────────────────────────────
    private VBox buildCompetencesCard() {
<<<<<<< HEAD
        VBox card = new VBox(14);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);
=======
        VBox card = buildCard();
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label sectionTitle = new Label("Compétences");
        sectionTitle.getStyleClass().add("profile-card-title");

        competenceNomField = new TextField();
        competenceNomField.setPromptText("Nom de la compétence");
<<<<<<< HEAD
        competenceNomField.getStyleClass().add("register-field");
=======
        competenceNomField.getStyleClass().add("auth-field");
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
        competenceNomField.setMaxWidth(Double.MAX_VALUE);

        competenceDescArea = new TextArea();
        competenceDescArea.setPromptText("Description de la compétence...");
        competenceDescArea.setPrefRowCount(2);
        competenceDescArea.setWrapText(true);
        competenceDescArea.setMaxWidth(Double.MAX_VALUE);
<<<<<<< HEAD
        competenceDescArea.setStyle(
            "-fx-text-fill: #000000;" +
            "-fx-background-color: #ffffff;" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-font-size: 13px;"
        );
=======
        competenceDescArea.getStyleClass().add("text-area");
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        ajouterCompetenceButton = new Button("+ Ajouter");
        ajouterCompetenceButton.getStyleClass().add("btn-secondary");

<<<<<<< HEAD
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
=======
        VBox fieldBox = new VBox(8,
                buildFieldNode("NOM",         competenceNomField),
                buildFieldNode("DESCRIPTION", competenceDescArea));
        HBox.setHgrow(fieldBox, Priority.ALWAYS);
        fieldBox.setMaxWidth(Double.MAX_VALUE);

        HBox inputRow = new HBox(12, fieldBox, ajouterCompetenceButton);
        inputRow.setAlignment(Pos.TOP_RIGHT);
        inputRow.setMaxWidth(Double.MAX_VALUE);
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        competencesListBox = new VBox(8);
        competencesListBox.setMaxWidth(Double.MAX_VALUE);
        competencesListBox.setStyle(
<<<<<<< HEAD
            "-fx-background-color: rgba(255,255,255,0.03);" +
            "-fx-border-color: rgba(255,255,255,0.07);" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 8;"
        );
=======
                "-fx-background-color: rgba(255,255,255,0.03);" +
                "-fx-border-color: rgba(255,255,255,0.07);" +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 8;");
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

        Label emptyLabel = new Label("Aucune compétence ajoutée.");
        emptyLabel.getStyleClass().add("profile-empty-label");
        competencesListBox.getChildren().add(emptyLabel);

        card.getChildren().addAll(sectionTitle, inputRow, competencesListBox);
        return card;
    }

<<<<<<< HEAD
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
=======
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

    /** Bloc stat : label + StarRating dans un encadré, pleine largeur. */
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
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
        return hbox;
    }

    // ── API publique ──────────────────────────────────────────────────────
<<<<<<< HEAD
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
=======
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
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65

    public TextField getCompetenceNomField()      { return competenceNomField; }
    public TextArea  getCompetenceDescArea()      { return competenceDescArea; }
    public Button    getAjouterCompetenceButton() { return ajouterCompetenceButton; }
    public VBox      getCompetencesListBox()      { return competencesListBox; }

<<<<<<< HEAD
    public Button getCreateButton()   { return createButton; }
    public Button getCancelButton()   { return cancelButton; }
    public Label  getMessageLabel()   { return messageLabel; }
=======
    public Button getCreateButton() { return createButton; }
    public Button getCancelButton() { return cancelButton; }
    public Label  getMessageLabel() { return messageLabel; }
>>>>>>> b499372c8c1cc55af69895b48cf9b05636a55e65
}