package com.project.jdr;

import com.project.jdr.database.InitialisationDb;
import com.project.jdr.model.Personnage;
import com.project.jdr.views.AddEquipementView;
import com.project.jdr.views.ChangePasswordView;
import com.project.jdr.views.ChatbotView;
import com.project.jdr.views.CreateCharacterView;
import com.project.jdr.views.DeleteAccountView;
import com.project.jdr.views.FichePersonnageView;
import com.project.jdr.views.ForgotPasswordView;
import com.project.jdr.controllers.AddEquipementController;
import com.project.jdr.controllers.ChangePasswordController;
import com.project.jdr.controllers.ChatbotController;
import com.project.jdr.controllers.CreateCharacterController;
import com.project.jdr.controllers.DeleteAccountController;
import com.project.jdr.controllers.FichePersonnageController;
import com.project.jdr.controllers.ForgotPasswordController;
import com.project.jdr.controllers.LoginController;
import com.project.jdr.controllers.ModifierPersonnageController;
import com.project.jdr.controllers.ProfileController;
import com.project.jdr.controllers.RegistrationController;
import com.project.jdr.views.LoginView;
import com.project.jdr.views.ModifierPersonnageView;
import com.project.jdr.views.ProfileView;
import com.project.jdr.views.RegistrationView;
import com.project.jdr.dao.PersonnageDAO;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppTest extends Application {

    private static final double WINDOW_WIDTH  = 1200;
    private static final double WINDOW_HEIGHT = 780;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        chargerEnv();
        InitialisationDb.initialiser();
        this.primaryStage = primaryStage;

        primaryStage.setTitle("JDP");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);

        showLogin();

        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void chargerEnv() {
        try {
            System.out.println("Dossier courant : " + System.getProperty("user.dir"));

            java.io.File file = new java.io.File(".env");
            System.out.println("Fichier .env existe : " + file.exists());
            System.out.println("Chemin absolu : " + file.getAbsolutePath());

            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("=")) {
                        String[] parts = line.split("=", 2);
                        System.setProperty(parts[0].trim(), parts[1].trim());
                        System.out.println("Charge : " + parts[0].trim());
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.err.println("Erreur chargement .env : " + e.getMessage());
        }
    }

    private void afficher(Parent root) {
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public void showLogin() {
        LoginView loginView = new LoginView();
        new LoginController(loginView, this);
        afficher(loginView.getRoot());
    }

    public void showProfile(int idUtilisateur, String username) {
        ProfileView profileView = new ProfileView();
        profileView.setUserId(idUtilisateur);
        profileView.setUsername(username);

        PersonnageDAO personnageDAO = new PersonnageDAO();
        profileView.getPersonnagesListView().getItems().setAll(
                personnageDAO.recupererPersonnagesParUtilisateur(idUtilisateur)
        );

        new ProfileController(profileView, this, idUtilisateur, username);
        afficher(profileView.getRoot());
    }

    public void showChangePassword(int idUtilisateur, String username) {
        ChangePasswordView view = new ChangePasswordView();
        new ChangePasswordController(view, this, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public void showAjouterEquipement(Personnage personnage, int idUtilisateur, String username) {
        AddEquipementView view = new AddEquipementView();
        new AddEquipementController(view, this, personnage, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public void showDeleteAccount(int idUtilisateur, String username) {
        DeleteAccountView view = new DeleteAccountView();
        new DeleteAccountController(view, this, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public void showModifierPersonnage(Personnage personnage, int idUtilisateur, String username) {
        ModifierPersonnageView view = new ModifierPersonnageView(personnage);
        new ModifierPersonnageController(view, this, personnage, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public void showFiche(Personnage personnage, int idUtilisateur, String username) {
        FichePersonnageView view = new FichePersonnageView(personnage);
        new FichePersonnageController(view, this, personnage, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public void showRegistration() {
        RegistrationView registrationView = new RegistrationView();
        new RegistrationController(registrationView, this);
        afficher(registrationView.getRoot());
    }

    public void showCreateCharacter(int idUtilisateur, String username) {
        CreateCharacterView view = new CreateCharacterView();
        new CreateCharacterController(view, this, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public void showForgotPassword() {
        ForgotPasswordView forgotPasswordView = new ForgotPasswordView();
        new ForgotPasswordController(forgotPasswordView, this);
        afficher(forgotPasswordView.getRoot());
    }

    public void showChatbot(int idUtilisateur, String username) {
        ChatbotView view = new ChatbotView();
        new ChatbotController(view, this, idUtilisateur, username);
        afficher(view.getRoot());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
