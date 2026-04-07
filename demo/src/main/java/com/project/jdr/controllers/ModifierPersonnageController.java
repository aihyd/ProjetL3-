package com.project.jdr.controllers;

import com.project.jdr.AppTest;
import com.project.jdr.dao.CompetenceDAO;
import com.project.jdr.dao.PortraitDAO;
import com.project.jdr.dao.EquipementDAO;
import com.project.jdr.dao.FichePersonnageDAO;
import com.project.jdr.dao.PersonnageDAO;
import com.project.jdr.dao.StatsDAO;
import com.project.jdr.model.Competence;
import com.project.jdr.model.Portrait;
import com.project.jdr.model.Equipement;
import com.project.jdr.model.FichePersonnage;
import com.project.jdr.model.Personnage;
import com.project.jdr.views.ModifierPersonnageView;
import com.project.jdr.views.ModifierPersonnageView.CompetenceRow;
import com.project.jdr.views.ModifierPersonnageView.EquipementRow;
import com.project.jdr.views.ModifierPersonnageView.StatRow;

import javafx.application.Platform;

public class ModifierPersonnageController {

    public ModifierPersonnageController(ModifierPersonnageView view, AppTest app,
                                        Personnage personnage, int idUtilisateur, String username) {

        PersonnageDAO      personnageDAO  = new PersonnageDAO();
        FichePersonnageDAO ficheDAO       = new FichePersonnageDAO();
        StatsDAO           statsDAO       = new StatsDAO();
        CompetenceDAO      competenceDAO  = new CompetenceDAO();
        EquipementDAO      equipementDAO  = new EquipementDAO();
        PortraitDAO        portraitDAO    = new PortraitDAO();

        view.getRetourLabel().setOnMouseClicked(e ->
                app.showFiche(personnage, idUtilisateur, username));

        // Ajouter une ligne compétence vide
        view.getAjouterCompetenceButton().setOnAction(e ->
                view.ajouterLigneCompetence());

        // Ajouter une ligne équipement vide
        view.getAjouterEquipementButton().setOnAction(e ->
                view.ajouterLigneEquipement());

        view.getConfirmerButton().setOnAction(e -> {

            String nom    = view.getNomField().getText().trim();
            String race   = view.getRaceCombo().getValue();
            String classe = view.getClasseCombo().getValue();
            int    niveau = view.getNiveauSpinner().getValue();

            if (nom.isEmpty() || race == null || classe == null) {
                view.getMessageLabel().setStyle("");
                view.getMessageLabel().getStyleClass().setAll("message-label");
                view.getMessageLabel().setText("Veuillez remplir le nom, la race et la classe.");
                return;
            }

            // ── Mettre à jour le personnage ───────────────────────────────
            personnage.setNom(nom);
            personnage.setRace(race);
            personnage.setClasse(classe);
            personnage.setNiveau(niveau);
            boolean ok = personnageDAO.mettreAJourPersonnage(personnage);
            if (!ok) {
                erreur(view, "Erreur mise a jour du personnage.");
                return;
            }

            // ── Récupère la fiche (créée si nécessaire) ───────────────────
            FichePersonnage fiche = personnage.getFiche();
            if (fiche == null) {
                int idFiche = ficheDAO.creerFichePourPersonnage(personnage.getId());
                if (idFiche == -1) { erreur(view, "Erreur creation de la fiche."); return; }
                fiche = new FichePersonnage(idFiche, "");
                personnage.setFiche(fiche);
            }
            int idFiche = fiche.getId();

            // ── Biographie ────────────────────────────────────────────────
            ficheDAO.mettreAJourBiographie(idFiche, view.getBiographieArea().getText().trim());

            // ── Stats ─────────────────────────────────────────────────────
            for (StatRow row : view.getStatRows()) {
                statsDAO.mettreAJourValeur(row.id, row.getValeur());
            }

            // ── Compétences ───────────────────────────────────────────────
            for (CompetenceRow row : view.getCompetenceRows()) {
                if (row.isSupprime()) {
                    if (row.id > 0) competenceDAO.supprimerCompetence(row.id);
                } else if (row.id > 0) {
                    competenceDAO.mettreAJourNomEtDescription(row.id, row.getNom(), row.getDescription());
                } else if (!row.getNom().isEmpty()) {
                    Competence c = new Competence(row.getNom(), row.getDescription());
                    c.setPosition(30, 30);
                    c.setSize(220, 80);
                    int newId = competenceDAO.ajouterCompetence(c, idFiche);
                    row.id = newId;
                }
            }

            // ── Équipements ───────────────────────────────────────────────
            for (EquipementRow row : view.getEquipementRows()) {
                if (row.isSupprime()) {
                    if (row.id > 0) equipementDAO.supprimerEquipement(row.id);
                } else if (row.id > 0) {
                    equipementDAO.mettreAJourNomEtDescription(row.id, row.getNom(), row.getDescription());
                } else if (!row.getNom().isEmpty()) {
                    Equipement eq = new Equipement(row.getNom(), row.getDescription());
                    eq.setPosition(30, 30);
                    eq.setSize(220, 80);
                    int newId = equipementDAO.ajouterEquipement(eq, idFiche);
                    row.id = newId;
                }
            }

            succes(view, "Modifications enregistrees !");
            new Thread(() -> {
                try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                Platform.runLater(() -> app.showFiche(personnage, idUtilisateur, username));
            }).start();
        });
    }

    private void erreur(ModifierPersonnageView view, String msg) {
        view.getMessageLabel().setStyle("");
        view.getMessageLabel().getStyleClass().setAll("message-label");
        view.getMessageLabel().setText(msg);
    }

    private void succes(ModifierPersonnageView view, String msg) {
        view.getMessageLabel().setStyle(
            "-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 12px;"
        );
        view.getMessageLabel().setText(msg);
    }
}