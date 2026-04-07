package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.CompetenceDAO;
import com.project.jdr.dao.FichePersonnageDAO;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.dao.PortraitDAO;
import com.project.jdr.dao.StatsDAO;
import com.project.jdr.model.Competence;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Portrait;
import com.project.jdr.model.Stats;
import com.project.jdr.views.CreateCharacterView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CreateCharacterController {

    private final PersonnageDAO personnageDAO;
    private final FichePersonnageDAO fichePersonnageDAO;
    private final StatsDAO statsDAO;
    private final CompetenceDAO competenceDAO;
    private final PortraitDAO portraitDAO;

    private final List<Competence> competencesAjoutees;

    public CreateCharacterController(CreateCharacterView view, AppTest app, int idUtilisateur, String username) {
        this.personnageDAO = new PersonnageDAO();
        this.fichePersonnageDAO = new FichePersonnageDAO();
        this.statsDAO = new StatsDAO();
        this.competenceDAO = new CompetenceDAO();
        this.portraitDAO = new PortraitDAO();
        this.competencesAjoutees = new ArrayList<>();

        view.getCancelButton().setOnAction(e -> app.showProfile(idUtilisateur, username));

        view.getAjouterCompetenceButton().setOnAction(e -> {
            String nomCompetence = view.getCompetenceNomField().getText().trim();
            String descriptionCompetence = view.getCompetenceDescArea().getText().trim();

            afficherMessageNeutre(view, "");

            if (nomCompetence.isEmpty() || descriptionCompetence.isEmpty()) {
                afficherErreur(view, "Veuillez remplir le nom et la description de la compétence.");
                return;
            }

            Competence competence = new Competence(nomCompetence, descriptionCompetence);
            competencesAjoutees.add(competence);

            ajouterCompetenceDansVue(view, competence);

            view.getCompetenceNomField().clear();
            view.getCompetenceDescArea().clear();

            afficherSucces(view, "Compétence ajoutée à la liste.");
        });

        view.getCreateButton().setOnAction(e -> {
            afficherMessageNeutre(view, "");

            String nom = view.getNom() != null ? view.getNom().trim() : "";
            String race = view.getRace() != null ? view.getRace().trim() : "";
            String classe = view.getClasse() != null ? view.getClasse().trim() : "";
            int niveau = view.getNiveau();
            String biographie = view.getBiographie() != null ? view.getBiographie().trim() : "";
            String cheminPhoto = view.getCheminPhoto();

            if (nom.isEmpty() || race.isEmpty() || classe.isEmpty()) {
                afficherErreur(view, "Veuillez remplir les informations de base du personnage.");
                return;
            }

            if (niveau < 1) {
                afficherErreur(view, "Le niveau doit être supérieur ou égal à 1.");
                return;
            }

            Personnage personnage = new Personnage(nom, race, classe, niveau);

            int idPersonnage = personnageDAO.ajouterPersonnage(personnage, idUtilisateur);
            if (idPersonnage == -1) {
                afficherErreur(view, "Erreur lors de la création du personnage.");
                return;
            }

            int idFiche = fichePersonnageDAO.creerFichePourPersonnage(idPersonnage);
            if (idFiche == -1) {
                afficherErreur(view, "Personnage créé, mais erreur lors de la création de la fiche.");
                return;
            }

            boolean bioOk = fichePersonnageDAO.mettreAJourBiographie(idFiche, biographie);
            if (!bioOk) {
                afficherErreur(view, "Erreur lors de l'enregistrement de la biographie.");
                return;
            }

            Stats force = new Stats("Force", view.getForce());
            force.setPosition(30, 30);
            force.setSize(140, 50);

            Stats agilite = new Stats("Agilité", view.getAgilite());
            agilite.setPosition(30, 100);
            agilite.setSize(140, 50);

            Stats intelligence = new Stats("Intelligence", view.getIntelligence());
            intelligence.setPosition(30, 170);
            intelligence.setSize(160, 50);

            Stats endurance = new Stats("Endurance", view.getEndurance());
            endurance.setPosition(30, 240);
            endurance.setSize(150, 50);

            if (statsDAO.ajouterStat(force, idFiche) == -1 ||
                statsDAO.ajouterStat(agilite, idFiche) == -1 ||
                statsDAO.ajouterStat(intelligence, idFiche) == -1 ||
                statsDAO.ajouterStat(endurance, idFiche) == -1) {
                afficherErreur(view, "Erreur lors de l'enregistrement des statistiques.");
                return;
            }

            int startX = 250;
            int startY = 30;
            int offsetY = 90;

            for (int i = 0; i < competencesAjoutees.size(); i++) {
                Competence competence = competencesAjoutees.get(i);
                competence.setPosition(startX, startY + i * offsetY);
                competence.setSize(240, 70);

                if (competenceDAO.ajouterCompetence(competence, idFiche) == -1) {
                    afficherErreur(view, "Erreur lors de l'enregistrement des compétences.");
                    return;
                }
            }

            if (cheminPhoto != null && !cheminPhoto.isBlank()) {
                Portrait portrait = new Portrait(cheminPhoto);
                portrait.setPosition(550, 30);
                portrait.setSize(180, 180);

                if (portraitDAO.ajouterPortrait(portrait, idFiche) == -1) {
                    afficherErreur(view, "Erreur lors de l'enregistrement du portrait.");
                    return;
                }
            }

            afficherSucces(view, "Personnage créé avec succès.");
            app.showProfile(idUtilisateur, username);
        });
    }

    private void ajouterCompetenceDansVue(CreateCharacterView view, Competence competence) {
        Label nomLabel = new Label(competence.getNom());
        nomLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label descriptionLabel = new Label(competence.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-text-fill: rgba(255,255,255,0.75); -fx-font-size: 12px;");

        VBox texteBox = new VBox(4, nomLabel, descriptionLabel);
        texteBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(texteBox, Priority.ALWAYS);

        Button supprimerButton = new Button("Supprimer");
        supprimerButton.getStyleClass().add("btn-secondary");
        supprimerButton.setStyle("-fx-text-fill: #ff6b6b;");

        HBox row = new HBox(12, texteBox, supprimerButton);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(8));
        row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.05);" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: rgba(255,255,255,0.07);" +
                "-fx-border-radius: 8;"
        );

        supprimerButton.setOnAction(e -> {
            competencesAjoutees.remove(competence);
            view.getCompetencesListBox().getChildren().remove(row);

            if (view.getCompetencesListBox().getChildren().isEmpty()) {
                Label emptyLabel = new Label("Aucune compétence ajoutée.");
                emptyLabel.getStyleClass().add("profile-empty-label");
                view.getCompetencesListBox().getChildren().add(emptyLabel);
            }
        });

        if (view.getCompetencesListBox().getChildren().size() == 1
                && view.getCompetencesListBox().getChildren().get(0) instanceof Label) {
            Label first = (Label) view.getCompetencesListBox().getChildren().get(0);
            if ("Aucune compétence ajoutée.".equals(first.getText())) {
                view.getCompetencesListBox().getChildren().clear();
            }
        }

        view.getCompetencesListBox().getChildren().add(row);
    }

    private void afficherErreur(CreateCharacterView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-success");
        if (!view.getMessageLabel().getStyleClass().contains("message-error")) {
            view.getMessageLabel().getStyleClass().add("message-error");
        }
    }

    private void afficherSucces(CreateCharacterView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-error");
        if (!view.getMessageLabel().getStyleClass().contains("message-success")) {
            view.getMessageLabel().getStyleClass().add("message-success");
        }
    }

    private void afficherMessageNeutre(CreateCharacterView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-error", "message-success");
    }
}