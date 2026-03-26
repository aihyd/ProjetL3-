package com.project.jdr.model;

public class Equipement {

    private String nom;
    private String description;
    private int x;
    private int y;

    public Equipement(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.x = 0;
        this.y = 0;
    }

    public Equipement(String nom, String description, int x, int y) {
        this.nom = nom;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description != null) {
            this.description = description;
        }
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

    @Override
    public String toString() {
        return "Equipement{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}