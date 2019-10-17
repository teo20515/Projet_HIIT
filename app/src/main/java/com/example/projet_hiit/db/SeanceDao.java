package com.example.projet_hiit.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projet_hiit.db.model.Seance;

import java.util.List;

@Dao
public interface SeanceDao {

    @Query("SELECT * FROM Seance")
    List<Seance> getAll();

    @Insert
    void insert(Seance seance);

    @Insert
    long[] insertAll(Seance... seance);

    @Delete
    void delete(Seance seance);

    @Update
    void update(Seance seance);
}
