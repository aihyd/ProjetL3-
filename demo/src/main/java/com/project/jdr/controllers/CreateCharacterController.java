package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.database.PersonnageDAO;
import com.project.jdr.views.CreateCharacterView;

public class CreateCharacterController {

    public CreateCharacterController(CreateCharacterView view, AppTest app,
                                      int idUtilisateur, String username) {

        // Annuler → retour au profil
        view.getCancelButton().setOnAction(e -> {
            app.showProfile(idUtilisateur, username);
        });

        // Créer le personnage
        view.getCreateButton().setOnAction(e -> {
            String nom         = view.getNom();
            String race        = view.getRace();
            String classe      = view.getClasse();
            int niveau         = view.getNiveau();
            String biographie  = view.getBiographie();
            int force          = view.getForce();
            int agilite        = view.getAgilite();
            int intelligence   = view.getIntelligence();
            int endurance      = view.getEndurance();

            // Vérifications
            if (nom == null || nom.trim().isEmpty()) {
                view.getMessageLabel().setText("Le nom est obligatoire !");
                return;
            }
            if (race == null) {
                view.getMessageLabel().setText("Choisis une race !");
                return;
            }
            if (classe == null) {
                view.getMessageLabel().setText("Choisis une classe !");
                return;
            }

            boolean succes = PersonnageDAO.creerPersonnage(
                nom, race, classe, niveau, biographie,
                force, agilite, intelligence, endurance,
                idUtilisateur
            );

            if (succes) {
                app.showProfile(idUtilisateur, username);
            } else {
                view.getMessageLabel().setStyle("-fx-text-fill: red;");
                view.getMessageLabel().setText("Erreur lors de la création !");
            }
        });
    }
}