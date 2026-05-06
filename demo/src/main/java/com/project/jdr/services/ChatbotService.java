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
    private static final String MODEL   = "llama-3.3-70b-versatile";

    public static String envoyerMessage(String message, String contexte) {
        try {
            String apiKey = System.getProperty("GROQ_API_KEY", System.getenv("GROQ_API_KEY"));
            if (apiKey == null || apiKey.isEmpty()) {
                return "Erreur : cle API manquante. Verifie le fichier .env";
            }

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("model", MODEL);
            root.put("max_tokens", 2048);

            ArrayNode messages = mapper.createArrayNode();

            ObjectNode systemMsg = mapper.createObjectNode();
            systemMsg.put("role", "system");
            systemMsg.put("content",
                "Tu es un assistant expert en jeux de role (JDR). " +
                "Tu es creatif, immersif et tres utile. " +
                "Reponds toujours en francais.\n\n" +

                "IMPORTANT : Quand l'utilisateur te demande de creer un personnage, " +
                "tu DOIS obligatoirement repondre en utilisant EXACTEMENT ce format, " +
                "sans aucune modification :\n\n" +

                "PERSONNAGE:\n" +
                "Nom: [nom du personnage]\n" +
                "Race: [Humain ou Elfe ou Nain ou Orc ou Halfelin]\n" +
                "Classe: [Guerrier ou Mage ou Rodeur ou Paladin ou Voleur ou Druide]\n" +
                "Niveau: [nombre entre 1 et 20]\n" +
                "Biographie: [histoire du personnage en 2-3 phrases]\n" +
                "Force: [nombre entre 1 et 5]\n" +
                "Agilite: [nombre entre 1 et 5]\n" +
                "Intelligence: [nombre entre 1 et 5]\n" +
                "Endurance: [nombre entre 1 et 5]\n" +
                "Competence1: [nom de la competence] | [description courte]\n" +
                "Competence2: [nom de la competence] | [description courte]\n" +
                "Competence3: [nom de la competence] | [description courte]\n" +
                "Equipement1: [nom de l'equipement] | [description courte]\n" +
                "Equipement2: [nom de l'equipement] | [description courte]\n\n" +

                "Apres ce bloc PERSONNAGE, ajoute une description creative du personnage.\n\n" +

                "Pour toute autre question, reponds normalement de facon creative.\n\n" +

                "Contexte de l'utilisateur :\n" + contexte
            );

            ObjectNode userMsg = mapper.createObjectNode();
            userMsg.put("role", "user");
            userMsg.put("content", message);

            messages.add(systemMsg);
            messages.add(userMsg);
            root.set("messages", messages);

            String requestBody = mapper.writeValueAsString(root);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Groq Response: " + response.body());

            JsonNode json = mapper.readTree(response.body());
            JsonNode contentNode = json
                .path("choices")
                .path(0)
                .path("message")
                .path("content");

            if (contentNode.isMissingNode()) {
                return "Erreur : reponse inattendue.";
            }

            return contentNode.asText();

        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }
}