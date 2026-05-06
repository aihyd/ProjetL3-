package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.CompetenceDAO;
import com.project.jdr.dao.EquipementDAO;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.dao.PortraitDAO;
import com.project.jdr.dao.StatsDAO;
import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.model.Stats;
import com.project.jdr.views.FichePersonnageView;
import com.project.jdr.views.FichePersonnageView.FicheCard;

import javafx.application.Platform;
import javafx.scene.input.MouseEvent;

public class FichePersonnageController {

    private final FichePersonnageView view;
    private final Personnage          personnage;

    private final StatsDAO      statsDAO      = new StatsDAO();
    private final CompetenceDAO competenceDAO = new CompetenceDAO();
    private final EquipementDAO equipementDAO = new EquipementDAO();
    private final PortraitDAO   portraitDAO   = new PortraitDAO();
    private final PersonnageDAO personnageDAO = new PersonnageDAO();

    public FichePersonnageController(FichePersonnageView view, AppTest app,
                                     Personnage personnage, int idUtilisateur, String username) {
        this.view       = view;
        this.personnage = personnage;

        view.getRetourButton().setOnAction(e ->
                app.showProfile(idUtilisateur, username));

        view.getModifierButton().setOnAction(e ->
                app.showModifierPersonnage(personnage, idUtilisateur, username));

        view.setOnSupprimerConfirme(() -> {
            personnageDAO.supprimerPersonnage(personnage.getId());
            app.showProfile(idUtilisateur, username);
        });

        view.getSauvegarderButton().setOnAction(e -> sauvegarder());

        for (FicheCard card : view.getCards()) {
            card.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> sauvegarder());
        }

        // ── Suppression par drag vers la corbeille ────────────────────────
        view.setOnSupprimerCarte(card -> {
            boolean ok = switch (card.getType()) {
                case "competence" -> competenceDAO.supprimerCompetence(card.getEntityId());
                case "equipement" -> equipementDAO.supprimerEquipement(card.getEntityId());
                default           -> false;
            };
            if (ok) {
                view.supprimerCarteVisuellement(card);
            }
        });

        // ── Edition inline (double-clic sur les éléments) ─────────────────
        view.setOnEditCompetence(c ->
                competenceDAO.mettreAJourNomEtDescription(c.getId(), c.getNom(), c.getDescription()));
        view.setOnEditEquipement(eq ->
                equipementDAO.mettreAJourNomEtDescription(eq.getId(), eq.getNom(), eq.getDescription()));
        view.setOnEditStats(s ->
                statsDAO.mettreAJourValeur(s.getId(), s.getValeur()));
        view.setOnEditNomPersonnage(newNom ->
                personnageDAO.mettreAJourPersonnage(personnage));
    }

    private void sauvegarder() {
        FichePersonnage fiche = personnage.getFiche();
        if (fiche == null) return;

        for (FicheCard card : view.getCards()) {
            int    id = card.getEntityId();
            int    x  = (int) card.getLayoutX();
            int    y  = (int) card.getLayoutY();
            double w  = card.getPrefWidth();
            double h  = card.getPrefHeight();

            switch (card.getType()) {
                case "portrait"   -> portraitDAO.mettreAJourPositionEtTaille(id, x, y, w, h);
                case "competence" -> competenceDAO.mettreAJourPositionEtTaille(id, x, y, w, h);
                case "equipement" -> equipementDAO.mettreAJourPositionEtTaille(id, x, y, w, h);
                case "stats"      -> {
                    for (Stats stat : fiche.getStats()) {
                        statsDAO.mettreAJourPositionEtTaille(stat.getId(), x, y, w, h);
                    }
                }
            }
        }

        Platform.runLater(() -> {
            view.getSauvegarderButton().setText("✓ Sauvegarde");
            view.getSauvegarderButton().setStyle(
                "-fx-background-color: linear-gradient(to bottom,#2ecc71,#27ae60);" +
                "-fx-text-fill:white;-fx-font-weight:bold;" +
                "-fx-background-radius:10;-fx-border-radius:10;-fx-padding:11 24 11 24;"
            );
            new Thread(() -> {
                try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
                Platform.runLater(() -> {
                    view.getSauvegarderButton().setText("Sauvegarder");
                    view.getSauvegarderButton().setStyle("");
                    view.getSauvegarderButton().getStyleClass().setAll("btn-primary");
                });
            }).start();
        });
    }
}