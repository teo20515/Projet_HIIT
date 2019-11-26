package com.example.projet_hiit.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Cycle implements Parcelable {

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

    protected Cycle(Parcel in) {
        id = in.readInt();
        travail = in.readParcelable(Travail.class.getClassLoader());
        repos = in.readInt();
        repetitions = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(travail, flags);
        dest.writeInt(repos);
        dest.writeInt(repetitions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cycle> CREATOR = new Creator<Cycle>() {
        @Override
        public Cycle createFromParcel(Parcel in) {
            return new Cycle(in);
        }

        @Override
        public Cycle[] newArray(int size) {
            return new Cycle[size];
        }
    };

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
