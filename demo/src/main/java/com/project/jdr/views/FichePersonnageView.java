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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

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
    private final Button supprimerButton;
    private final Button addPhotoButton;

    private final Label corbeilleLabel = new Label("SUPPR");

    private final List<FicheCard> cards = new ArrayList<>();
    private Runnable                onSupprimerConfirme;
    private Consumer<FicheCard>     onSupprimerCarte;

    private final Personnage personnageRef;
    private Portrait         portraitRef;
    private Label            titreLabel;
    private FicheCard        portraitCard;
    private VBox             portraitContent;

    private Consumer<Competence> onEditCompetence;
    private Consumer<Equipement> onEditEquipement;
    private Consumer<Stats>      onEditStats;
    private Consumer<String>     onEditNomPersonnage;
    private Consumer<Portrait>   onEditPortrait;
    private Consumer<Portrait>   onAddPortrait;

    public FichePersonnageView(Personnage personnage) {

        this.personnageRef = personnage;

        retourButton = new Button("<- Retour");
        retourButton.getStyleClass().add("btn-secondary");

        titreLabel = new Label(personnage.getNom() + "  -  Fiche personnage");
        titreLabel.setStyle(
            "-fx-font-size: 16px; -fx-font-weight: bold;" +
            "-fx-text-fill: #ffd700; -fx-font-family: Arial;"
        );
        HBox.setHgrow(titreLabel, Priority.ALWAYS);

        sauvegarderButton = new Button("Sauvegarder");
        sauvegarderButton.getStyleClass().add("btn-primary");

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
            styliserDialog(alert, "rgba(231,76,60,0.35)");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                if (onSupprimerConfirme != null) onSupprimerConfirme.run();
            }
        });

        Label hint = new Label("Astuce : double-clic sur un element pour le modifier");
        hint.setStyle("-fx-font-size: 10px; -fx-text-fill: rgba(255,255,255,0.30); -fx-font-family: Arial;");

        addPhotoButton = new Button("Ajouter photo");
        addPhotoButton.getStyleClass().add("btn-secondary");
        addPhotoButton.setOnAction(e -> startPortraitEdit(addPhotoButton));

        HBox topBar = new HBox(12, retourButton, titreLabel, hint, addPhotoButton, sauvegarderButton, supprimerButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12, 20, 12, 20));
        topBar.setStyle(
            "-fx-background-color: rgba(255,255,255,0.03);" +
            "-fx-border-color: transparent transparent rgba(255,215,0,0.08) transparent;" +
            "-fx-border-width: 1;"
        );

        canvas = new Pane();
        canvas.setPrefSize(1200, 800);
        canvas.setStyle(
            "-fx-background-color: rgba(255,255,255,0.01);" +
            "-fx-border-color: rgba(255,255,255,0.05);" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );

        corbeilleLabel.setStyle(corbeilleStyle(false));
        corbeilleLabel.setVisible(false);
        corbeilleLabel.setManaged(false);

        canvas.widthProperty().addListener((obs, o, n) ->
                corbeilleLabel.setLayoutX(n.doubleValue() - 80));
        canvas.heightProperty().addListener((obs, o, n) ->
                corbeilleLabel.setLayoutY(n.doubleValue() - 60));
        corbeilleLabel.setLayoutX(1120);
        corbeilleLabel.setLayoutY(740);

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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Supprimer l'element");
                alert.setHeaderText("Supprimer cet element ?");
                alert.setContentText("Cette action supprimera definitivement cet element de la fiche.");
                styliserDialog(alert, "rgba(231,76,60,0.35)");

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
            ? "-fx-font-size: 13px; -fx-font-weight: bold; -fx-cursor: hand;" +
              "-fx-background-color: rgba(231,76,60,0.30);" +
              "-fx-border-color: rgba(231,76,60,0.70);" +
              "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;" +
              "-fx-text-fill: #ffffff; -fx-font-family: Arial;" +
              "-fx-padding: 10;"
            : "-fx-font-size: 12px; -fx-font-weight: bold; -fx-cursor: hand;" +
              "-fx-background-color: rgba(231,76,60,0.10);" +
              "-fx-border-color: rgba(231,76,60,0.35);" +
              "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;" +
              "-fx-text-fill: #ff6b6b; -fx-font-family: Arial;" +
              "-fx-padding: 10;";
    }

    public void afficherCorbeille(boolean visible) {
        corbeilleLabel.setVisible(visible);
        corbeilleLabel.setManaged(visible);
        corbeilleLabel.toFront();
    }

    private void buildCards(FichePersonnage fiche, Personnage personnage) {
        Portrait portrait = fiche.getPortrait();
        if (portrait != null) {
            this.portraitRef = portrait;
            FicheCard card = buildPortraitCard(portrait, personnage);
            this.portraitCard = card;
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

    private FicheCard buildPortraitCard(Portrait portrait, Personnage personnage) {
        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(Double.MAX_VALUE);
        content.setMaxHeight(Double.MAX_VALUE);
        this.portraitContent = content;

        rebuildPortraitImage(content, portrait, personnage);

        Label nomLabel = new Label(personnage.getNom());
        nomLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial;" +
            "-fx-cursor: text;"
        );
        nomLabel.setWrapText(true);
        nomLabel.maxWidthProperty().bind(content.widthProperty());
        attachInlineEdit(nomLabel, newNom -> {
            personnageRef.setNom(newNom);
            if (titreLabel != null) titreLabel.setText(newNom + "  -  Fiche personnage");
            if (onEditNomPersonnage != null) onEditNomPersonnage.accept(newNom);
        });
        content.getChildren().add(nomLabel);

        return new FicheCard("Portrait", content, portrait.getId(), "portrait", CardColor.GOLD);
    }

    private void rebuildPortraitImage(VBox content, Portrait portrait, Personnage personnage) {
        Node first = content.getChildren().isEmpty() ? null : content.getChildren().get(0);
        boolean hasImageOrPlaceholder = first instanceof ImageView || first instanceof Label && content.getChildren().size() > 1;

        Node imageNode;
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
            iv.setStyle("-fx-cursor: hand;");
            attachPortraitEdit(iv);
            imageNode = iv;
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
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
            );
            attachPortraitEdit(init);
            imageNode = init;
        }

        if (hasImageOrPlaceholder) {
            content.getChildren().set(0, imageNode);
        } else {
            content.getChildren().add(0, imageNode);
        }
    }

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

            Label stars = new Label(String.valueOf(stat.getValeur()));
            stars.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #ffd700; -fx-font-family: Arial; -fx-cursor: hand;");

            content.widthProperty().addListener((obs, o, n) -> {
                double w = n.doubleValue();
                double starSize = clamp(12 + (w - 140) / 260.0 * 10, 12, 22);
                double nomSize  = clamp(8  + (w - 140) / 260.0 * 4,  8,  12);
                stars.setStyle(String.format(
                    "-fx-font-size: %.0fpx; -fx-font-weight: bold; -fx-text-fill: #ffd700; -fx-font-family: Arial; -fx-cursor: hand;", starSize));
                nom.setStyle(String.format(
                    "-fx-font-size: %.0fpx; -fx-font-weight: bold;" +
                    "-fx-text-fill: rgba(255,215,0,0.50);" +
                    "-fx-font-family: Arial; -fx-min-width: 70px;", nomSize));
            });

            row.getChildren().addAll(nom, spacer, stars);
            attachStatsEdit(row, stars, stat);

            Region rowSep = new Region();
            rowSep.setPrefHeight(1);
            rowSep.setMaxWidth(Double.MAX_VALUE);
            rowSep.setStyle("-fx-background-color: rgba(255,255,255,0.04);");

            content.getChildren().addAll(row, rowSep);
        }

        return new FicheCard("Statistiques", content, -1, "stats", CardColor.GOLD);
    }

    private FicheCard buildCompetenceCard(Competence c) {
        VBox content = new VBox(6);
        content.setMaxWidth(Double.MAX_VALUE);

        Label nomLabel = new Label(c.getNom());
        nomLabel.setWrapText(true);
        nomLabel.maxWidthProperty().bind(content.widthProperty());

        Label descLabel = new Label(c.getDescription() != null ? c.getDescription() : "");
        descLabel.setWrapText(true);
        descLabel.maxWidthProperty().bind(content.widthProperty());

        content.widthProperty().addListener((obs, o, n) -> {
            double w = n.doubleValue();
            double nomSize  = clamp(11 + (w - 140) / 260.0 * 4, 11, 16);
            double descSize = clamp(10 + (w - 140) / 260.0 * 3, 10, 14);
            nomLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial; -fx-cursor: text;", nomSize));
            descLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx;" +
                "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial; -fx-cursor: text;", descSize));
        });

        nomLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial; -fx-cursor: text;"
        );
        descLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial; -fx-cursor: text;"
        );

        attachInlineEdit(nomLabel, newVal -> {
            c.setNom(newVal);
            if (onEditCompetence != null) onEditCompetence.accept(c);
        });
        attachInlineEdit(descLabel, newVal -> {
            c.setDescription(newVal);
            if (onEditCompetence != null) onEditCompetence.accept(c);
        });

        content.getChildren().addAll(nomLabel, descLabel);
        return new FicheCard("Competence", content, c.getId(), "competence", CardColor.PURPLE);
    }

    private FicheCard buildEquipementCard(Equipement e) {
        VBox content = new VBox(6);
        content.setMaxWidth(Double.MAX_VALUE);

        Label nomLabel = new Label(e.getNom());
        nomLabel.setWrapText(true);
        nomLabel.maxWidthProperty().bind(content.widthProperty());

        Label descLabel = new Label(e.getDescription() != null ? e.getDescription() : "");
        descLabel.setWrapText(true);
        descLabel.maxWidthProperty().bind(content.widthProperty());

        content.widthProperty().addListener((obs, o, n) -> {
            double w = n.doubleValue();
            double nomSize  = clamp(11 + (w - 140) / 260.0 * 4, 11, 16);
            double descSize = clamp(10 + (w - 140) / 260.0 * 3, 10, 14);
            nomLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx; -fx-font-weight: bold;" +
                "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial; -fx-cursor: text;", nomSize));
            descLabel.setStyle(String.format(
                "-fx-font-size: %.0fpx;" +
                "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial; -fx-cursor: text;", descSize));
        });

        nomLabel.setStyle(
            "-fx-font-size: 13px; -fx-font-weight: bold;" +
            "-fx-text-fill: rgba(255,255,255,0.90); -fx-font-family: Arial; -fx-cursor: text;"
        );
        descLabel.setStyle(
            "-fx-font-size: 12px;" +
            "-fx-text-fill: rgba(255,255,255,0.45); -fx-font-family: Arial; -fx-cursor: text;"
        );

        attachInlineEdit(nomLabel, newVal -> {
            e.setNom(newVal);
            if (onEditEquipement != null) onEditEquipement.accept(e);
        });
        attachInlineEdit(descLabel, newVal -> {
            e.setDescription(newVal);
            if (onEditEquipement != null) onEditEquipement.accept(e);
        });

        content.getChildren().addAll(nomLabel, descLabel);
        return new FicheCard("Equipement", content, e.getId(), "equipement", CardColor.ORANGE);
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Édition inline
    // ─────────────────────────────────────────────────────────────────────

    private FicheCard findEnclosingCard(Node n) {
        Node cur = n;
        while (cur != null) {
            if (cur instanceof FicheCard fc) return fc;
            cur = cur.getParent();
        }
        return null;
    }

    private void attachInlineEdit(Label label, Consumer<String> onCommit) {
        label.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                startInlineEdit(label, onCommit);
                e.consume();
            }
        });
    }

    private void startInlineEdit(Label label, Consumer<String> onCommit) {
        if (!(label.getParent() instanceof Pane parent)) return;
        int index = parent.getChildren().indexOf(label);
        if (index < 0) return;

        FicheCard enclosing = findEnclosingCard(label);
        if (enclosing != null) enclosing.setEditing(true);

        String original = label.getText();
        TextField field = new TextField(original);
        field.setStyle(
            "-fx-background-color: rgba(255,255,255,0.08);" +
            "-fx-border-color: rgba(255,215,0,0.65);" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-padding: 4 8 4 8;" +
            "-fx-font-family: Arial;"
        );
        field.setMaxWidth(Double.MAX_VALUE);

        boolean[] resolving = { false };

        Runnable[] askRef = new Runnable[1];
        askRef[0] = () -> {
            if (resolving[0]) return;
            resolving[0] = true;
            String saisie = field.getText();
            if (saisie.equals(original)) {
                restoreLabel(parent, field, label, original);
                if (enclosing != null) enclosing.setEditing(false);
                resolving[0] = false;
                return;
            }
            DialogResult r = demanderConfirmation("la modification de cet element", original, saisie);
            switch (r) {
                case CONFIRMER -> {
                    label.setText(saisie);
                    restoreLabel(parent, field, label, saisie);
                    if (enclosing != null) enclosing.setEditing(false);
                    onCommit.accept(saisie);
                }
                case CONTINUER -> {
                    resolving[0] = false;
                    field.requestFocus();
                    field.positionCaret(field.getText().length());
                }
                case ANNULER -> {
                    restoreLabel(parent, field, label, original);
                    if (enclosing != null) enclosing.setEditing(false);
                }
            }
        };

        field.setOnAction(e -> askRef[0].run());
        field.focusedProperty().addListener((obs, was, now) -> {
            if (!now && !resolving[0]) askRef[0].run();
        });
        field.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ESCAPE) {
                resolving[0] = true;
                restoreLabel(parent, field, label, original);
                if (enclosing != null) enclosing.setEditing(false);
                ev.consume();
            }
        });
        field.addEventFilter(MouseEvent.MOUSE_PRESSED,  MouseEvent::consume);
        field.addEventFilter(MouseEvent.MOUSE_DRAGGED,  MouseEvent::consume);
        field.addEventFilter(MouseEvent.MOUSE_RELEASED, MouseEvent::consume);

        parent.getChildren().set(index, field);
        field.requestFocus();
        field.selectAll();
    }

    private void restoreLabel(Pane parent, Node currentEditor, Label label, String text) {
        int idx = parent.getChildren().indexOf(currentEditor);
        if (idx >= 0) {
            label.setText(text);
            parent.getChildren().set(idx, label);
        }
    }

    private void attachStatsEdit(HBox row, Label valueLabel, Stats stat) {
        valueLabel.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                startStatsEdit(row, valueLabel, stat);
                e.consume();
            }
        });
    }

    private void startStatsEdit(HBox row, Label valueLabel, Stats stat) {
        int index = row.getChildren().indexOf(valueLabel);
        if (index < 0) return;

        FicheCard enclosing = findEnclosingCard(valueLabel);
        if (enclosing != null) enclosing.setEditing(true);

        int original = stat.getValeur();
        Spinner<Integer> spinner = new Spinner<>(1, Integer.MAX_VALUE, Math.max(1, original));
        spinner.setEditable(true);
        spinner.setPrefWidth(90);
        spinner.setMaxWidth(90);

        boolean[] resolving = { false };

        Runnable[] askRef = new Runnable[1];
        askRef[0] = () -> {
            if (resolving[0]) return;
            resolving[0] = true;
            int saisie = spinner.getValue();
            if (saisie == original) {
                restoreStatsLabel(row, spinner, valueLabel, original);
                if (enclosing != null) enclosing.setEditing(false);
                resolving[0] = false;
                return;
            }
            DialogResult r = demanderConfirmation("la modification de cette statistique",
                    String.valueOf(original), String.valueOf(saisie));
            switch (r) {
                case CONFIRMER -> {
                    stat.setValeur(saisie);
                    restoreStatsLabel(row, spinner, valueLabel, saisie);
                    if (enclosing != null) enclosing.setEditing(false);
                    if (onEditStats != null) onEditStats.accept(stat);
                }
                case CONTINUER -> {
                    resolving[0] = false;
                    spinner.requestFocus();
                }
                case ANNULER -> {
                    restoreStatsLabel(row, spinner, valueLabel, original);
                    if (enclosing != null) enclosing.setEditing(false);
                }
            }
        };

        spinner.focusedProperty().addListener((obs, was, now) -> {
            if (!now && !resolving[0]) askRef[0].run();
        });
        spinner.getEditor().setOnAction(e -> askRef[0].run());
        spinner.addEventFilter(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ESCAPE) {
                resolving[0] = true;
                restoreStatsLabel(row, spinner, valueLabel, original);
                if (enclosing != null) enclosing.setEditing(false);
                ev.consume();
            } else if (ev.getCode() == KeyCode.ENTER) {
                askRef[0].run();
                ev.consume();
            }
        });
        spinner.addEventFilter(MouseEvent.MOUSE_PRESSED,  MouseEvent::consume);
        spinner.addEventFilter(MouseEvent.MOUSE_DRAGGED,  MouseEvent::consume);
        spinner.addEventFilter(MouseEvent.MOUSE_RELEASED, MouseEvent::consume);

        row.getChildren().set(index, spinner);
        spinner.requestFocus();
    }

    private void restoreStatsLabel(HBox row, Spinner<Integer> spinner, Label valueLabel, int newValue) {
        int idx = row.getChildren().indexOf(spinner);
        if (idx >= 0) {
            valueLabel.setText(String.valueOf(newValue));
            row.getChildren().set(idx, valueLabel);
        }
    }

    private void attachPortraitEdit(Node imageNode) {
        imageNode.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                startPortraitEdit(imageNode);
                e.consume();
            }
        });
    }

    private void startPortraitEdit(Node imageNode) {
        Window owner = imageNode.getScene() != null ? imageNode.getScene().getWindow() : null;

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choisir une nouvelle photo");
        chooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File initial = (portraitRef != null && portraitRef.getCheminImage() != null)
                ? new File(portraitRef.getCheminImage()) : null;
        if (initial != null && initial.exists() && initial.getParentFile() != null) {
            chooser.setInitialDirectory(initial.getParentFile());
        }

        File chosen = chooser.showOpenDialog(owner);
        if (chosen == null) return;

        String original = (portraitRef != null && portraitRef.getCheminImage() != null)
                ? portraitRef.getCheminImage() : "(aucune)";
        DialogResult r = demanderConfirmation("le changement de photo",
                original, chosen.getAbsolutePath());

        switch (r) {
            case CONFIRMER -> {
                if (portraitRef == null) {
                    Portrait p = new Portrait(chosen.getAbsolutePath());
                    p.setX(20);
                    p.setY(20);
                    p.setWidth(200);
                    p.setHeight(240);
                    if (onAddPortrait != null) onAddPortrait.accept(p);
                    portraitRef  = p;
                    FicheCard card = buildPortraitCard(p, personnageRef);
                    portraitCard = card;
                    placeCard(card, p.getX(), p.getY(), p.getWidth(), p.getHeight());
                    register(card);
                } else {
                    portraitRef.setCheminImage(chosen.getAbsolutePath());
                    rebuildPortraitImage(portraitContent, portraitRef, personnageRef);
                    if (onEditPortrait != null) onEditPortrait.accept(portraitRef);
                }
            }
            case CONTINUER -> startPortraitEdit(imageNode);
            case ANNULER   -> { /* rien a faire */ }
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    //  Dialogue de confirmation 3 boutons
    // ─────────────────────────────────────────────────────────────────────

    private enum DialogResult { CONFIRMER, CONTINUER, ANNULER }

    private DialogResult demanderConfirmation(String quoi, String avant, String apres) {
        ButtonType btnConfirmer = new ButtonType("Confirmer la modification", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnContinuer = new ButtonType("Continuer a modifier",      ButtonBar.ButtonData.OTHER);
        ButtonType btnAnnuler   = new ButtonType("Annuler la modification",   ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la modification");
        alert.setHeaderText("Confirmer " + quoi + " ?");

        String avantAffiche = tronquer(avant, 80);
        String apresAffiche = tronquer(apres, 80);
        alert.setContentText("Avant : " + avantAffiche + "\n\nApres : " + apresAffiche);

        alert.getButtonTypes().setAll(btnConfirmer, btnContinuer, btnAnnuler);
        styliserDialog(alert, "rgba(255,215,0,0.30)");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) return DialogResult.ANNULER;
        ButtonType bt = result.get();
        if (bt == btnConfirmer) return DialogResult.CONFIRMER;
        if (bt == btnContinuer) return DialogResult.CONTINUER;
        return DialogResult.ANNULER;
    }

    private String tronquer(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    private void styliserDialog(Alert alert, String borderColor) {
        alert.getDialogPane().setStyle(
            "-fx-background-color: #1e1e2f;" +
            "-fx-border-color: " + borderColor + ";" +
            "-fx-border-width: 1;"
        );
        alert.getDialogPane().lookupAll(".label").forEach(n ->
                n.setStyle("-fx-text-fill: #ffffff; -fx-font-family: Arial;"));
    }

    // ─────────────────────────────────────────────────────────────────────

    private double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public Parent          getRoot()               { return root; }
    public Button          getSauvegarderButton()  { return sauvegarderButton; }
    public Button          getRetourButton()       { return retourButton; }
    public Button          getSupprimerButton()    { return supprimerButton; }
    public List<FicheCard> getCards()              { return cards; }

    public void setOnSupprimerConfirme(Runnable r)           { this.onSupprimerConfirme = r; }
    public void setOnSupprimerCarte(Consumer<FicheCard> c)   { this.onSupprimerCarte = c; }

    public void setOnEditCompetence(Consumer<Competence> c)  { this.onEditCompetence = c; }
    public void setOnEditEquipement(Consumer<Equipement> c)  { this.onEditEquipement = c; }
    public void setOnEditStats(Consumer<Stats> c)            { this.onEditStats = c; }
    public void setOnEditNomPersonnage(Consumer<String> c)   { this.onEditNomPersonnage = c; }
    public void setOnEditPortrait(Consumer<Portrait> c)      { this.onEditPortrait = c; }
    public void setOnAddPortrait(Consumer<Portrait> c)       { this.onAddPortrait = c; }

    public enum CardColor { GOLD, PURPLE, ORANGE }

    public class FicheCard extends VBox {

        private final int       entityId;
        private final String    type;
        private final CardColor color;

        private double  pressSceneX, pressSceneY;
        private double  pressLayoutX, pressLayoutY;
        private boolean dragging = false;
        private double  pressW, pressH;
        private ResizeEdge edge = ResizeEdge.NONE;
        private boolean editing = false;

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

            Label dot = new Label(">");
            dot.setStyle("-fx-font-size: 9px; -fx-text-fill: " + accentColor() + "; -fx-font-family: Arial;");

            Label header = new Label(titleText.toUpperCase());
            header.setStyle(
                "-fx-font-size: 9px; -fx-font-weight: bold; -fx-font-family: Arial;" +
                "-fx-text-fill: rgba(255,255,255,0.35); -fx-cursor: move;"
            );

            Region headerSpacer = new Region();
            HBox.setHgrow(headerSpacer, Priority.ALWAYS);

            HBox headerRow;
            if ("competence".equals(type) || "equipement".equals(type)) {
                Label ctrlHint = new Label("Ctrl+drag pour supprimer");
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

            addEventFilter(MouseEvent.MOUSE_MOVED, e -> {
                if (editing) return;
                ResizeEdge detected = detectEdge(e.getX(), e.getY());
                setCursor(detected == ResizeEdge.NONE ? Cursor.DEFAULT : cursorFor(detected));
            });

            addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
                if (editing) return;
                if (e.getClickCount() >= 2) return;  // laisser passer le double-clic
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
                if (editing) return;
                if (e.getClickCount() >= 2) return;
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
                if (editing) return;
                if (e.getClickCount() >= 2) {
                    applyStyle(false);
                    return;
                }
                dragging = false;
                edge     = ResizeEdge.NONE;
                applyStyle(false);
                e.consume();
            });

            addEventFilter(MouseEvent.MOUSE_EXITED, e -> {
                if (editing) return;
                if (!dragging && edge == ResizeEdge.NONE) setCursor(Cursor.DEFAULT);
            });
        }

        public void setEditing(boolean editing) {
            this.editing = editing;
            if (editing) applyEditingStyle(); else applyStyle(false);
            toFront();
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

        private String editingBorderColor() {
            return switch (color) {
                case GOLD   -> "rgba(255,215,0,0.85)";
                case PURPLE -> "rgba(180,150,240,0.85)";
                case ORANGE -> "rgba(255,170,30,0.85)";
            };
        }

        private String editingShadowColor() {
            return switch (color) {
                case GOLD   -> "rgba(255,215,0,0.55)";
                case PURPLE -> "rgba(160,130,220,0.55)";
                case ORANGE -> "rgba(255,152,0,0.55)";
            };
        }

        private void applyStyle(boolean active) {
            setStyle(
                "-fx-background-color: rgba(10,9,20," + (active ? "0.95" : "0.85") + ");" +
                "-fx-border-color: " + borderColor(active) + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-translate-y: 0;" +
                (active
                    ? "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.60),14,0,0,3);"
                    : "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.40),8,0,0,2);")
            );
        }

        private void applyEditingStyle() {
            setStyle(
                "-fx-background-color: rgba(20,18,32,0.98);" +
                "-fx-border-color: " + editingBorderColor() + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 10;" +
                "-fx-background-radius: 10;" +
                "-fx-translate-y: -4;" +
                "-fx-effect: dropshadow(gaussian," + editingShadowColor() + ",28,0.45,0,8);"
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
