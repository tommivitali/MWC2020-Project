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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addRecipes(List<Recipe> recipes);
}
