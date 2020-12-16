package com.toedro.fao.db;

import androidx.room.ColumnInfo;
/**
 * this class provides methods
 * in order to return the number of the steps referred to a specific day
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
