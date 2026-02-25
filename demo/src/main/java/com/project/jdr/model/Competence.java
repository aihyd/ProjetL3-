package com.project.jdr.model;

public class Competence {

    private String nom;
    private String description;

    public Competence(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }
}