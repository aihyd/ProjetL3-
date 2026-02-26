package com.project.jdr.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView {

    private VBox root;
    private TextField Username;
    private PasswordField passwordField;
    private Hyperlink forgotPasswordLink ;
    private Button btnConnexion;
    private Button btnInscription;
    private Label message;

    public LoginView() {

        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

        Label titre = new Label("JDP Login");
        titre.getStyleClass().add("title-label");

         this.Username = new TextField();
        Username.setPromptText("User Name");
        Username.getStyleClass().add("User Name");

        this.passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("Password");

        
        this.forgotPasswordLink = new Hyperlink("Forgot your password?");
        forgotPasswordLink.getStyleClass().add("forgot-password-link");
        forgotPasswordLink.setOnAction(e -> {
            System.out.println("Rediriger vers la récupération de mot de passe...");
         
        });

        this.btnConnexion = new Button("Login");
        btnConnexion.getStyleClass().add("btn-primary");

        this.btnInscription = new Button("Register");
        btnInscription.getStyleClass().add("btn-secondary");

        this.message = new Label();
        message.getStyleClass().add("message-label");

        root.getChildren().addAll(
            titre,
            Username,
            passwordField,
            forgotPasswordLink, 
            btnConnexion,
            btnInscription,
            message
        );
    }
public TextField getUsername() { return Username; }
public PasswordField getPasswordField() { return passwordField; }
public Hyperlink getForgotPasswordLink() { return forgotPasswordLink; }
public Button getBtnConnexion() { return btnConnexion; }
public Button getBtnInscription() { return btnInscription; }
public Label getMessage() { return message; }



    public Parent getRoot() {
        return root;
    }
}