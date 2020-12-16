package com.toedro.fao.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * The AppDatabase class create the database handled in the app, calling DAOs (classes where data is accessed)
 * This class provides a general structure for the functioning of each db
 * Each instance provides the method for each db
 */
@Database(entities = {Recipe.class, Step.class, Ingredient.class, Pantry.class, RecipeIngredients.class, Calories.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDAO recipeDAO();
    public abstract StepDAO stepDAO();
    public abstract CaloriesDao caloriesDao();
    public abstract IngredientDAO ingredientDAO();
    public abstract PantryDAO pantryDAO();
    public abstract RecipeIngredientsDAO recipeIngredientsDAO();
}
