package com.toedro.fao.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecipeDAO recipeDAO();
}
