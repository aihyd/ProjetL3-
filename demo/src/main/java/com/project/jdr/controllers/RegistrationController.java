package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.views.RegistrationView;

public class RegistrationController {
    public RegistrationController(RegistrationView view, AppTest app) {
        view.getBackToLogin().setOnMouseClicked(e -> app.showLogin());
        view.getRegisterButton().setOnAction(e -> {
        });
    }
}
