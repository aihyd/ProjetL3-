package com.project.jdr.controllers;

import java.util.stream.Collectors;

import com.project.jdr.AppTest;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.model.Personnage;
import com.project.jdr.views.ProfileView;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileController {

    private final PersonnageDAO personnageDAO = new PersonnageDAO();
    private java.util.List<Personnage> currentPersonnages;

    public ProfileController(ProfileView view, AppTest app, int idUtilisateur, String username) {

        refreshList(view, idUtilisateur);

        view.getLogoutItem().setOnAction(e -> app.showLogin());

        view.getChangePasswordItem().setOnAction(e -> {
            app.showChangePassword(idUtilisateur, username);
        });

        view.getDeleteAccountItem().setOnAction(e -> {
            app.showDeleteAccount(idUtilisateur, username);
        });

        view.getCreateCharacterButton().setOnAction(e -> {
            app.showCreatePersonnage(idUtilisateur, username);
        });

        view.getPersonnagesListView().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int index = view.getPersonnagesListView().getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < currentPersonnages.size()) {
                    showPersonnagePopup(currentPersonnages.get(index));
                }
            }
        });
    }

    private void showPersonnagePopup(Personnage p) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Fiche de " + p.getNom());

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #1e1e2f; -fx-padding: 30; -fx-border-color: #ffd700; -fx-border-width: 1; -fx-border-radius: 15; -fx-background-radius: 15;");
        
        javafx.scene.image.ImageView portraitView = new javafx.scene.image.ImageView();
        try {
            String path = p.getFiche().getPortrait() != null ? p.getFiche().getPortrait().getCheminImage() : "";
            if (!path.isEmpty()) {
                portraitView.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(path)));
            } else {
                // Image par défaut si vide
                portraitView.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/default_avatar.png")));
            }
        } catch (Exception e) {
            System.out.println("Portrait non trouvé, utilisation d'un placeholder");
        }
        portraitView.setFitWidth(120);
        portraitView.setFitHeight(120);
        portraitView.setPreserveRatio(true);
        
        VBox portraitContainer = new VBox(portraitView);
        portraitContainer.setStyle("-fx-border-color: rgba(255,255,255,0.1); -fx-border-radius: 10; -fx-padding: 5;");
        portraitContainer.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(p.getNom());
        nameLabel.getStyleClass().add("title-label");
        
        Label subInfo = new Label(String.format("%s - %s (Niveau %d)", p.getRace(), p.getClasse(), p.getNiveau()));
        subInfo.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px; -fx-font-weight: bold;");

        VBox bioBox = new VBox(5);
        Label bioTitle = new Label("BIOGRAPHIE");
        bioTitle.setStyle("-fx-text-fill: rgba(255,215,0,0.6); -fx-font-size: 10px; -fx-font-weight: bold;");
        Label bioText = new Label(p.getFiche().getBiographie().isEmpty() ? "Aucune biographie rédigée." : p.getFiche().getBiographie());
        bioText.setWrapText(true);
        bioText.setMaxWidth(300);
        bioText.setStyle("-fx-text-fill: white; -fx-font-style: italic;");
        bioBox.getChildren().addAll(bioTitle, bioText);
        bioBox.setAlignment(Pos.CENTER);

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);
        statsRow.getChildren().addAll(
            createStatDisplay("FOR", "10"), // Valeurs à brancher sur le modèle Stats plus tard
            createStatDisplay("AGI", "10"),
            createStatDisplay("INT", "10")
        );

        Button closeButton = new Button("Fermer la fiche");
        closeButton.getStyleClass().add("btn-secondary");
        closeButton.setMinWidth(150);
        closeButton.setOnAction(e -> popupStage.close());

        layout.getChildren().addAll(portraitContainer, nameLabel, subInfo, new Region(), bioBox, statsRow, new Region(), closeButton);
        
        Scene scene = new Scene(layout, 400, 550);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private VBox createStatDisplay(String label, String value) {
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 10px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        VBox box = new VBox(2, l, v);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: rgba(255,255,255,0.05); -fx-padding: 10; -fx-background-radius: 8; -fx-min-width: 60;");
        return box;
    }

    private void refreshList(ProfileView view, int idUtilisateur) {
        currentPersonnages = personnageDAO.listerPersonnagesParUtilisateur(idUtilisateur);
        view.setPersonnages(
            currentPersonnages.stream()
                .map(p -> p.getNom() + " (Niv. " + p.getNiveau() + ")")
                .collect(Collectors.toList())
        );
    }
}