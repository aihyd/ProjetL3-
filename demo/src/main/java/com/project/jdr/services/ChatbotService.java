package com.project.jdr.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ChatbotService {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

private static final String MODEL = "llama-3.3-70b-versatile";
    public static String envoyerMessage(String message) {
        try {
            String body = "{"
                + "\"model\": \"" + MODEL + "\","
                + "\"messages\": ["
                + "{\"role\": \"system\", \"content\": \"Tu es un assistant expert en jeux de role (JDR). Tu aides les joueurs a creer des personnages, inventer des histoires, des competences, des equipements et des aventures. Reponds toujours en francais de maniere creative et immersive.\"},"
                + "{\"role\": \"user\", \"content\": \""
                + message.replace("\\", "\\\\")
                         .replace("\"", "\\\"")
                         .replace("\n", "\\n")
                         .replace("\r", "")
                + "\"}"
                + "],"
                + "\"max_tokens\": 1024"
                + "}";

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println("Groq Response: " + responseBody);

            // Extraire le texte de la reponse JSON Groq
            // Format: {"choices":[{"message":{"content":"..."}}]}
            int start = responseBody.indexOf("\"content\":\"") + 11;
            if (start < 11) return "Erreur : reponse inattendue.";
            int end = responseBody.indexOf("\"}", start);
            if (end < 0) return "Erreur : impossible de parser la reponse.";

            return responseBody.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\'", "'");

        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}