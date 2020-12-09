package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class PantryDAO {
    @Query("SELECT * FROM Pantry WHERE id = :id")
    public abstract Pantry getPantry(Integer id);
    @Query("SELECT * FROM Pantry WHERE (name = :name AND barcode = :barcode)")
    public abstract Pantry getPantry(String name, String barcode);
    @Query("SELECT * FROM Pantry")
    public abstract List<Pantry> getPantry();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addPantry(Pantry pantries);
}




