package com.example.projet_hiit.db;

import androidx.room.TypeConverter;

import com.example.projet_hiit.db.model.Cycle;
import com.example.projet_hiit.db.model.Seance;
import com.example.projet_hiit.db.model.Sequence;
import com.example.projet_hiit.db.model.Travail;
import com.google.gson.Gson;

//Permet de convertir les objets en chaine de caractère (Json) afin de pouvoir les sauvegarder en base de données
public class Converters {

    private static Gson gson = new Gson();

    ///////Travail converters///////
    @TypeConverter
    public String travailToString(Travail travail){
        return gson.toJson(travail);
    }

    @TypeConverter
    public Travail stringToTravail(String value){
        return gson.fromJson(value,Travail.class);
    }


    ///////Cycle converters///////
    @TypeConverter
    public String cycleToString(Cycle cycle){
        return gson.toJson(cycle);
    }

    @TypeConverter
    public Cycle stringToCycle(String value){
        return gson.fromJson(value, Cycle.class);
    }


    ///////Sequence converters///////
    @TypeConverter
    public String sequenceToString(Sequence sequence){
        return gson.toJson(sequence);
    }

    @TypeConverter
    public Sequence stringToSequence(String value){
        return gson.fromJson(value, Sequence.class);
    }


    ///////Seance converters///////
    @TypeConverter
    public String seanceToString(Seance seance){
        return gson.toJson(seance);
    }

    @TypeConverter
    public Seance stringtoSeance(String value){
        return gson.fromJson(value, Seance.class);
    }
}
