package com.project.jdr.model;

public class Personnage {

    private String nom;
    private String race;
    private String classe;
    private int niveau;
    private FichePersonnage fiche;

    public Personnage(String nom, String race, String classe, int niveau) {
        this.nom = nom;
        this.race = race;
        this.classe = classe;
        this.niveau = niveau;
        this.fiche = new FichePersonnage();
    }

    public String getNom() {
        return nom;
    }

    public FichePersonnage getFiche() {
        return fiche;
    }
}