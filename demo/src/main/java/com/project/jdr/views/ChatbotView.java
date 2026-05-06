package com.project.jdr.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ChatbotView {

    private final VBox      root;
    private final VBox      messagesBox;
    private final TextField inputField;
    private final Button    sendButton;
    private final Button    backButton;
    private final ScrollPane scrollPane;

    public ChatbotView() {
        root = new VBox(12);
        root.getStyleClass().add("auth-root");
        root.setPadding(new Insets(30, 40, 30, 40));
        root.setAlignment(Pos.TOP_CENTER);

        // ── Bouton retour + titre ──────────────────────────────────────
        backButton = new Button("<- Retour");
        backButton.getStyleClass().add("btn-secondary");

        Label title = new Label("Assistant JDR");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(title, Priority.ALWAYS);

        HBox topBar = new HBox(12, backButton, title);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setMaxWidth(900);

        // ── Zone des messages ──────────────────────────────────────────
        messagesBox = new VBox(10);
        messagesBox.setPadding(new Insets(12));
        messagesBox.setMaxWidth(Double.MAX_VALUE);

        scrollPane = new ScrollPane(messagesBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: rgba(255,255,255,0.03);" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-border-radius: 12;" +
            "-fx-background-radius: 12;"
        );
        scrollPane.setPrefHeight(450);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // ── Zone de saisie ─────────────────────────────────────────────
        inputField = new TextField();
        inputField.setPromptText("Pose ta question sur le JDR...");
        inputField.getStyleClass().add("register-field");
        inputField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        sendButton = new Button("Envoyer");
        sendButton.getStyleClass().add("btn-primary");

        HBox inputRow = new HBox(10);
        inputRow.setAlignment(Pos.CENTER);
        inputRow.setMaxWidth(900);
        inputRow.getChildren().addAll(inputField, sendButton);

        VBox contentWrapper = new VBox(12);
        contentWrapper.setMaxWidth(900);
        contentWrapper.setPrefWidth(Double.MAX_VALUE);
        contentWrapper.getChildren().addAll(topBar, scrollPane, inputRow);

        root.getChildren().add(contentWrapper);
    }

    public void addUserMessage(String text) {
        Label msg = new Label("Vous :  " + text);
        msg.setWrapText(true);
        msg.setMaxWidth(700);
        msg.setStyle(
            "-fx-background-color: rgba(255,152,0,0.15);" +
            "-fx-border-color: rgba(255,152,0,0.30);" +
            "-fx-border-radius: 10; -fx-background-radius: 10;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-text-fill: #ffffff; -fx-font-size: 13px;" +
            "-fx-font-family: Arial;"
        );
        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_RIGHT);
        messagesBox.getChildren().add(row);
        scrollToBottom();
    }

    public void addBotMessage(String text) {
        Label msg = new Label("Assistant :  " + text);
        msg.setWrapText(true);
        msg.setMaxWidth(700);
        msg.setStyle(
            "-fx-background-color: rgba(255,255,255,0.06);" +
            "-fx-border-color: rgba(255,255,255,0.10);" +
            "-fx-border-radius: 10; -fx-background-radius: 10;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-text-fill: #ffffff; -fx-font-size: 13px;" +
            "-fx-font-family: Arial;"
        );
        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_LEFT);
        messagesBox.getChildren().add(row);
        scrollToBottom();
    }

    public void addLoadingMessage() {
        Label msg = new Label("Assistant :  ...");
        msg.setStyle(
            "-fx-background-color: rgba(255,255,255,0.04);" +
            "-fx-border-radius: 10; -fx-background-radius: 10;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-text-fill: rgba(255,255,255,0.40); -fx-font-size: 13px;" +
            "-fx-font-family: Arial;"
        );
        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setId("loading");
        messagesBox.getChildren().add(row);
        scrollToBottom();
    }

    public void removeLoadingMessage() {
        messagesBox.getChildren().removeIf(node -> "loading".equals(node.getId()));
    }

    private void scrollToBottom() {
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }

    public Parent      getRoot()       { return root; }
    public TextField   getInputField() { return inputField; }
    public Button      getSendButton() { return sendButton; }
    public Button      getBackButton() { return backButton; }
}