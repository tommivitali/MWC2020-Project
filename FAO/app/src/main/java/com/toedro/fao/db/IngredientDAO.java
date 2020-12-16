package com.toedro.fao.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;
/**
 * this class provides a serie of sql query that can be applied to to the db
 */
@Dao
public abstract class IngredientDAO {
    @Query("SELECT * FROM Ingredients WHERE id = :id")
    public abstract Ingredient getIngredient(String id);
    @Query("SELECT * FROM Ingredients WHERE id IN (:id)")
    public abstract List<Ingredient> getIngredients(List<String> id);
    @Query("SELECT * FROM Ingredients")
    public abstract List<Ingredient> getIngredients();
    @Query("SELECT keywords FROM Ingredients")
    public abstract List<String> getKeywords();
    @Query("SELECT keywords FROM Ingredients WHERE name = :name")
    public abstract List<String> getKeywords(String name);
    @Query("SELECT name FROM Ingredients ORDER BY name")
    public abstract List<String> getNames();
    @Query("SELECT calories FROM Ingredients WHERE (name = :keywords OR keywords = :keywords)")
    public abstract Integer getCals_100g(String keywords);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addIngredients(List<Ingredient> ingredients);
}
