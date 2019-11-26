package com.example.projet_hiit.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Travail implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "duree")
    private int duree;

    @ColumnInfo(name = "nom")
    private String nom;

    public Travail(String nom, int duree){
        this.duree = duree;
        this.nom = nom;
    }

    protected Travail(Parcel in) {
        id = in.readInt();
        duree = in.readInt();
        nom = in.readString();
    }

    public static final Creator<Travail> CREATOR = new Creator<Travail>() {
        @Override
        public Travail createFromParcel(Parcel in) {
            return new Travail(in);
        }

        @Override
        public Travail[] newArray(int size) {
            return new Travail[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(duree);
        parcel.writeString(nom);
    }
}
