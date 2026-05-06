package com.project.jdr.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ChatbotService {

    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL = "llama-3.3-70b-versatile";

    public static String envoyerMessage(String message) {
        try {
            // 🔐 Récupération sécurisée de la clé API
            String apiKey = System.getenv("GROQ_API_KEY");
            if (apiKey == null || apiKey.isEmpty()) {
                return "Erreur : clé API manquante (variable GROQ_API_KEY).";
            }

            // 🧠 Création du JSON avec Jackson
            ObjectMapper mapper = new ObjectMapper();

            ObjectNode root = mapper.createObjectNode();
            root.put("model", MODEL);
            root.put("max_tokens", 1024);

            ArrayNode messages = mapper.createArrayNode();

            ObjectNode systemMsg = mapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content",
                "Tu es un assistant expert en jeux de role (JDR). " +
                "Tu aides les joueurs a creer des personnages, inventer des histoires, " +
                "des competences, des equipements et des aventures. " +
                "Reponds toujours en francais de maniere creative et immersive."
            );

            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", message);

            messages.add(systemMsg);
            messages.add(userMsg);

            root.set("messages", messages);

            String requestBody = mapper.writeValueAsString(root);

            // 🌐 Requête HTTP
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Groq Response: " + responseBody);

            // 📦 Parsing JSON propre
            JsonNode json = mapper.readTree(responseBody);
            JsonNode contentNode = json
                .path("choices")
                .path(0)
                .path("message")
                .path("content");

            if (contentNode.isMissingNode()) {
                return "Erreur : réponse inattendue.";
            }

            return contentNode.asText();

        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}