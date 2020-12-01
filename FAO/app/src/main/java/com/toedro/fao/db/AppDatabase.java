package com.toedro.fao.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, Step.class, Pantry.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDAO recipeDAO();
    public abstract StepDAO stepDAO();
    public abstract PantryDAO pantryDAO();
}
