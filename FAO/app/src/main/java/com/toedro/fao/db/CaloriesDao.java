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
    @Query("SELECT * FROM Calories WHERE day = :day")
    public abstract List<Calories> getCalories(String day);
    @Query("SELECT SUM(value) FROM Calories WHERE day = :day")
    public abstract Double getSumCalories(String day);
    @Query("SELECT timestamp FROM Calories ORDER BY id DESC LIMIT 1")
    public abstract String getLastMealTimestamp();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addCalories(Calories calories);
}
