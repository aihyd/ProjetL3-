package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.model.Personnage;
import com.project.jdr.views.ProfileView;

import java.util.List;

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

    public void rafraichirPersonnages() {
        List<Personnage> personnages = personnageDAO
                .recupererPersonnagesParUtilisateur(idUtilisateur);
        view.getPersonnagesListView()
            .getItems()
            .setAll(personnages);
    }
}
