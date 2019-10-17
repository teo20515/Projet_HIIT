package com.example.projet_hiit.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projet_hiit.db.model.Travail;

import java.util.List;

@Dao
public interface TravailDao {

    @Query("SELECT  * FROM Travail")
    List<Travail> getAll();

    @Insert
    void insert(Travail travail);

    @Insert
    long[] insertAll(Travail... traveaux);

    @Delete
    void delete(Travail travail);

    @Update
    void update(Travail travail);

}
