package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.List;
/**
 * definition of Ingredients db structure
 * in the following class are defined variables of the db
 * and the method that can be applied to this class
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
