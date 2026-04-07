package com.project.jdr.views;

import com.project.jdr.model.Competence;
import com.project.jdr.model.Equipement;
import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Portrait;
import com.project.jdr.model.Stats;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class FichePersonnageView {

    private static final double MIN_W  = 140;
    private static final double MIN_H  = 100;
    private static final double BORDER = 8;

    private final VBox   root;
    private final Pane   canvas;
    private final Button sauvegarderButton;
    private final Button retourButton;
    private final Button modifierButton;
    private final Button supprimerButton;

    // Corbeille — affichée en bas à droite du canvas
    private final Label corbeilleLabel = new Label("🗑");
    private boolean corbeilleActive    = false;

    private final List<FicheCard>     cards    = new ArrayList<>();
    private Runnable                  onSupprimerConfirme;
    // Appelé quand une carte est droppée sur la corbeille : reçoit la FicheCard
    private Consumer<FicheCard>       onSupprimerCarte;

    public FichePersonnageView(Personnage personnage) {

        retourButton = new Button("<- Retour");
        retourButton.getStyleClass().add("btn-secondary");

        Label titre = new Label(personnage.getNom() + "  -  Fiche personnage");
        titre.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold;" +
            "-fx-text-fill: #ffd700; -fx-font-family: Arial;"
        );
        HBox.setHgrow(titre, Priority.ALWAYS);

        sauvegarderButton = new Button("Sauvegarder");
        sauvegarderButton.getStyleClass().add("btn-primary");

        modifierButton = new Button("Modifier");
        modifierButton.getStyleClass().add("btn-secondary");

        supprimerButton = new Button("Supprimer");
        supprimerButton.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-border-color: rgba(231,76,60,0.45);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-text-fill: #ff6b6b;" +
            "-fx-font-size: 13px;" +
            "-fx-font-family: Arial;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-cursor: hand;"
        );

        supprimerButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supprimer le personnage");
            alert.setHeaderText("Supprimer " + personnage.getNom() + " ?");
            alert.setContentText(
                "Cette action est irreversible. " +
                "Toutes les donnees du personnage seront supprimees."
            );
            alert.getDialogPane().setStyle(
                "-fx-background-color: #1e1e2f;" +
                "-fx-border-color: rgba(231,76,60,0.35);" +
                "-fx-border-width: 1;"
            );
            alert.getDialogPane().lookupAll(".label").forEach(n ->
                    n.setStyle("-fx-text-fill: #ffffff; -fx-font-family: Arial;"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (onSupprimerConfirme != null) onSupprimerConfirme.run();
            }
        });

        HBox topBar = new HBox(12, retourButton, titre, modifierButton, sauvegarderButton, supprimerButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle(
            "-fx-background-color: rgba(255,255,255,0.03);" +
            "-fx-border-color: transparent transparent rgba(255,215,0,0.08) transparent;" +
            "-fx-border-width: 1;"
        );

        // ── Canvas ────────────────────────────────────────────────────────
        canvas = new Pane();
        canvas.setPrefSize(1200, 800);
        canvas.setStyle(
            "-fx-background-color: rgba(255,255,255,0.01);" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );

        // ── Corbeille fixée en bas à droite du canvas ─────────────────────
        corbeilleLabel.setStyle(corbeilleStyle(false));
        corbeilleLabel.setVisible(false);
        corbeilleLabel.setManaged(false);

        // Position dans le Pane — on la repositionne quand le canvas change de taille
        canvas.widthProperty().addListener((obs, o, n) ->
                corbeilleLabel.setLayoutX(n.doubleValue() - 70));
        canvas.heightProperty().addListener((obs, o, n) ->
                corbeilleLabel.setLayoutY(n.doubleValue() - 70));
        corbeilleLabel.setLayoutX(1130);
        corbeilleLabel.setLayoutY(730);

        // Accepte le drop des cartes compétence/équipement
        corbeilleLabel.setOnDragOver(e -> {
            if (e.getDragboard().hasString()) {
                e.acceptTransferModes(TransferMode.MOVE);
                corbeilleLabel.setStyle(corbeilleStyle(true));
            }
            e.consume();
        });

        corbeilleLabel.setOnDragExited(e -> {
            corbeilleLabel.setStyle(corbeilleStyle(false));
            e.consume();
        });

        corbeilleLabel.setOnDragDropped(e -> {
            String id = e.getDragboard().getString();
            FicheCard target = cards.stream()
                    .filter(c -> String.valueOf(c.getEntityId()).equals(id))
                    .findFirst().orElse(null);

            if (target != null && onSupprimerCarte != null) {
                // Confirmation
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Supprimer l'element");
                alert.setHeaderText("Supprimer cet element ?");
                alert.setContentText("Cette action supprimera definitivement cet element de la fiche.");
                alert.getDialogPane().setStyle(
                    "-fx-background-color: #1e1e2f;" +
                    "-fx-border-color: rgba(231,76,60,0.35);" +
                    "-fx-border-width: 1;"
                );
                alert.getDialogPane().lookupAll(".label").forEach(n ->
                        n.setStyle("-fx-text-fill: #ffffff; -fx-font-family: Arial;"));

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    onSupprimerCarte.accept(target);
                }
            }

            corbeilleLabel.setStyle(corbeilleStyle(false));
            e.setDropCompleted(true);
            e.consume();
        });

        canvas.getChildren().add(corbeilleLabel);

        FichePersonnage fiche = personnage.getFiche();
        if (fiche != null) buildCards(fiche, personnage);

        StackPane canvasWrapper = new StackPane(canvas);
        canvasWrapper.setAlignment(Pos.CENTER);
        canvasWrapper.setPadding(new Insets(24));
        canvasWrapper.setStyle("-fx-background-color: transparent;");

        ScrollPane scroll = new ScrollPane(canvasWrapper);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
        scroll.getStyleClass().clear();
        VBox.setVgrow(scroll, Priority.ALWAYS);

        root = new VBox(topBar, scroll);
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #12121f, #1a1a2e, #1e1e2f);"
        );
    }

    private String corbeilleStyle(boolean survol) {
        return survol
            ? "-fx-font-size: 32px; -fx-cursor: hand;" +
              "-fx-background-color: rgba(231,76,60,0.30);" +
              "-fx-border-color: rgba(231,76,60,0.70);" +
              "-fx-border-width: 1; -fx-border-radius: 50; -fx-background-radius: 50;" +
              "-fx-padding: 10;"
            : "-fx-font-size: 28px; -fx-cursor: hand;" +
              "-fx-background-color: rgba(231,76,60,0.10);" +
              "-fx-border-color: rgba(231,76,60,0.35);" +
              "-fx-border-width: 1; -fx-border-radius: 50; -fx-background-radius: 50;" +
              "-fx-padding: 10;";
    }

    // ── Affiche/cache la corbeille selon qu'un drag est en cours ──────────
    public void afficherCorbeille(boolean visible) {
        corbeilleLabel.setVisible(visible);
        corbeilleLabel.setManaged(visible);
        corbeilleLabel.toFront();
    }

    private void buildCards(FichePersonnage fiche, Personnage personnage) {
        Portrait portrait = fiche.getPortrait();
        if (portrait != null) {
            FicheCard card = buildPortraitCard(portrait, personnage);
            placeCard(card, portrait.getX(), portrait.getY(), portrait.getWidth(), portrait.getHeight());
            register(card);
        }

        if (!fiche.getStats().isEmpty()) {
            Stats first = fiche.getStats().get(0);
            FicheCard card = buildStatsCard(fiche.getStats());
            placeCard(card, first.getX(), first.getY(),
                    Math.max(first.getWidth(), 240),
                    Math.max(first.getHeight(), fiche.getStats().size() * 38 + 50));
            register(card);
        }

        for (Competence c : fiche.getCompetences()) {
            FicheCard card = buildCompetenceCard(c);
            placeCard(card, c.getX(), c.getY(), c.getWidth(), c.getHeight());
            register(card);
        }

        for (Equipement e : fiche.getEquipements()) {
            FicheCard card = buildEquipementCard(e);
            placeCard(card, e.getX(), e.getY(), e.getWidth(), e.getHeight());
            register(card);
        }
    }

    public void supprimerCarteVisuellement(FicheCard card) {
        canvas.getChildren().remove(card);
        cards.remove(card);
    }

    private void register(FicheCard card) {
        canvas.getChildren().add(card);
        cards.add(card);
    }

    private void placeCard(FicheCard card, double x, double y, double w, double h) {
        card.setLayoutX(Math.max(10, x));
        card.setLayoutY(Math.max(10, y));
        card.setPrefWidth(Math.max(MIN_W, w));
        card.setPrefHeight(Math.max(MIN_H, h));
    }

    // ── Portrait ──────────────────────────────────────────────────────────
    private FicheCard buildPortraitCard(Portrait portrait, Personnage personnage) {
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);

        File f = new File(portrait.getCheminImage() != null ? portrait.getCheminImage() : "");
        if (f.exists()) {
            ImageView iv = new ImageView(new Image(f.toURI().toString()));
            iv.setPreserveRatio(true);
            iv.setFitWidth(100);
            iv.setFitHeight(100);
            content.widthProperty().addListener((obs, o, n) ->
                    iv.setFitWidth(Math.max(40, n.doubleValue() - 24)));
            content.heightProperty().addListener((obs, o, n) ->
                    iv.setFitHeight(Math.max(40, n.doubleValue() - 50)));
            content.getChildren().add(iv);
        } else {
            Label init = new Label(personnage.getNom().length() >= 2
                    ? personnage.getNom().substring(0, 2).toUpperCase()
                    : personnage.getNom().toUpperCase());
            init.setStyle(
                "-fx-font-size: 32px; -fx-font-weight: bold;" +
                "-fx-text-fill: #ff9800; -fx-font-family: Arial;" +
                "-fx-min-width: 70px; -fx-min-height: 70px;" +
                "-fx-max-width: 70px; -fx-max-height: 70px;" +
                "-fx-alignment: center;" +
                "-fx-background-color: rgba(255,152,0,0.08);" +
                "-fx-border-color: rgba(255,152,0,0.20);" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
            );
            content.getChildren().add(init);
        }

        Label nomLabel = new Label(personnage.getNom());
        nomLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial;"
        );
        nomLabel.setWrapText(true);
        nomLabel.maxWidthProperty().bind(content.widthProperty());
        content.getChildren().add(nomLabel);

        return new FicheCard("Portrait", content, portrait.getId(), "portrait", CardColor.GOLD);
    }

    // ── Stats ─────────────────────────────────────────────────────────────
    private FicheCard buildStatsCard(List<Stats> statsList) {
        VBox content = new VBox(0);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);

        for (Stats stat : statsList) {
            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setMaxWidth(Double.MAX_VALUE);
            row.setPadding(new Insets(5, 0, 5, 0));
            VBox.setVgrow(row, Priority.ALWAYS);

            Label nom = new Label(stat.getNom().toUpperCase());
            nom.setStyle(
                "-fx-font-size: 9px; -fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255,215,0,0.50); -fx-font-family: Arial;" +
                "-fx-min-width: 75px;"
            );

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label stars = new Label(buildStars(stat.getValeur()));
            stars.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffd700;");

            content.widthProperty().addListener((obs, o, n) -> {
                double w = n.doubleValue();
                double starSize = clamp(12 + (w - 140) / 260.0 * 10, 12, 22);
                double nomSize  = clamp(8  + (w - 140) / 260.0 * 4,  8,  12);
                stars.setStyle(String.format(
                    "-fx-font-size: %.0fpx; -fx-text-fill: #ffd700;", starSize));
                nom.setStyle(String.format(
                    "-fx-font-size: %.0fpx; -fx-font-weight: bold;" +
                    "-fx-text-fill: rgba(255,215,0,0.50);" +
                    "-fx-font-family: Arial; -fx-min-width: 70px;", nomSize));
            });

            row.getChildren().addAll(nom, spacer, stars);

            Region rowSep = new Region();
            rowSep.setPrefHeight(1);
            rowSep.setMaxWidth(Double.MAX_VALUE);
            rowSep.setStyle("-fx-background-color: rgba(255,255,255,0.04);");

            content.getChildren().addAll(row, rowSep);
        }

        return new FicheCard("Statistiques", content, -1, "stats", CardColor.GOLD);
    }

    // ── Competence ────────────────────────────────────────────────────────
    private FicheCard buildCompetenceCard(Competence c) {
        VBox content = new VBox(6);
        content.setMaxWidth(Double.MAX_VALUE);

        Label nomLabel = new Label(c.getNom());
        nomLabel.setWrapText(true);
        nomLabel.maxWidthProperty().bind(content.widthProperty());

        Label descLabel = new Label(c.getDescription() != null ? c.getDescription() : "");
        descLabel.setWrapText(true);
        descLabel.maxWidthProperty().bind(content.widthProperty());

        // Tailles responsives selon largeur
        content.widthProperty().addListener((obs, o, n) -> {
            double w = n.doubleValue();
            double nomSize  = clamp(11 + (w - 140) / 260.0 * 4, 11, 16);
            double descSize = clamp(10 + (w - 140) / 260.0 * 3, 10, 14);
            nomLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial;", nomSize));
            descLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx;" +
                "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial;", descSize));
        });

        // Style initial
        nomLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial;"
        );
        descLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial;"
        );

        content.getChildren().addAll(nomLabel, descLabel);
        return new FicheCard("Competence", content, c.getId(), "competence", CardColor.PURPLE);
    }

    // ── Equipement ────────────────────────────────────────────────────────
    private FicheCard buildEquipementCard(Equipement e) {
        VBox content = new VBox(6);
        content.setMaxWidth(Double.MAX_VALUE);

        Label nomLabel = new Label(e.getNom());
        nomLabel.setWrapText(true);
        nomLabel.maxWidthProperty().bind(content.widthProperty());

        Label descLabel = new Label(e.getDescription() != null ? e.getDescription() : "");
        descLabel.setWrapText(true);
        descLabel.maxWidthProperty().bind(content.widthProperty());

        // Tailles responsives selon largeur
        content.widthProperty().addListener((obs, o, n) -> {
            double w = n.doubleValue();
            double nomSize  = clamp(11 + (w - 140) / 260.0 * 4, 11, 16);
            double descSize = clamp(10 + (w - 140) / 260.0 * 3, 10, 14);
            nomLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial;", nomSize));
            descLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx;" +
                "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial;", descSize));
        });

        // Style initial
        nomLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial;"
        );
        descLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial;"
        );

        content.getChildren().addAll(nomLabel, descLabel);
        return new FicheCard("Equipement", content, e.getId(), "equipement", CardColor.ORANGE);
    }

    private String buildStars(int v) {
        v = Math.max(0, Math.min(5, v));
        return "★".repeat(v) + "☆".repeat(5 - v);
    }

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    // ── API ───────────────────────────────────────────────────────────────
    public Parent          getRoot()               { return root; }
    public Button          getSauvegarderButton()  { return sauvegarderButton; }
    public Button          getRetourButton()       { return retourButton; }
    public Button          getModifierButton()     { return modifierButton; }
    public Button          getSupprimerButton()    { return supprimerButton; }
    public List<FicheCard> getCards()              { return cards; }

    public void setOnSupprimerConfirme(Runnable r)           { this.onSupprimerConfirme = r; }
    public void setOnSupprimerCarte(Consumer<FicheCard> c)   { this.onSupprimerCarte = c; }

    public enum CardColor { GOLD, PURPLE, ORANGE }

    // ═════════════════════════════════════════════════════════════════════
    public class FicheCard extends VBox {

        private final int       entityId;
        private final String    type;
        private final CardColor color;

        private double  pressSceneX, pressSceneY;
        private double  pressLayoutX, pressLayoutY;
        private boolean dragging = false;
        private double  pressW, pressH;
        private ResizeEdge edge = ResizeEdge.NONE;

        private enum ResizeEdge { NONE, N, S, E, W, NE, NW, SE, SW }

        FicheCard(String titleText, VBox content, int entityId, String type, CardColor color) {
            this.entityId = entityId;
            this.type     = type;
            this.color    = color;

            setPadding(new Insets(0));
            applyStyle(false);

            Region accentLine = new Region();
            accentLine.setPrefHeight(2);
            accentLine.setMaxWidth(Double.MAX_VALUE);
            accentLine.setStyle("-fx-background-color: " + accentColor() + ";");

            Label dot = new Label("●");
            dot.setStyle("-fx-font-size: 7px; -fx-text-fill: " + accentColor() + ";");

            Label header = new Label(titleText.toUpperCase());
            header.setStyle(
                "-fx-font-size: 9px; -fx-font-weight: bold; -fx-font-family: Arial;" +
                "-fx-text-fill: rgba(255,255,255,0.35); -fx-cursor: move;"
            );

            // Hint Ctrl+drag pour corbeille (compétence/équipement seulement)
            Region headerSpacer = new Region();
            HBox.setHgrow(headerSpacer, Priority.ALWAYS);

            HBox headerRow;
            if ("competence".equals(type) || "equipement".equals(type)) {
                Label ctrlHint = new Label("Ctrl+drag → 🗑");
                ctrlHint.setStyle(
                    "-fx-font-size: 8px;" +
                    "-fx-text-fill: rgba(255,255,255,0.18);" +
                    "-fx-font-family: Arial;"
                );
                headerRow = new HBox(6, dot, header, headerSpacer, ctrlHint);
            } else {
                headerRow = new HBox(6, dot, header);
            }
            headerRow.setAlignment(Pos.CENTER_LEFT);
            headerRow.setPadding(new Insets(7, 10, 5, 10));
            headerRow.setMaxWidth(Double.MAX_VALUE);
            headerRow.setStyle("-fx-cursor: move;");

            Region sep = new Region();
            sep.setPrefHeight(1);
            sep.setMaxWidth(Double.MAX_VALUE);
            sep.setStyle("-fx-background-color: rgba(255,255,255,0.04);");

            content.setPadding(new Insets(10, 12, 12, 12));
            content.setMaxWidth(Double.MAX_VALUE);
            content.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(content, Priority.ALWAYS);

            Region resizeHandle = new Region();
            resizeHandle.setPrefHeight(5);
            resizeHandle.setMaxWidth(Double.MAX_VALUE);
            resizeHandle.setStyle("-fx-background-color: rgba(255,255,255,0.03);");

            getChildren().addAll(accentLine, headerRow, sep, content, resizeHandle);

            // ── Drag vers la corbeille : uniquement avec Ctrl enfoncé ──────
            if ("competence".equals(type) || "equipement".equals(type)) {
                setOnDragDetected(e -> {
                    if (!e.isControlDown()) return;
                    Dragboard db = startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString(String.valueOf(entityId));
                    db.setContent(cc);
                    afficherCorbeille(true);
                    e.consume();
                });

                setOnDragDone(e -> {
                    afficherCorbeille(false);
                    e.consume();
                });
            }

            // ── Move / resize ─────────────────────────────────────────────
            addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
                ResizeEdge detected = detectEdge(e.getX(), e.getY());
                setCursor(detected == ResizeEdge.NONE ? Cursor.DEFAULT : cursorFor(detected));
            });

            addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                toFront();
                pressSceneX  = e.getSceneX();
                pressSceneY  = e.getSceneY();
                pressLayoutX = getLayoutX();
                pressLayoutY = getLayoutY();
                pressW       = getPrefWidth();
                pressH       = getPrefHeight();
                edge         = detectEdge(e.getX(), e.getY());
                dragging     = (edge == ResizeEdge.NONE);
                applyStyle(true);
                e.consume();
            });

            addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
                double dx = e.getSceneX() - pressSceneX;
                double dy = e.getSceneY() - pressSceneY;
                if (dragging) {
                    setLayoutX(Math.max(0, pressLayoutX + dx));
                    setLayoutY(Math.max(0, pressLayoutY + dy));
                } else if (edge != ResizeEdge.NONE) {
                    applyResize(dx, dy);
                }
                e.consume();
            });

            addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
                dragging = false;
                edge     = ResizeEdge.NONE;
                applyStyle(false);
                e.consume();
            });

            addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                if (!dragging && edge == ResizeEdge.NONE) setCursor(Cursor.DEFAULT);
            });
        }

        private String accentColor() {
            return switch (color) {
                case GOLD   -> "rgba(255,215,0,0.45)";
                case PURPLE -> "rgba(140,120,200,0.45)";
                case ORANGE -> "rgba(255,152,0,0.45)";
            };
        }

        private String borderColor(boolean active) {
            if (active) return switch (color) {
                case GOLD   -> "rgba(255,215,0,0.50)";
                case PURPLE -> "rgba(140,120,200,0.50)";
                case ORANGE -> "rgba(255,152,0,0.50)";
            };
            return switch (color) {
                case GOLD   -> "rgba(255,215,0,0.18)";
                case PURPLE -> "rgba(140,120,200,0.18)";
                case ORANGE -> "rgba(255,152,0,0.18)";
            };
        }

        private void applyStyle(boolean active) {
            setStyle(
                "-fx-background-color: rgba(10,9,20," + (active ? "0.95" : "0.85") + ");" +
                "-fx-border-color: " + borderColor(active) + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                (active
                    ? "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.60),14,0,0,3);"
                    : "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.40),8,0,0,2);")
            );
        }

        private ResizeEdge detectEdge(double x, double y) {
            double w = getPrefWidth();
            double h = getPrefHeight();
            boolean top    = y <= BORDER;
            boolean bottom = y >= h - BORDER;
            boolean left   = x <= BORDER;
            boolean right  = x >= w - BORDER;
            if (top && left)     return ResizeEdge.NW;
            if (top && right)    return ResizeEdge.NE;
            if (bottom && left)  return ResizeEdge.SW;
            if (bottom && right) return ResizeEdge.SE;
            if (top)    return ResizeEdge.N;
            if (bottom) return ResizeEdge.S;
            if (left)   return ResizeEdge.W;
            if (right)  return ResizeEdge.E;
            return ResizeEdge.NONE;
        }

        private Cursor cursorFor(ResizeEdge e) {
            return switch (e) {
                case N, S   -> Cursor.V_RESIZE;
                case E, W   -> Cursor.H_RESIZE;
                case NE, SW -> Cursor.NE_RESIZE;
                case NW, SE -> Cursor.NW_RESIZE;
                default     -> Cursor.DEFAULT;
            };
        }

        private void applyResize(double dx, double dy) {
            double newW = pressW, newH = pressH;
            double newX = pressLayoutX, newY = pressLayoutY;

            switch (edge) {
                case E, NE, SE -> newW = Math.max(MIN_W, pressW + dx);
                case W, NW, SW -> {
                    newW = Math.max(MIN_W, pressW - dx);
                    newX = pressLayoutX + (pressW - newW);
                }
                default -> {}
            }
            switch (edge) {
                case S, SE, SW -> newH = Math.max(MIN_H, pressH + dy);
                case N, NE, NW -> {
                    newH = Math.max(MIN_H, pressH - dy);
                    newY = pressLayoutY + (pressH - newH);
                }
                default -> {}
            }

            setPrefWidth(newW);
            setPrefHeight(newH);
            setLayoutX(Math.max(0, newX));
            setLayoutY(Math.max(0, newY));
        }

        public int       getEntityId() { return entityId; }
        public String    getType()     { return type; }
        public CardColor getColor()    { return color; }
    }
}