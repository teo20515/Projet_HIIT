package com.example.projet_hiit.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.projet_hiit.db.Converters;

import java.io.Serializable;

@Entity
public class Sequence implements Parcelable {

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

    protected Sequence(Parcel in) {
        id = in.readInt();
        cycle = in.readParcelable(Cycle.class.getClassLoader());
        reposLong = in.readInt();
        repetitions = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeParcelable(cycle, flags);
        dest.writeInt(reposLong);
        dest.writeInt(repetitions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Sequence> CREATOR = new Creator<Sequence>() {
        @Override
        public Sequence createFromParcel(Parcel in) {
            return new Sequence(in);
        }

        @Override
        public Sequence[] newArray(int size) {
            return new Sequence[size];
        }
    };

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
