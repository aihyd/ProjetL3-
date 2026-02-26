package com.project.jdr;

import com.project.jdr.views.ForgotPasswordView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        ForgotPasswordView forgotPasswordView=new ForgotPasswordView();
      
        Scene scene = new Scene(forgotPasswordView.getRoot(), 400, 500);
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
         primaryStage.setScene(scene);
       primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}