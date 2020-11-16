package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

@Entity(tableName = "Recipes")
public class Recipe {
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
