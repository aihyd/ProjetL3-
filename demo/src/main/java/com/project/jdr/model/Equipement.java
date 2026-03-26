package com.project.jdr.model;

public class Equipement extends ElementFiche {

    private String nom;
    private String description;

    public Equipement(String nom, String description) {
        super();
        this.nom = nom;
        this.description = description;
    }

    public Equipement(int id, String nom, String description, int x, int y, double width, double height) {
        super(id, x, y, width, height);
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom != null && !nom.trim().isEmpty()) {
            this.nom = nom;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
    }
}