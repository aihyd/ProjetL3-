package com.project.jdr;

import com.project.jdr.database.InitialisationDb;
import com.project.jdr.controllers.ChatbotController;
import com.project.jdr.controllers.LoginController;
import com.project.jdr.controllers.RegistrationController;
import com.project.jdr.views.ChatbotView;
import com.project.jdr.views.LoginView;
import com.project.jdr.views.RegistrationView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppTest extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        chargerEnv();
        InitialisationDb.initialiser();
        this.primaryStage = primaryStage;
        showLogin();
    }

    private void chargerEnv() {
        try {
            java.io.File file = new java.io.File(".env");
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("=")) {
                        String[] parts = line.split("=", 2);
                        System.setProperty(parts[0].trim(), parts[1].trim());
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement .env : " + e.getMessage());
        }
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

    public void showChatbot(int idUtilisateur, String username) {
        ChatbotView view = new ChatbotView();
        new ChatbotController(view, this, idUtilisateur, username);
        Scene scene = new Scene(view.getRoot(), 900, 650);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}