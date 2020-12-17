package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * This class provides a series of SQL queries that can be applied to the DB; these are specifically
 * for the Recipe table and what it concerns.
 */
@Dao
public abstract class RecipeDAO {
    @Query("SELECT * FROM Recipes WHERE id = :id")
    public abstract Recipe getRecipe(String id);
    @Query("SELECT * FROM Recipes")
    public abstract List<Recipe> getRecipes();
    @Query("SELECT SUM(RI.quantity * I.calories /100) " +
            "FROM RecipesIngredients RI JOIN Ingredients I ON RI.idIngredient = I.id " +
            "WHERE RI.idRecipe = :idRecipe")
    public abstract Integer getCalories(String idRecipe);
    @Query("SELECT SUM(RI.quantity * I.calories / 100) AS kcal, R.id, R.name, R.text, R.type, R.image " +
            "FROM RecipesIngredients RI JOIN Ingredients I ON RI.idIngredient = I.id " +
            "JOIN Recipes R ON RI.idRecipe = R.id " +
            "GROUP BY R.id, R.name, R.text, R.type, R.image " +
            "HAVING kcal BETWEEN :min AND :max " +
            "AND I.keywords IN (SELECT keywords FROM Pantry)")
    public abstract List<RecipeQueryResult> getRecipes(Double min, Double max);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addRecipes(List<Recipe> recipes);
}
