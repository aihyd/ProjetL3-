package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.views.ForgotPasswordView;

public class ForgotPasswordController {

    public ForgotPasswordController(ForgotPasswordView view, AppTest app) {

        view.getVerifyButton().setOnAction(e -> {
            String username = view.getUsernameField().getText().trim();
            if (username.isEmpty()) {
                view.getMessageLabel().setText("Please enter a username!");
                return;
            }
            if (username.equalsIgnoreCase("user1")) {
                view.getSecretQuestionLabel().setText("What is your pet's name?");
                view.getSecretQuestionLabel().setVisible(true);
                view.getSecretAnswerField().setVisible(true);
                view.getNewPasswordField().setVisible(true);
                view.getResetButton().setVisible(true);
                view.getMessageLabel().setText("");
            } else {
                view.getMessageLabel().setText("Username not found!");
            }
        });

        view.getResetButton().setOnAction(e -> {
            String answer = view.getSecretAnswerField().getText().trim();
            String newPassword = view.getNewPasswordField().getText().trim();
            if (answer.isEmpty() || newPassword.isEmpty()) {
                view.getMessageLabel().setText("Fill all fields!");
                return;
            }
            if (answer.equalsIgnoreCase("fluffy")) {
                view.getMessageLabel().setText("Password successfully updated!");
            } else {
                view.getMessageLabel().setText("Incorrect answer to secret question!");
            }
        });
    }
}