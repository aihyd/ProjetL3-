package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.views.LoginView;

public class LoginController {

    public LoginController(LoginView view, AppTest app) {
        view.getBtnInscription().setOnAction(e -> app.showRegistration());
        view.getBtnConnexion().setOnAction(e -> {
           
        });
    }
}