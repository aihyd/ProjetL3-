package com.project.jdr.model;
import java.util.ArrayList;
import java.util.List;
public class Utilisateur {

    private String nomUtilisateur;
    private String motDePasseHash;
    private List<Personnage> personnages;

    public Utilisateur(String nomUtilisateur, String motDePasseHash) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasseHash = motDePasseHash;
        this.personnages = new ArrayList<>();
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void addPersonnage(Personnage personnage) {
        personnages.add(personnage);
    }

    public List<Personnage> getPersonnages() {
        return personnages;
    }
}