package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * The RecipeIngredientsDAO class create queries for the RecipeIngredients class
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
