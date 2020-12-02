package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public abstract class RecipeIngredientsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addRecipeIngredients(List<RecipeIngredients> recipeIngredients);
}
