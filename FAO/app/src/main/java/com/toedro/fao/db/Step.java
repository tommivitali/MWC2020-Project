package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Definition of Step class for the related table structure in the DB; here are defined the
 * columns of the table to use it as ORM with Room. In the class we have some fields, a constructor
 * and some getter methods. In this table are added all the steps done by a people, detected by
 * the step detector or the accelerometer; rows are added by the listener. For each step we have the
 * relative day, the hour and the timestamp.
 */
@Entity(tableName = "Steps")
public class Step {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer id;
    String day;
    String hour;
    String timestamp;

    public Step(String day, String hour, String timestamp) {
        this.day = day;
        this.hour = hour;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }
    public String getDay() {
        return day;
    }
    public String getHour() {
        return hour;
    }
    public String getTimestamp() {
        return timestamp;
    }
}
