package com.project.jdr.model;

import java.util.ArrayList;
import java.util.List;

public class Utilisateur {

    private String nomUtilisateur;
    private String motDePasseHash;
    private String questionSecrete;
    private String reponseSecreteHash;
    private List<Personnage> personnages;

    public Utilisateur(String nomUtilisateur, String motDePasseHash,
                       String questionSecrete, String reponseSecreteHash) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasseHash = motDePasseHash;
        this.questionSecrete = questionSecrete;
        this.reponseSecreteHash = reponseSecreteHash;
        this.personnages = new ArrayList<>();
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        if (nomUtilisateur != null && !nomUtilisateur.trim().isEmpty()) {
            this.nomUtilisateur = nomUtilisateur;
        }
    }

    public String getMotDePasseHash() {
        return motDePasseHash;
    }

    public void setMotDePasseHash(String motDePasseHash) {
        if (motDePasseHash != null && !motDePasseHash.trim().isEmpty()) {
            this.motDePasseHash = motDePasseHash;
        }
    }

    public String getQuestionSecrete() {
        return questionSecrete;
    }

    public void setQuestionSecrete(String questionSecrete) {
        if (questionSecrete != null && !questionSecrete.trim().isEmpty()) {
            this.questionSecrete = questionSecrete;
        }
    }

    public String getReponseSecreteHash() {
        return reponseSecreteHash;
    }

    public void setReponseSecreteHash(String reponseSecreteHash) {
        if (reponseSecreteHash != null && !reponseSecreteHash.trim().isEmpty()) {
            this.reponseSecreteHash = reponseSecreteHash;
        }
    }

    public void addPersonnage(Personnage personnage) {
        if (personnage != null) {
            personnages.add(personnage);
        }
    }

    public boolean removePersonnage(String nom) {
        Personnage personnage = rechercherPersonnageParNom(nom);
        if (personnage != null) {
            return personnages.remove(personnage);
        }
        return false;
    }

    public Personnage rechercherPersonnageParNom(String nom) {
        for (Personnage personnage : personnages) {
            if (personnage.getNom().equalsIgnoreCase(nom)) {
                return personnage;
            }
        }
        return null;
    }

    public boolean possedePersonnage(String nom) {
        return rechercherPersonnageParNom(nom) != null;
    }

    public List<Personnage> getPersonnages() {
        return personnages;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "nomUtilisateur='" + nomUtilisateur + '\'' +
                ", questionSecrete='" + questionSecrete + '\'' +
                ", nombreDePersonnages=" + personnages.size() +
                '}';
    }
}