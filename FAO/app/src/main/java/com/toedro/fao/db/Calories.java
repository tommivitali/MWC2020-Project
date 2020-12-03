package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "Calories")
public class Calories {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    Integer id;
    Double value;
    //String timestamp;
    //Calendar timestamp;
    Long timestamp;

    public Calories(Double value, Long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }
    public Double getValue() {
        return value;
    }
    public Long getTimestamp() {
        return timestamp;
    }
}
