package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
/**
 * This class provides a series of SQL queries that can be applied to the DB; these are specifically
 * for the RecipeIngredients table and what it concerns.
 */
@Dao
public abstract class RecipeIngredientsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addRecipeIngredients(List<RecipeIngredients> recipeIngredients);
    @Query("SELECT I.keywords, Ri.quantity " +
            "FROM RecipesIngredients RI JOIN Ingredients I ON RI.idIngredient = I.id " +
            "WHERE RI.idRecipe = :recipeID")
    public abstract List<RecipeIngredientsQueryResult> getIngredientsRecipe(String recipeID);
}
