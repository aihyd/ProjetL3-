package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.model.Personnage;
import com.project.jdr.views.PersonnageView;

import javafx.scene.paint.Color;

public class PersonnageController {

    private final PersonnageView view;
    private final AppTest app;
    private final int idUtilisateur;
    private final String username;
    private final PersonnageDAO personnageDAO = new PersonnageDAO();

    public PersonnageController(PersonnageView view, AppTest app, int idUtilisateur, String username) {
        this.view = view;
        this.app = app;
        this.idUtilisateur = idUtilisateur;
        this.username = username;

        this.view.getBtnCreer().setOnAction(e -> handleCreer());
        this.view.getBtnRetour().setOnAction(e -> app.showProfile(idUtilisateur, username));
    }

    private void handleCreer() {
        String nom = view.getNom().getText().trim();
        String race = view.getRaceField().getText().trim();
        String classe = view.getClasseField().getText().trim();

        if (nom.isEmpty()) {
            view.getMessage().setText("Le nom ne peut pas être vide");
            view.getMessage().setTextFill(Color.RED);
            return;
        }

        Personnage newPerso = new Personnage(nom, race, classe, 1);
        
        newPerso.getFiche().setBiographie(view.getBiographieArea().getText());
        
        boolean success = personnageDAO.ajouterPersonnage(newPerso, idUtilisateur);

        if (success) {
            view.getMessage().setText("Personnage créé avec succès !");
            view.getMessage().setTextFill(Color.GREEN);
            app.showProfile(idUtilisateur, username);
        } else {
            view.getMessage().setText("Erreur lors de la création en base");
            view.getMessage().setTextFill(Color.RED);
        }
    }
}
