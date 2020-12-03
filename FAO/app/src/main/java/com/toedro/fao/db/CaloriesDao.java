package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class CaloriesDao {
    @Query("SELECT * FROM Calories")
    public abstract List<Calories> getCalories();
    //@Query("SELECT value, COUNT(id) FROM Calories WHERE timestamp > (:date)")
    //public abstract List<Calories> getCalories(Long date);
    //@Query("SELECT * FROM Calories WHERE timestamp > (:date)") //TODO prendi ultimo pasto, se non esiste ritorna data installazione app
    //public abstract List<Calories> getCalories(Long date);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addCalories(Calories calories);
}
