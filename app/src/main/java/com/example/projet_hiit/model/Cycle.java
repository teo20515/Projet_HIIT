package com.example.projet_hiit.model;

public class Cycle {

    private Travail travail;
    private Repos repos;
    private int repetitions;

    public Cycle(Travail travail, Repos repos, int repetitions){
        this.travail = travail;
        this.repos = repos;
        this.repetitions = repetitions;
    }

    public Travail getTravail() {
        return travail;
    }

    public void setTravail(Travail travail) {
        this.travail = travail;
    }

    public Repos getRepos() {
        return repos;
    }

    public void setRepos(Repos repos) {
        this.repos = repos;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }
}
