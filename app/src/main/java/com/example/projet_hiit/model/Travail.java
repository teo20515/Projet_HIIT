package com.example.projet_hiit.model;

public class Travail {
    private int duree;
    private String nom;

    public Travail(int duree, String nom){
        this.duree = duree;
        this.nom = nom;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
