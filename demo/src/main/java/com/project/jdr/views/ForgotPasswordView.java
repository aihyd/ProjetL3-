package com.project.jdr.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ForgotPasswordView {

    private VBox root;

    public ForgotPasswordView() {
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);

      
        Label title = new Label("Forgot Password");
        title.getStyleClass().add("title-label");

       
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("text-field");

       
        Button verifyButton = new Button("Verify");
        verifyButton.getStyleClass().add("btn-primary");

        
        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

        Label secretQuestionLabel = new Label("Your secret question will appear here");
        secretQuestionLabel.getStyleClass().add("secret-question-label");
        secretQuestionLabel.setVisible(false);

      
        TextField secretAnswerField = new TextField();
        secretAnswerField.setPromptText("Answer to secret question");
        secretAnswerField.getStyleClass().add("text-field");
        secretAnswerField.setVisible(false);

      
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("New password");
        newPasswordField.getStyleClass().add("text-field");
        newPasswordField.setVisible(false);

       
        Button resetButton = new Button("Reset Password");
        resetButton.getStyleClass().add("btn-primary");
        resetButton.setVisible(false);

        verifyButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                messageLabel.setText("Please enter a username!");
                return;
            }

            if (username.equalsIgnoreCase("user1")) {
                secretQuestionLabel.setText("What is your pet's name?");
                secretQuestionLabel.setVisible(true);
                secretAnswerField.setVisible(true);
                newPasswordField.setVisible(true);
                resetButton.setVisible(true);
                messageLabel.setText("");
            } else {
                messageLabel.setText("Username not found!");
            }
        });

       
        resetButton.setOnAction(e -> {
            String answer = secretAnswerField.getText().trim();
            String newPassword = newPasswordField.getText().trim();

            if (answer.isEmpty() || newPassword.isEmpty()) {
                messageLabel.setText("Fill all fields!");
                return;
            }

       
            if (answer.equalsIgnoreCase("fluffy")) {
                messageLabel.setText("Password successfully updated!");
            } else {
                messageLabel.setText("Incorrect answer to secret question!");
            }
        });

        
        root.getChildren().addAll(
                title,
                usernameField,
                verifyButton,
                messageLabel,
                secretQuestionLabel,
                secretAnswerField,
                newPasswordField,
                resetButton
        );
    }

    public Parent getRoot() {
        return root;
    }
}