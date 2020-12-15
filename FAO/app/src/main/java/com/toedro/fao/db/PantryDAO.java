package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * The PantryDAO class create queries for the Pantry class
 */
@Dao
public abstract class PantryDAO {
    @Query("SELECT * FROM Pantry WHERE id = :id")
    public abstract Pantry getPantry(Integer id);
    @Query("SELECT * FROM Pantry WHERE (name = :name AND barcode = :barcode)")
    public abstract Pantry getPantry(String name, String barcode);
    @Query("SELECT * FROM Pantry")
    public abstract List<Pantry> getPantry();
    @Query("DELETE FROM Pantry WHERE id = :id")
    public abstract void removePantry(Integer id);
    @Query("UPDATE Pantry SET quantity = :quantity WHERE id = :id")
    public abstract void setQuantity(Integer id, Integer quantity);
    @Query("UPDATE Pantry SET quantity = quantity - :quantity WHERE keywords = :keywords")
    public abstract void subQuantity(String keywords, Integer quantity);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addPantry(Pantry pantries);
}




