package com.project.jdr.model;

public class Stats extends ElementFiche {

    private String nom;
    private int valeur;

    public Stats(String nom, int valeur) {
        super();
        this.nom = nom;
        this.valeur = valeur;
    }

    public Stats(int id, String nom, int valeur, int x, int y, double width, double height) {
        super(id, x, y, width, height);
        this.nom = nom;
        this.valeur = valeur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom != null && !nom.trim().isEmpty()) {
            this.nom = nom;
        }
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
}