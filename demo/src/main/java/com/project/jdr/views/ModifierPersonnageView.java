package com.project.jdr.views;

import com.project.jdr.model.Competence;
import com.project.jdr.model.Equipement;
import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Stats;

import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ModifierPersonnageView {

    private final VBox root;

    private TextField        nomField;
    private ComboBox<String> raceCombo;
    private ComboBox<String> classeCombo;
    private Spinner<Integer> niveauSpinner;
    private TextArea         biographieArea;

    // Portrait
    private ImageView portraitImageView;
    private Label     photoPathLabel;
    private String    cheminPhoto      = null;

    private final List<StatRow>        statRows        = new ArrayList<>();
    private final List<CompetenceRow>  competenceRows  = new ArrayList<>();
    private final List<EquipementRow>  equipementRows  = new ArrayList<>();

    private VBox   competencesContainer;
    private VBox   equipementsContainer;
    private Button ajouterCompetenceButton;
    private Button ajouterEquipementButton;
    private Button confirmerButton;
    private Label  messageLabel;
    private Label  retourLabel;

    // ═════════════════════════════════════════════════════════════════════
    // Sélecteur de valeur entière (1 à infini)
    // ═════════════════════════════════════════════════════════════════════
    public static class StarRating extends HBox {

        private final Spinner<Integer> spinner;

        public StarRating()            { this(1); }
        public StarRating(int initial) {
            super(0);
            setAlignment(Pos.CENTER_LEFT);

            spinner = new Spinner<>(1, Integer.MAX_VALUE, Math.max(1, initial));
            spinner.setEditable(true);
            spinner.setPrefWidth(110);
            spinner.setMaxWidth(110);

            getChildren().add(spinner);
        }

        public void setValue(int val) { spinner.getValueFactory().setValue(Math.max(1, val)); }
        public int  getValue()        { return spinner.getValue(); }
    }

    // ═════════════════════════════════════════════════════════════════════

    public ModifierPersonnageView(Personnage personnage) {

        VBox pageContent = new VBox(16);
        pageContent.setAlignment(Pos.TOP_CENTER);
        pageContent.setPadding(new Insets(30, 40, 30, 40));

        Label pageTitle = new Label("Modifier  " + personnage.getNom());
        pageTitle.getStyleClass().add("title-label");
        pageTitle.setMaxWidth(Double.MAX_VALUE);
        pageTitle.setAlignment(Pos.CENTER_LEFT);

        pageContent.getChildren().addAll(
                pageTitle,
                buildPortraitCard(personnage),
                buildInfosCard(personnage),
                buildBioCard(personnage),
                buildStatsCard(personnage),
                buildCompetencesCard(personnage),
                buildEquipementsCard(personnage),
                buildBottomBar()
        );

        ScrollPane scroll = new ScrollPane(pageContent);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
        scroll.getStyleClass().clear();
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root = new VBox(scroll);
        root.getStyleClass().add("auth-root");
    }

    // ── Portrait ──────────────────────────────────────────────────────────
    private VBox buildPortraitCard(Personnage personnage) {
        VBox card = buildCard("Portrait");

        portraitImageView = new ImageView();
        portraitImageView.setFitWidth(100);
        portraitImageView.setFitHeight(100);
        portraitImageView.setPreserveRatio(true);

        // Charge la photo existante si disponible
        if (personnage.getFiche() != null && personnage.getFiche().getPortrait() != null) {
            String chemin = personnage.getFiche().getPortrait().getCheminImage();
            if (chemin != null && new File(chemin).exists()) {
                cheminPhoto = chemin;
                portraitImageView.setImage(new Image(new File(chemin).toURI().toString()));
            }
        }

        Label placeholderLabel = new Label("👤");
        placeholderLabel.setStyle(
            "-fx-font-size: 48px;" +
            "-fx-min-width: 100px; -fx-min-height: 100px;" +
            "-fx-max-width: 100px; -fx-max-height: 100px;" +
            "-fx-alignment: center;" +
            "-fx-background-color: rgba(255,152,0,0.10);" +
            "-fx-border-color: rgba(255,152,0,0.35);" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 50; -fx-background-radius: 50;"
        );

        VBox imageContainer = new VBox();
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setMinWidth(100);
        imageContainer.setMaxWidth(100);
        if (cheminPhoto != null) {
            imageContainer.getChildren().add(portraitImageView);
        } else {
            imageContainer.getChildren().add(placeholderLabel);
        }

        photoPathLabel = new Label(cheminPhoto != null
                ? new File(cheminPhoto).getName() : "Aucune photo selectionnee");
        photoPathLabel.setStyle("-fx-text-fill: " +
                (cheminPhoto != null ? "#2ecc71" : "rgba(255,255,255,0.35)") +
                "; -fx-font-size: 11px;");

        Button choisirButton = new Button("Choisir une photo");
        choisirButton.getStyleClass().add("btn-secondary");
        choisirButton.setOnAction(e -> {
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

        Button supprimerButton = new Button("Supprimer");
        supprimerButton.getStyleClass().add("btn-secondary");
        supprimerButton.setStyle("-fx-text-fill: #ff6b6b;");
        supprimerButton.setOnAction(e -> {
            cheminPhoto = null;
            portraitImageView.setImage(null);
            photoPathLabel.setText("Aucune photo selectionnee");
            photoPathLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.35); -fx-font-size: 11px;");
            imageContainer.getChildren().setAll(placeholderLabel);
        });

        HBox buttons = new HBox(10, choisirButton, supprimerButton);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox infoBox = new VBox(10, buttons, photoPathLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        HBox content = new HBox(20, imageContainer, infoBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().add(content);
        return card;
    }

    // ── Infos de base ─────────────────────────────────────────────────────
    private VBox buildInfosCard(Personnage personnage) {
        VBox card = buildCard("Informations de base");

        nomField = new TextField(personnage.getNom());
        nomField.getStyleClass().add("auth-field");
        nomField.setMaxWidth(Double.MAX_VALUE);

        raceCombo = new ComboBox<>();
        raceCombo.getItems().addAll("Humain", "Elfe", "Nain", "Orc", "Halfelin");
        raceCombo.setValue(personnage.getRace());
        raceCombo.setMaxWidth(Double.MAX_VALUE);

        classeCombo = new ComboBox<>();
        classeCombo.getItems().addAll("Guerrier", "Mage", "Rodeur", "Paladin", "Voleur", "Druide");
        classeCombo.setValue(personnage.getClasse());
        classeCombo.setMaxWidth(Double.MAX_VALUE);

        niveauSpinner = new Spinner<>();
        niveauSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, personnage.getNiveau()));
        niveauSpinner.setEditable(true);
        niveauSpinner.setMaxWidth(Double.MAX_VALUE);
        niveauSpinner.getStyleClass().add("spinner");

        GridPane grid = buildGrid();
        grid.add(buildField("NOM",    nomField),      0, 0);
        grid.add(buildField("RACE",   raceCombo),     1, 0);
        grid.add(buildField("CLASSE", classeCombo),   0, 1);
        grid.add(buildField("NIVEAU", niveauSpinner), 1, 1);

        card.getChildren().add(grid);
        return card;
    }

    // ── Biographie ────────────────────────────────────────────────────────
    private VBox buildBioCard(Personnage personnage) {
        VBox card = buildCard("Biographie");

        FichePersonnage fiche = personnage.getFiche();
        String bio = (fiche != null && fiche.getBiographie() != null) ? fiche.getBiographie() : "";

        biographieArea = new TextArea(bio);
        biographieArea.setPromptText("Biographie du personnage...");
        biographieArea.setPrefRowCount(4);
        biographieArea.setWrapText(true);
        biographieArea.setMaxWidth(Double.MAX_VALUE);
        biographieArea.getStyleClass().add("text-area");

        card.getChildren().add(biographieArea);
        return card;
    }

    // ── Stats avec étoiles ────────────────────────────────────────────────
    private VBox buildStatsCard(Personnage personnage) {
        VBox card = buildCard("Statistiques");

        Label hint = new Label("Saisis ou ajuste la valeur de chaque statistique (entier >= 1).");
        hint.setStyle(
            "-fx-font-size: 11px; -fx-text-fill: rgba(255,255,255,0.30); -fx-font-family: Arial;"
        );

        FichePersonnage fiche = personnage.getFiche();
        if (fiche != null && !fiche.getStats().isEmpty()) {
            GridPane grid = buildGrid();
            int col = 0, row = 0;
            for (Stats stat : fiche.getStats()) {
                StarRating rating = new StarRating(stat.getValeur());
                rating.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(rating, Priority.ALWAYS);

                statRows.add(new StatRow(stat.getId(), stat.getNom(), rating));
                grid.add(buildStatEntry(stat.getNom().toUpperCase(), rating), col, row);

                col++;
                if (col > 1) { col = 0; row++; }
            }
            card.getChildren().addAll(hint, grid);
        } else {
            Label none = new Label("Aucune statistique.");
            none.getStyleClass().add("profile-empty-label");
            card.getChildren().add(none);
        }

        return card;
    }

    // ── Compétences ───────────────────────────────────────────────────────
    private VBox buildCompetencesCard(Personnage personnage) {
        VBox card = buildCard("Competences");

        competencesContainer = new VBox(10);
        competencesContainer.setMaxWidth(Double.MAX_VALUE);

        FichePersonnage fiche = personnage.getFiche();
        if (fiche != null) {
            for (Competence c : fiche.getCompetences()) {
                CompetenceRow r = new CompetenceRow(c.getId(), c.getNom(), c.getDescription());
                competenceRows.add(r);
                competencesContainer.getChildren().add(r.buildRow());
            }
        }

        ajouterCompetenceButton = new Button("+ Ajouter une competence");
        ajouterCompetenceButton.getStyleClass().add("btn-secondary");

        card.getChildren().addAll(competencesContainer, ajouterCompetenceButton);
        return card;
    }

    // ── Équipements ───────────────────────────────────────────────────────
    private VBox buildEquipementsCard(Personnage personnage) {
        VBox card = buildCard("Equipements");

        equipementsContainer = new VBox(10);
        equipementsContainer.setMaxWidth(Double.MAX_VALUE);

        FichePersonnage fiche = personnage.getFiche();
        if (fiche != null) {
            for (Equipement e : fiche.getEquipements()) {
                EquipementRow r = new EquipementRow(e.getId(), e.getNom(), e.getDescription());
                equipementRows.add(r);
                equipementsContainer.getChildren().add(r.buildRow());
            }
        }

        ajouterEquipementButton = new Button("+ Ajouter un equipement");
        ajouterEquipementButton.getStyleClass().add("btn-secondary");

        card.getChildren().addAll(equipementsContainer, ajouterEquipementButton);
        return card;
    }

    // ── Barre du bas ──────────────────────────────────────────────────────
    private HBox buildBottomBar() {
        messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");
        messageLabel.setMaxWidth(Double.MAX_VALUE);
        messageLabel.setWrapText(true);
        HBox.setHgrow(messageLabel, Priority.ALWAYS);

        confirmerButton = new Button("Enregistrer les modifications");
        confirmerButton.getStyleClass().add("btn-primary");

        retourLabel = new Label("<- Retour a la fiche");
        retourLabel.getStyleClass().add("action-link");

        HBox bar = new HBox(16, retourLabel, messageLabel, confirmerButton);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 0, 8, 0));
        bar.setMaxWidth(Double.MAX_VALUE);
        return bar;
    }

    // ── Helpers layout ────────────────────────────────────────────────────
    private VBox buildCard(String titre) {
        VBox card = new VBox(12);
        card.getStyleClass().add("auth-card");
        card.setPadding(new Insets(20, 24, 20, 24));
        card.setMaxWidth(Double.MAX_VALUE);

        Label sectionTitle = new Label(titre.toUpperCase());
        sectionTitle.getStyleClass().add("auth-field-label");

        Region sep = new Region();
        sep.getStyleClass().add("auth-divider");
        sep.setPrefHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);

        card.getChildren().addAll(sectionTitle, sep);
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

    private VBox buildField(String labelText, javafx.scene.control.Control field) {
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("auth-field-label");
        field.getStyleClass().add("auth-field");
        field.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(5, lbl, field);
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
            "-fx-background-radius: 10;"
        );
        return box;
    }

    private VBox buildFieldSimple(String label, javafx.scene.control.Control field) {
        Label lbl = new Label(label);
        lbl.getStyleClass().add("auth-field-label");
        field.setMaxWidth(Double.MAX_VALUE);
        return new VBox(4, lbl, field);
    }

    public void ajouterLigneCompetence() {
        CompetenceRow r = new CompetenceRow(-1, "", "");
        competenceRows.add(r);
        competencesContainer.getChildren().add(r.buildRow());
    }

    public void ajouterLigneEquipement() {
        EquipementRow r = new EquipementRow(-1, "", "");
        equipementRows.add(r);
        equipementsContainer.getChildren().add(r.buildRow());
    }

    // ── API publique ──────────────────────────────────────────────────────
    public Parent              getRoot()                       { return root; }
    public TextField           getNomField()                   { return nomField; }
    public ComboBox<String>    getRaceCombo()                  { return raceCombo; }
    public ComboBox<String>    getClasseCombo()                { return classeCombo; }
    public Spinner<Integer>    getNiveauSpinner()              { return niveauSpinner; }
    public TextArea            getBiographieArea()             { return biographieArea; }
    public List<StatRow>       getStatRows()                   { return statRows; }
    public List<CompetenceRow> getCompetenceRows()             { return competenceRows; }
    public List<EquipementRow> getEquipementRows()             { return equipementRows; }
    public Button              getConfirmerButton()            { return confirmerButton; }
    public Label               getMessageLabel()               { return messageLabel; }
    public Label               getRetourLabel()                { return retourLabel; }
    public Button              getAjouterCompetenceButton()    { return ajouterCompetenceButton; }
    public String              getCheminPhoto()                { return cheminPhoto; }
    public Button              getAjouterEquipementButton()    { return ajouterEquipementButton; }

    // ── StatRow — id + nom + StarRating ───────────────────────────────────
    public static class StatRow {
        public final int        id;
        public final String     nom;
        public final StarRating rating;
        StatRow(int id, String nom, StarRating rating) {
            this.id = id; this.nom = nom; this.rating = rating;
        }
        public int getValeur() { return rating.getValue(); }
    }

    // ── CompetenceRow ─────────────────────────────────────────────────────
    public class CompetenceRow {
        public int id;
        private final TextField nomField;
        private final TextArea  descArea;
        private boolean supprime = false;

        CompetenceRow(int id, String nom, String desc) {
            this.id = id;
            nomField = new TextField(nom);
            nomField.setPromptText("Nom");
            nomField.getStyleClass().add("auth-field");
            nomField.setMaxWidth(Double.MAX_VALUE);

            descArea = new TextArea(desc);
            descArea.setPromptText("Description");
            descArea.setPrefRowCount(2);
            descArea.setWrapText(true);
            descArea.setMaxWidth(Double.MAX_VALUE);
            descArea.getStyleClass().add("text-area");
        }

        public HBox buildRow() {
            VBox fields = new VBox(6,
                buildFieldSimple("NOM", nomField),
                buildFieldSimple("DESCRIPTION", descArea));
            HBox.setHgrow(fields, Priority.ALWAYS);

            Button suppr = new Button("✕");
            suppr.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #ff6b6b; -fx-font-size: 14px;" +
                "-fx-cursor: hand; -fx-border-color: transparent;"
            );
            suppr.setOnAction(e -> {
                supprime = true;
                fields.setDisable(true);
                fields.setOpacity(0.3);
                suppr.setText("↩");
                suppr.setOnAction(ev -> {
                    supprime = false;
                    fields.setDisable(false);
                    fields.setOpacity(1.0);
                    suppr.setText("✕");
                    suppr.setOnAction(this::handleSuppr);
                });
            });

            HBox row = new HBox(10, fields, suppr);
            row.setAlignment(Pos.TOP_RIGHT);
            row.setPadding(new Insets(8, 10, 8, 10));
            row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.03);" +
                "-fx-border-color: rgba(140,120,200,0.15);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
            );
            return row;
        }

        private void handleSuppr(javafx.event.ActionEvent e) {}
        public String  getNom()         { return nomField.getText().trim(); }
        public String  getDescription() { return descArea.getText().trim(); }
        public boolean isSupprime()     { return supprime; }
    }

    // ── EquipementRow ─────────────────────────────────────────────────────
    public class EquipementRow {
        public int id;
        private final TextField nomField;
        private final TextArea  descArea;
        private boolean supprime = false;

        EquipementRow(int id, String nom, String desc) {
            this.id = id;
            nomField = new TextField(nom);
            nomField.setPromptText("Nom");
            nomField.getStyleClass().add("auth-field");
            nomField.setMaxWidth(Double.MAX_VALUE);

            descArea = new TextArea(desc);
            descArea.setPromptText("Description");
            descArea.setPrefRowCount(2);
            descArea.setWrapText(true);
            descArea.setMaxWidth(Double.MAX_VALUE);
            descArea.getStyleClass().add("text-area");
        }

        public HBox buildRow() {
            VBox fields = new VBox(6,
                buildFieldSimple("NOM", nomField),
                buildFieldSimple("DESCRIPTION", descArea));
            HBox.setHgrow(fields, Priority.ALWAYS);

            Button suppr = new Button("✕");
            suppr.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #ff6b6b; -fx-font-size: 14px;" +
                "-fx-cursor: hand; -fx-border-color: transparent;"
            );
            suppr.setOnAction(e -> {
                supprime = true;
                fields.setDisable(true);
                fields.setOpacity(0.3);
                suppr.setText("↩");
                suppr.setOnAction(ev -> {
                    supprime = false;
                    fields.setDisable(false);
                    fields.setOpacity(1.0);
                    suppr.setText("✕");
                    suppr.setOnAction(this::handleSuppr);
                });
            });

            HBox row = new HBox(10, fields, suppr);
            row.setAlignment(Pos.TOP_RIGHT);
            row.setPadding(new Insets(8, 10, 8, 10));
            row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.03);" +
                "-fx-border-color: rgba(255,152,0,0.15);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
            );
            return row;
        }

        private void handleSuppr(javafx.event.ActionEvent e) {}
        public String  getNom()         { return nomField.getText().trim(); }
        public String  getDescription() { return descArea.getText().trim(); }
        public boolean isSupprime()     { return supprime; }
    }
}