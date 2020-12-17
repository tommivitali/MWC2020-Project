package com.toedro.fao.db;

/**
 * This class provides some fields and the correspondent getter methods to get the results for a
 * query that selects some recipes and the correspondent calculated kcals.
 * See the RecipeDAO for usage.
 */
public class RecipeQueryResult {
    Double kcal;
    String id;
    String name;
    String text;
    String type;
    String image;


    public RecipeQueryResult(Double kcal, String id, String name, String text, String type, String image) {
        this.kcal = kcal;
        this.id = id;
        this.name = name;
        this.text = text;
        this.type = type;
        this.image = image;
    }

    public Double getKcal() { return kcal; }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
    public String getType() {
        return type;
    }
    public String getImage() {
        return image;
    }
}
