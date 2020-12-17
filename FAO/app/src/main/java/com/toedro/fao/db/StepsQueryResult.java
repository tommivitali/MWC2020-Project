package com.toedro.fao.db;

import androidx.room.ColumnInfo;
/**
 * This class provides some fields and the correspondent getter methods to get the results for a
 * query that selects the number of steps referred to a specific day.
 * See the StepDAO for multiple usage.
 */
public class StepsQueryResult {
    String day;
    @ColumnInfo(name = "COUNT(id)")
    Integer steps;

    public StepsQueryResult(String day, Integer steps) {
        this.day = day;
        this.steps = steps;
    }

    public String getDay() {
        return day;
    }

    public Integer getSteps() {
        return steps;
    }
}
