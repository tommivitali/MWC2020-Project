package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * definition of steps db structure
 * steps are counted by the step-counter/step-detector
 * and uploaded with the specifics of the time in which the step is taken
 * in the following class are defined variables of the db
 * and the method that can be applied to this class in order to visualize steps along time
 */
@Entity(tableName = "Steps")
public class Step { // initialization of the variable of the db
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
    // definition of methods of the class: each method allow to return the value of an instance
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
