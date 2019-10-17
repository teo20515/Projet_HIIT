package com.example.projet_hiit.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.projet_hiit.db.Converters;

import java.io.Serializable;

@Entity
public class Sequence implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "cycle")
    @TypeConverters(Converters.class)
    private Cycle cycle;

    @ColumnInfo(name = "reposLong")
    private int reposLong;

    @ColumnInfo(name = "repetitions")
    private int repetitions;

    public Sequence(Cycle cycle, int reposLong, int repetitions) {
        this.cycle = cycle;
        this.reposLong = reposLong;
        this.repetitions = repetitions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public void setCycle(Cycle cycle) {
        this.cycle = cycle;
    }

    public int getReposLong() {
        return reposLong;
    }

    public void setReposLong(int reposLong) {
        this.reposLong = reposLong;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getTempsTotal(){
        return (getCycle().getTempsTotal()+getReposLong());
    }
}
