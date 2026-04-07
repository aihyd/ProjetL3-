package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.EquipementDAO;
import com.project.jdr.dao.FichePersonnageDAO;
import com.project.jdr.model.Equipement;
import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.views.AddEquipementView;

import java.util.List;

public class AddEquipementController {

    public AddEquipementController(AddEquipementView view, AppTest app,
                                   Personnage personnage, int idUtilisateur, String username) {

        EquipementDAO    equipementDAO = new EquipementDAO();
        FichePersonnageDAO ficheDAO   = new FichePersonnageDAO();

        // ── Charger les équipements existants du compte ───────────────────
        List<Equipement> existants = equipementDAO.recupererEquipementsParUtilisateur(idUtilisateur);
        view.getEquipementExistantCombo().getItems().setAll(existants);

        // ── Sélection d'un équipement existant → préremplir le formulaire ─
        view.getEquipementExistantCombo().getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, old, selected) -> {
                    if (selected == null) return;
                    view.getNomField().setText(selected.getNom());
                    view.getDescriptionArea().setText(
                            selected.getDescription() != null ? selected.getDescription() : ""
                    );
                });

        // ── Retour au profil ──────────────────────────────────────────────
        view.getBackToProfile().setOnMouseClicked(e ->
                app.showProfile(idUtilisateur, username));

        // ── Confirmation ──────────────────────────────────────────────────
        view.getConfirmerButton().setOnAction(e -> {
            String nom         = view.getNomField().getText().trim();
            String description = view.getDescriptionArea().getText().trim();

            if (nom.isEmpty()) {
                afficherErreur(view, "Le nom de l'équipement est obligatoire.");
                return;
            }

            FichePersonnage fiche = personnage.getFiche();
            if (fiche == null || fiche.getId() == 0) {
                fiche = ficheDAO.recupererFicheParPersonnage(personnage.getId());
            }

            if (fiche == null) {
                afficherErreur(view, "Impossible de trouver la fiche du personnage.");
                return;
            }

            Equipement equipement = new Equipement(nom, description);
            equipement.setPosition(30, 30);
            equipement.setSize(200, 70);

            int resultat = equipementDAO.ajouterEquipement(equipement, fiche.getId());
            if (resultat == -1) {
                afficherErreur(view, "Erreur lors de l'ajout de l'équipement.");
                return;
            }

            afficherSucces(view, "Équipement ajouté avec succès !");

            view.getNomField().clear();
            view.getDescriptionArea().clear();
            view.getEquipementExistantCombo().getSelectionModel().clearSelection();

            app.showProfile(idUtilisateur, username);
        });
    }

    private void afficherErreur(AddEquipementView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-success");
        if (!view.getMessageLabel().getStyleClass().contains("message-label")) {
            view.getMessageLabel().getStyleClass().add("message-label");
        }
    }

    private void afficherSucces(AddEquipementView view, String message) {
        view.getMessageLabel().setText(message);
        view.getMessageLabel().getStyleClass().removeAll("message-label");
        view.getMessageLabel().setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 12px;");
    }
}