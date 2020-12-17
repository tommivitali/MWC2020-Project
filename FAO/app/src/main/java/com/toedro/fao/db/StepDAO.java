package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * This class provides a series of SQL queries that can be applied to the DB; these are specifically
 * for the Step table and what it concerns.
 */
@Dao
public abstract class StepDAO {
    @Query("SELECT day, COUNT(id) FROM Steps WHERE day IN (:dateInterval) GROUP BY day")
    public abstract List<StepsQueryResult> getSteps(List<String> dateInterval);
    @Query("SELECT COUNT(*) FROM Steps WHERE day = :day")
    public abstract Integer getDaySteps(String day);
    @Query("SELECT day, COUNT(id) FROM Steps GROUP BY day")
    public abstract List<StepsQueryResult> getSteps();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addStep(Step step);
}
