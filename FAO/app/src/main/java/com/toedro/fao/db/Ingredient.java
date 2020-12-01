package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.List;

@Entity(tableName = "Ingredients")
public class Ingredient {
    @PrimaryKey
    @NonNull
    String id;
    Integer calories;
    String keywords;
    String name;
    Double quantity;

    public Ingredient(@NonNull String id, Integer calories, List<String> keywords, String name, Double quantity) {
        this.id = id;
        this.calories = calories;
        this.keywords = (new Gson()).toJson(keywords);
        this.name = name;
        this.quantity = quantity;
    }

    public Ingredient(@NonNull String id, Integer calories, String keywords, String name, Double quantity) {
        this.id = id;
        this.calories = calories;
        this.keywords = keywords;
        this.name = name;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public Integer getCalories() { return calories; }
    public List<String> getKeywords() { return (new Gson()).fromJson(keywords, List.class); }
    public String getName() { return name; }
    public Double getQuantity() { return quantity; }
}
