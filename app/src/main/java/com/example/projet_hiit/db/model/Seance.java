package com.example.projet_hiit.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Seance implements Serializable, Parcelable {

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

    protected Seance(Parcel in) {
        id = in.readInt();
        preparation = in.readInt();
    }

    public static final Creator<Seance> CREATOR = new Creator<Seance>() {
        @Override
        public Seance createFromParcel(Parcel in) {
            return new Seance(in);
        }

        @Override
        public Seance[] newArray(int size) {
            return new Seance[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(preparation);
    }
}
