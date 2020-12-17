package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

/**
 * Definition of Recipe class for the related table structure in the DB; here are defined the
 * columns of the table to use it as ORM with Room. In the class we have some fields, a constructor
 * and some getter methods. This table is a copy of the "recipes" collection in firestore cloud,
 * updated every time the app starts through the splash fragment.
 */
@Entity(tableName = "Recipes")
public class Recipe { // initialization of the variable of the db
    @PrimaryKey
    @NonNull
    String id;
    String name;
    String text;
    String type;
    String image;

    public Recipe(@NonNull String id, String name, String text, String type, String image) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.type = type;
        this.image = image;
    }
    // definition of methods of the class: each method allow to return the value of an instance
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
    public String getType() {
        return type;
    }
    public String getImage() {
        return image;
    }
}
