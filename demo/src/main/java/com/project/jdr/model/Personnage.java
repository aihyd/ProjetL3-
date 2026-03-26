package com.project.jdr.model;

public class Personnage {

    private int id;
    private String nom;
    private String race;
    private String classe;
    private int niveau;
    private FichePersonnage fiche;

    public Personnage(String nom, String race, String classe, int niveau) {
        this(0, nom, race, classe, niveau, null);
    }

    public Personnage(int id, String nom, String race, String classe, int niveau, FichePersonnage fiche) {
        this.id = id;
        this.nom = nom;
        this.race = race;
        this.classe = classe;
        this.niveau = niveau;
        this.fiche = (fiche != null) ? fiche : new FichePersonnage();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 0) {
            this.id = id;
        }
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        if (nom != null && !nom.trim().isEmpty()) {
            this.nom = nom;
        }
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        if (race != null && !race.trim().isEmpty()) {
            this.race = race;
        }
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        if (classe != null && !classe.trim().isEmpty()) {
            this.classe = classe;
        }
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        if (niveau >= 0) {
            this.niveau = niveau;
        }
    }

    public void monterNiveau() {
        this.niveau++;
    }

    public FichePersonnage getFiche() {
        return fiche;
    }

    public void setFiche(FichePersonnage fiche) {
        if (fiche != null) {
            this.fiche = fiche;
        }
    }
}