package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
/**
 * Definition of Ingredients class for the related table structure in the DB; here are defined the
 * columns of the table to use it as ORM with Room. In the class we have some fields, a constructor
 * and some getter methods. This table is a copy of what holds the firestore cloud DB, and it is
 * used to "connect" the recipes table with the ingredients table.
 */
@Entity(tableName = "RecipesIngredients", primaryKeys = {"idRecipe","idIngredient"})
public class RecipeIngredients { // initialization of the variable of the db
    @NonNull
    String idRecipe;
    @NonNull
    String idIngredient;
    Integer quantity;

    public RecipeIngredients(@NonNull String idRecipe, @NonNull String idIngredient, Integer quantity) {
        this.idRecipe = idRecipe;
        this.idIngredient = idIngredient;
        this.quantity = quantity;
    }
    // definition of methods of the class: each method allow to return the value of an instance
    public String getIdRecipe() { return idRecipe; }
    public String getIdIngredient() { return idIngredient; }
    public Integer getQuantity() { return quantity; }
}
