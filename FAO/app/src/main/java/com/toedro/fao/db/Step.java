package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
