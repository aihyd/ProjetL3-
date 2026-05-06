package com.project.jdr.controllers;

import java.util.ArrayList;
import java.util.List;

import com.project.jdr.AppTest;
import com.project.jdr.dao.ChatMessageDAO;
import com.project.jdr.dao.CompetenceDAO;
import com.project.jdr.dao.EquipementDAO;
import com.project.jdr.dao.FichePersonnageDAO;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.dao.StatsDAO;
import com.project.jdr.model.Competence;
import com.project.jdr.model.Equipement;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Stats;
import com.project.jdr.services.ChatbotService;
import com.project.jdr.views.ChatbotView;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class ChatbotController {

    private final int              idUtilisateur;
    private final String           username;
    private       String           contexteUtilisateur;

    private final PersonnageDAO      personnageDAO = new PersonnageDAO();
    private final FichePersonnageDAO ficheDAO      = new FichePersonnageDAO();
    private final CompetenceDAO      competenceDAO = new CompetenceDAO();
    private final EquipementDAO      equipementDAO = new EquipementDAO();
    private final StatsDAO           statsDAO      = new StatsDAO();

    public ChatbotController(ChatbotView view, AppTest app,
                             int idUtilisateur, String username) {
        this.idUtilisateur = idUtilisateur;
        this.username      = username;
        this.contexteUtilisateur = construireContexte();

        view.getBackButton().setOnAction(e -> app.showProfile(idUtilisateur, username));

        // Charger les anciens messages
        List<String[]> ancienMessages = ChatMessageDAO.recupererMessages(idUtilisateur);
        if (ancienMessages.isEmpty()) {
            String bienvenue = "Bienvenue " + username + " ! Je suis ton assistant JDR. " +
                    "Je peux creer des personnages complets pour toi ! " +
                    "Dis-moi par exemple : 'Cree-moi un elfe mage' ou " +
                    "'Cree un guerrier orc puissant'.";
            view.addBotMessage(bienvenue);
            ChatMessageDAO.sauvegarderMessage(idUtilisateur, "assistant", bienvenue);
        } else {
            for (String[] msg : ancienMessages) {
                if ("user".equals(msg[0])) {
                    view.addUserMessage(msg[1]);
                } else {
                    view.addBotMessage(msg[1]);
                }
            }
        }

        view.getSendButton().setOnAction(e -> envoyerMessage(view));
        view.getInputField().setOnAction(e -> envoyerMessage(view));
    }

    private String construireContexte() {
        StringBuilder contexte = new StringBuilder();
        contexte.append("Utilisateur : ").append(username).append("\n\n");

        try {
            List<Personnage> personnages = personnageDAO
                    .recupererPersonnagesParUtilisateur(idUtilisateur);

            if (personnages.isEmpty()) {
                contexte.append("L'utilisateur n'a pas encore de personnages.\n");
            } else {
                contexte.append("Personnages existants :\n");
                for (Personnage p : personnages) {
                    contexte.append("- ").append(p.getNom())
                            .append(" (").append(p.getRace())
                            .append(" ").append(p.getClasse())
                            .append(" Niv.").append(p.getNiveau())
                            .append(")\n");
                    if (p.getFiche() != null) {
                        if (!p.getFiche().getStats().isEmpty()) {
                            for (Stats s : p.getFiche().getStats()) {
                                contexte.append("  ").append(s.getNom())
                                        .append(": ").append(s.getValeur())
                                        .append("/5\n");
                            }
                        }
                        if (!p.getFiche().getCompetences().isEmpty()) {
                            contexte.append("  Competences: ");
                            for (Competence c : p.getFiche().getCompetences()) {
                                contexte.append(c.getNom()).append(", ");
                            }
                            contexte.append("\n");
                        }
                        if (!p.getFiche().getEquipements().isEmpty()) {
                            contexte.append("  Equipements: ");
                            for (Equipement e : p.getFiche().getEquipements()) {
                                contexte.append(e.getNom()).append(", ");
                            }
                            contexte.append("\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            contexte.append("Impossible de charger les personnages.\n");
            System.err.println("Erreur contexte : " + e.getMessage());
        }

        return contexte.toString();
    }

    private void envoyerMessage(ChatbotView view) {
        String message = view.getInputField().getText().trim();
        if (message.isEmpty()) return;

        view.addUserMessage(message);
        view.getInputField().clear();
        view.addLoadingMessage();

        ChatMessageDAO.sauvegarderMessage(idUtilisateur, "user", message);

        String contexte = this.contexteUtilisateur;

        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return ChatbotService.envoyerMessage(message, contexte);
            }
        };

        task.setOnSucceeded(e -> Platform.runLater(() -> {
            view.removeLoadingMessage();
            String reponse = task.getValue();
            view.addBotMessage(reponse);
            ChatMessageDAO.sauvegarderMessage(idUtilisateur, "assistant", reponse);

            // Si la réponse contient un personnage → le sauvegarder en DB
            if (reponse.contains("PERSONNAGE:")) {
                sauvegarderPersonnageDepuisReponse(reponse);
                contexteUtilisateur = construireContexte();
                view.addBotMessage("Ton personnage a ete sauvegarde dans ton profil !");
                ChatMessageDAO.sauvegarderMessage(idUtilisateur, "assistant",
                        "Ton personnage a ete sauvegarde dans ton profil !");
            }
        }));

        task.setOnFailed(e -> Platform.runLater(() -> {
            view.removeLoadingMessage();
            view.addBotMessage("Une erreur s'est produite. Reessaie !");
        }));

        new Thread(task).start();
    }

    private void sauvegarderPersonnageDepuisReponse(String reponse) {
        try {
            String[] lignes = reponse.split("\n");

            String nom          = "";
            String race         = "Humain";
            String classe       = "Guerrier";
            int    niveau       = 1;
            String biographie   = "";
            int    force        = 3;
            int    agilite      = 3;
            int    intelligence = 3;
            int    endurance    = 3;

            List<String[]> competences = new ArrayList<>();
            List<String[]> equipements = new ArrayList<>();

            for (String ligne : lignes) {
                ligne = ligne.trim();
                if (ligne.startsWith("Nom:"))
                    nom = ligne.replace("Nom:", "").trim();
                else if (ligne.startsWith("Race:"))
                    race = ligne.replace("Race:", "").trim();
                else if (ligne.startsWith("Classe:"))
                    classe = ligne.replace("Classe:", "").trim();
                else if (ligne.startsWith("Niveau:")) {
                    try { niveau = Integer.parseInt(
                            ligne.replace("Niveau:", "").trim()); }
                    catch (Exception ignored) {}
                }
                else if (ligne.startsWith("Biographie:"))
                    biographie = ligne.replace("Biographie:", "").trim();
                else if (ligne.startsWith("Force:")) {
                    try { force = Integer.parseInt(
                            ligne.replace("Force:", "").trim()); }
                    catch (Exception ignored) {}
                }
                else if (ligne.startsWith("Agilite:")) {
                    try { agilite = Integer.parseInt(
                            ligne.replace("Agilite:", "").trim()); }
                    catch (Exception ignored) {}
                }
                else if (ligne.startsWith("Intelligence:")) {
                    try { intelligence = Integer.parseInt(
                            ligne.replace("Intelligence:", "").trim()); }
                    catch (Exception ignored) {}
                }
                else if (ligne.startsWith("Endurance:")) {
                    try { endurance = Integer.parseInt(
                            ligne.replace("Endurance:", "").trim()); }
                    catch (Exception ignored) {}
                }
                else if (ligne.matches("Competence\\d+:.*")) {
                    String val = ligne.replaceAll("Competence\\d+:", "").trim();
                    String[] parts = val.split("\\|");
                    if (parts.length >= 2)
                        competences.add(new String[]{parts[0].trim(), parts[1].trim()});
                    else if (parts.length == 1)
                        competences.add(new String[]{parts[0].trim(), ""});
                }
                else if (ligne.matches("Equipement\\d+:.*")) {
                    String val = ligne.replaceAll("Equipement\\d+:", "").trim();
                    String[] parts = val.split("\\|");
                    if (parts.length >= 2)
                        equipements.add(new String[]{parts[0].trim(), parts[1].trim()});
                    else if (parts.length == 1)
                        equipements.add(new String[]{parts[0].trim(), ""});
                }
            }

            if (nom.isEmpty()) {
                System.err.println("Nom du personnage non trouve dans la reponse.");
                return;
            }

            // 1. Créer le personnage
            Personnage p = new Personnage(nom, race, classe, niveau);
            int idPersonnage = personnageDAO.ajouterPersonnage(p, idUtilisateur);
            if (idPersonnage == -1) {
                System.err.println("Erreur creation personnage en DB.");
                return;
            }

            // 2. Créer la fiche
            int idFiche = ficheDAO.creerFichePourPersonnage(idPersonnage);
            if (idFiche == -1) {
                System.err.println("Erreur creation fiche en DB.");
                return;
            }

            // 3. Mettre à jour la biographie
            if (!biographie.isEmpty()) {
                ficheDAO.mettreAJourBiographie(idFiche, biographie);
            }

            // 4. Ajouter les stats
            statsDAO.ajouterStat(new Stats("Force",        force),        idFiche);
            statsDAO.ajouterStat(new Stats("Agilite",      agilite),      idFiche);
            statsDAO.ajouterStat(new Stats("Intelligence", intelligence), idFiche);
            statsDAO.ajouterStat(new Stats("Endurance",    endurance),    idFiche);

            // 5. Ajouter les compétences
            for (String[] c : competences) {
                competenceDAO.ajouterCompetence(
                        new Competence(c[0], c[1]), idFiche);
            }

            // 6. Ajouter les équipements
            for (String[] eq : equipements) {
                equipementDAO.ajouterEquipement(
                        new Equipement(eq[0], eq[1]), idFiche);
            }

            System.out.println("Personnage cree avec succes : " + nom);

        } catch (Exception e) {
            System.err.println("Erreur sauvegarde personnage : " + e.getMessage());
        }
    }
}