package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
/**
 * definition of the db structure
 * in order to manage Ids of different recipes, ingredient and quantity
 * in the following class are defined variables of the db
 * and the method that can be applied to this class
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
