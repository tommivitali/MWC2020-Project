package com.toedro.fao.db;
/**
 * This class provides two fields and the correspondent getter methods to get the results for a
 * query that selects all the ingredients for a recipe. See the RecipeIngredientsDAO for usage.
 */
public class RecipeIngredientsQueryResult {
    String keywords;
    Integer quantity;

    public RecipeIngredientsQueryResult(String keywords, Integer quantity) {
        this.keywords = keywords;
        this.quantity = quantity;
    }

    public String getKeywords() { return keywords; }
    public Integer getQuantity() { return quantity; }
}
