package com.example.projet_hiit.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.projet_hiit.db.model.Sequence;

import java.util.List;

@Dao
public interface SequenceDao {

    @Query("SELECT * FROM Sequence")
    List<Sequence> getAll();

    @Insert
    void insert(Sequence sequence);

    @Insert
    long[] insertAll(Sequence... sequences);

    @Delete
    void delete(Sequence sequence);

    @Update
    void update(Sequence sequence);
}
