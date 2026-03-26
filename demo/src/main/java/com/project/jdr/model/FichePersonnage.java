package com.project.jdr.model;

import java.util.ArrayList;
import java.util.List;

public class FichePersonnage {

    private int id;
    private String biographie;
    private List<Stats> stats;
    private List<Competence> competences;
    private List<Equipement> equipements;
    private Portrait portrait;

    public FichePersonnage() {
        this.id = 0;
        this.biographie = "";
        this.stats = new ArrayList<>();
        this.competences = new ArrayList<>();
        this.equipements = new ArrayList<>();
        this.portrait = null;
    }

    public FichePersonnage(int id, String biographie) {
        this();
        this.id = id;
        this.biographie = biographie != null ? biographie : "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id >= 0) {
            this.id = id;
        }
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        if (biographie != null) {
            this.biographie = biographie;
        }
    }

    public List<Stats> getStats() {
        return stats;
    }

    public List<Competence> getCompetences() {
        return competences;
    }

    public List<Equipement> getEquipements() {
        return equipements;
    }

    public Portrait getPortrait() {
        return portrait;
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
    }

    public void addStats(Stats stat) {
        if (stat != null) {
            stats.add(stat);
        }
    }

    public void addCompetence(Competence competence) {
        if (competence != null) {
            competences.add(competence);
        }
    }

    public void addEquipement(Equipement equipement) {
        if (equipement != null) {
            equipements.add(equipement);
        }
    }
}