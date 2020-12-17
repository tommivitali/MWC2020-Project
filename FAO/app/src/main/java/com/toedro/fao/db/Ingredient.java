package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.List;
/**
 * Definition of Ingredients class for the related table structure in the DB; here are defined the
 * columns of the table to use it as ORM with Room. In the class we have some fields, a constructor
 * and some getter methods. This table is a copy of the "ingredients" collection in firestore cloud,
 * updated every time the app starts through the splash fragment.
 */
@Entity(tableName = "Ingredients")
public class Ingredient { // initialization of the variable of the db
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
    // definition of methods of the class: each method allow to return the value of an instance
    public String getId() { return id; }
    public Integer getCalories() { return calories; }
    public List<String> getKeywords() { return (new Gson()).fromJson(keywords, List.class); }
    public String getName() { return name; }
    public Double getQuantity() { return quantity; }
}
