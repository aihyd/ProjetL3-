package com.project.jdr.model;

import java.util.ArrayList;
import java.util.List;

public class FichePersonnage {

    private String biographie;
    private List<Stats> stats;
    private List<Competence> competences;
    private List<Equipement> equipements;
    private Portrait portrait;

    public FichePersonnage() {
        this.stats = new ArrayList<>();
        this.competences = new ArrayList<>();
        this.equipements = new ArrayList<>();
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public void addStats(Stats stat) {
        stats.add(stat);
    }

    public void addCompetence(Competence competence) {
        competences.add(competence);
    }

    public void addEquipement(Equipement equipement) {
        equipements.add(equipement);
    }

    public void setPortrait(Portrait portrait) {
        this.portrait = portrait;
    }
}