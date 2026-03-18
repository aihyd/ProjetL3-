package com.project.jdr;
<<<<<<< Updated upstream

import com.project.jdr.views.LoginView;
=======
import com.project.jdr.controllers.ChangePasswordController;
import com.project.jdr.controllers.DeleteAccountController;
import com.project.jdr.controllers.ForgotPasswordController;
import com.project.jdr.controllers.LoginController;
import com.project.jdr.controllers.PersonnageController;
import com.project.jdr.controllers.ProfileController;
import com.project.jdr.controllers.RegistrationController;
import com.project.jdr.database.InitialisationDb;
import com.project.jdr.views.ChangePasswordView;
import com.project.jdr.views.DeleteAccountView;
import com.project.jdr.views.ForgotPasswordView;
import com.project.jdr.views.LoginView;
import com.project.jdr.views.PersonnageView;
import com.project.jdr.views.ProfileView;
import com.project.jdr.views.RegistrationView;
>>>>>>> Stashed changes

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginView loginView = new LoginView();
        Scene scene = new Scene(loginView.getRoot(), 400, 500);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
         primaryStage.setScene(scene);
       primaryStage.show();
    }

public void showCreatePersonnage(int idUtilisateur, String username) {
    PersonnageView view = new PersonnageView();
    new PersonnageController(view, this, idUtilisateur, username);

    Scene scene = new Scene(view.getRoot(), 400, 500);
    scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}