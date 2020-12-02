package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "RecipesIngredients", primaryKeys = {"idRecipe","idIngredient"})
public class RecipeIngredients {
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

    public String getIdRecipe() { return idRecipe; }
    public String getIdIngredient() { return idIngredient; }
    public Integer getQuantity() { return quantity; }
}
