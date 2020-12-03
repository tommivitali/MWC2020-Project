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
    @Query("SELECT * FROM Calories WHERE timestamp > (:date)")
    public abstract List<Calories> getCalories(Long date);
    @Query("SELECT timestamp FROM Calories INNER JOIN (SELECT max(timestamp) as Last FROM Calories GROUP BY timestamp)" +
            " cal on Calories.timestamp = cal.Last")
    public abstract Long getLastMeal(); //TODO prendi ultimo pasto, se non esiste ritorna data installazione app
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addCalories(Calories calories);
}
