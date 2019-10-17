package com.example.projet_hiit.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Cycle implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "travail")
    private Travail travail;

    @ColumnInfo(name = "repos")
    private int repos;

    @ColumnInfo(name = "repetitions")
    private int repetitions;

    public Cycle(Travail travail, int repos, int repetitions){
        this.travail = travail;
        this.repos = repos;
        this.repetitions = repetitions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Travail getTravail() {
        return travail;
    }

    public void setTravail(Travail travail) {
        this.travail = travail;
    }

    public int getRepos() {
        return repos;
    }

    public void setRepos(int repos) {
        this.repos = repos;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }


    public int getTempsTotal(){
        return (getTravail().getDuree()+getRepos());
    }
}
