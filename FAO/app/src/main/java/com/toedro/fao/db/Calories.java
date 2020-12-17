package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;
/**
 * Definition of Calories class for the related table structure in the DB; here are defined the
 * columns of the table to use it as ORM with Room. In the class we have some fields, a constructor
 * and some getter methods.
 */
@Entity(tableName = "Calories")
public class Calories { // initialization of the variable of the db
    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer id;
    Double value;
    String day;
    String timestamp;

    public Calories(Double value, String day, String timestamp) {
        this.value = value;
        this.day = day;
        this.timestamp = timestamp;
    }
    // definition of methods of the class: each method allow to return the value of an instance
    public Integer getId() {
        return id;
    }
    public Double getValue() {
        return value;
    }
    public String getDay() {
        return day;
    }
    public String getTimestamp() {return timestamp; }
}
