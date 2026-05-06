package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.model.Personnage;
import com.project.jdr.services.AudioManager;
import com.project.jdr.views.ProfileView;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileController {

    private final ProfileView   view;
    private final PersonnageDAO personnageDAO = new PersonnageDAO();
    private final int           idUtilisateur;

    public ProfileController(ProfileView view, AppTest app, int idUtilisateur, String username) {
        this.view          = view;
        this.idUtilisateur = idUtilisateur;

        view.setUserId(idUtilisateur);
        view.setUsername(username);

        rafraichirPersonnages();

        view.getLogoutItem().setOnAction(e -> app.showLogin());

        view.getChangePasswordItem().setOnAction(e ->
                app.showChangePassword(idUtilisateur, username));

        view.getDeleteAccountItem().setOnAction(e ->
                app.showDeleteAccount(idUtilisateur, username));

        view.getAudioSettingsItem().setOnAction(e -> showAudioSettingsDialog());

        view.getCreateCharacterButton().setOnAction(e ->
                app.showCreateCharacter(idUtilisateur, username));

        view.getPersonnagesListView()
            .getSelectionModel()
            .selectedItemProperty()
            .addListener((obs, ancien, selectionne) -> {
                if (selectionne == null) return;

                view.getAfficherFicheButton().setOnAction(e ->
                        app.showFiche(selectionne, idUtilisateur, username));

                view.getAjouterEquipementButton().setOnAction(e ->
                        app.showAjouterEquipement(selectionne, idUtilisateur, username));
            });
                view.getChatbotButton().setOnAction(e ->
                                app.showChatbot(idUtilisateur, username));
    }

        private void showAudioSettingsDialog() {
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("Audio");
                if (view.getRoot().getScene() != null) {
                        stage.initOwner(view.getRoot().getScene().getWindow());
                }

                Label title = new Label("Parametres audio");
                title.getStyleClass().add("title-label");

                Label bgLabel = new Label("Fond");
                bgLabel.getStyleClass().add("auth-field-label");
                Label bgValue = new Label();
                bgValue.getStyleClass().add("profile-field-value");

                Slider bgSlider = new Slider(0, 100, AudioManager.getBackgroundVolume() * 100.0);
                bgSlider.getStyleClass().add("audio-slider");
                bgSlider.setPrefWidth(240);
                bgSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                        AudioManager.setBackgroundVolume(newVal.doubleValue() / 100.0);
                        bgValue.setText(String.format("%d%%", newVal.intValue()));
                });
                bgValue.setText(String.format("%d%%", (int) bgSlider.getValue()));

                Label clickLabel = new Label("Clic");
                clickLabel.getStyleClass().add("auth-field-label");
                Label clickValue = new Label();
                clickValue.getStyleClass().add("profile-field-value");

                Slider clickSlider = new Slider(0, 100, AudioManager.getClickVolume() * 100.0);
                clickSlider.getStyleClass().add("audio-slider");
                clickSlider.setPrefWidth(240);
                clickSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                        AudioManager.setClickVolume(newVal.doubleValue() / 100.0);
                        clickValue.setText(String.format("%d%%", newVal.intValue()));
                });
                clickValue.setText(String.format("%d%%", (int) clickSlider.getValue()));

                HBox bgRow = new HBox(12, bgLabel, bgSlider, bgValue);
                bgRow.setAlignment(Pos.CENTER_LEFT);
                HBox clickRow = new HBox(12, clickLabel, clickSlider, clickValue);
                clickRow.setAlignment(Pos.CENTER_LEFT);

                Button closeButton = new Button("Fermer");
                closeButton.getStyleClass().add("btn-secondary");
                closeButton.setOnAction(e -> stage.close());

                HBox actions = new HBox(closeButton);
                actions.setAlignment(Pos.CENTER_RIGHT);

                VBox card = new VBox(14, title, bgRow, clickRow, actions);
                card.getStyleClass().add("auth-card");
                card.setPadding(new Insets(18, 22, 18, 22));
                card.setMaxWidth(Double.MAX_VALUE);

                StackPane root = new StackPane(card);
                root.getStyleClass().add("auth-root");
                root.setPadding(new Insets(24));

                Scene scene = new Scene(root, 520, 280);
                scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
                AudioManager.applyToScene(scene);
                stage.setScene(scene);
                stage.showAndWait();
        }

    public void rafraichirPersonnages() {
        List<Personnage> personnages = personnageDAO
                .recupererPersonnagesParUtilisateur(idUtilisateur);
        view.getPersonnagesListView()
            .getItems()
            .setAll(personnages);
    }
}
