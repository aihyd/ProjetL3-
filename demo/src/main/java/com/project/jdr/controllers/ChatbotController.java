package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.services.ChatbotService;
import com.project.jdr.views.ChatbotView;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class ChatbotController {

   public ChatbotController(ChatbotView view, AppTest app, int idUtilisateur, String username) {
        view.getBackButton().setOnAction(e -> app.showProfile(idUtilisateur, username));
        // Message de bienvenue
        view.addBotMessage("Bienvenue aventurier ! Je suis ton assistant JDR. Pose-moi tes questions sur la création de personnages, les compétences, les histoires...");

        // Envoyer avec le bouton
        view.getSendButton().setOnAction(e -> envoyerMessage(view));

        // Envoyer avec la touche Entrée
        view.getInputField().setOnAction(e -> envoyerMessage(view));
    }

    private void envoyerMessage(ChatbotView view) {
        String message = view.getInputField().getText().trim();
        if (message.isEmpty()) return;

        view.addUserMessage(message);
        view.getInputField().clear();
        view.addLoadingMessage();

        // Appel API dans un thread séparé pour ne pas bloquer l'interface
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return ChatbotService.envoyerMessage(message);
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            view.removeLoadingMessage();
            view.addBotMessage(task.getValue());
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            view.removeLoadingMessage();
            view.addBotMessage("Une erreur s'est produite. Réessaie !");
        }));

        new Thread(task).start();
    }
}