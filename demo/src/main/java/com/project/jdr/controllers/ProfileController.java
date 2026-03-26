package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.views.ProfileView;

public class ProfileController {

    public ProfileController(ProfileView view, AppTest app, int idUtilisateur, String username) {

        view.getLogoutItem().setOnAction(e -> app.showLogin());

        view.getChangePasswordItem().setOnAction(e -> {
            app.showChangePassword(idUtilisateur, username);
        });

        view.getDeleteAccountItem().setOnAction(e -> {
            app.showDeleteAccount(idUtilisateur, username);
        });

        view.getCreateCharacterButton().setOnAction(e -> {
            System.out.println("Créer un personnage");
        });
    }
}