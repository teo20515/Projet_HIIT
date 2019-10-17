package com.example.projet_hiit.db.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Seance implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "sequence")
    private Sequence sequence;

    @ColumnInfo(name = "preparation")
    private int preparation;



    public Seance(Sequence sequence, int preparation) {
        this.sequence = sequence;
        this.preparation = preparation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Sequence getSequence() { return sequence; }

    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }

    public int getPreparation() {
        return preparation;
    }

    public void setPreparation(int preparation) {
        this.preparation = preparation;
    }

    public int getTempsTotal(){
        return (getSequence().getTempsTotal()+getPreparation());
    }
}
