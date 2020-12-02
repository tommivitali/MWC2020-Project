package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class RecipeDAO {
    @Query("SELECT * FROM Recipes WHERE id = :id")
    public abstract Recipe getRecipe(String id);
    @Query("SELECT * FROM Recipes")
    public abstract List<Recipe> getRecipes();
    @Query("SELECT SUM(RI.quantity * I.calories) " +
            "FROM RecipesIngredients RI JOIN Ingredients I ON RI.idIngredient = I.id " +
            "WHERE RI.idRecipe = :idRecipe")
    public abstract Integer getCalories(String idRecipe);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addRecipes(List<Recipe> recipes);
}
