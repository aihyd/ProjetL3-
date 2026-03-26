package com.project.jdr.model;

public class Stats {

    private String nom;
    private int valeur;
    private int x;
    private int y;

    public Stats(String nom, int valeur) {
        this.nom = nom;
        this.valeur = valeur;
        this.x = 0;
        this.y = 0;
    }

    public Stats(String nom, int valeur, int x, int y) {
        this.nom = nom;
        this.valeur = valeur;
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}