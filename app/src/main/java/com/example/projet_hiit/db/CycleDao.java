package com.example.projet_hiit.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projet_hiit.db.model.Cycle;

import java.util.List;

@Dao
public interface CycleDao {

    @Query("SELECT * FROM Cycle")
    List<Cycle> getAll();

    @Insert
    void insert(Cycle cycle);

    @Insert
    long[] insertAll(Cycle... cycles);

    @Delete
    void delete(Cycle cycle);

    @Update
    void update(Cycle cycle);
}
