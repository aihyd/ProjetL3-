package com.project.jdr;

import com.project.jdr.views.ForgotPasswordView;
import com.project.jdr.controllers.ForgotPasswordController;
import com.project.jdr.controllers.LoginController;
import com.project.jdr.views.LoginView;
import com.project.jdr.views.RegistrationView;
import com.project.jdr.controllers.RegistrationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppTest extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLogin();
    }

    public void showLogin() {
        LoginView loginView = new LoginView();
        new LoginController(loginView, this);
        Scene scene = new Scene(loginView.getRoot(), 400, 500);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showRegistration() {
        RegistrationView registrationView = new RegistrationView();
        new RegistrationController(registrationView, this);
        Scene scene = new Scene(registrationView.getRoot(), 400, 500);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public void showForgotPassword() {
    ForgotPasswordView forgotPasswordView = new ForgotPasswordView();
    new ForgotPasswordController(forgotPasswordView, this);
    Scene scene = new Scene(forgotPasswordView.getRoot(), 400, 500);
    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
    primaryStage.setScene(scene);
}

    public static void main(String[] args) {
        launch(args);
    }
}