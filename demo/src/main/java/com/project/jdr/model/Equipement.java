package com.project.jdr.model;

public class Equipement {

    private String nom;
    private String description;

    public Equipement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription(){
        return description;
    }
}