package com.toedro.fao.db;
/**
 * this class provides methods
 * in order to return the value of keywords (the identify the kind of product)
 * and quantity of the product itself
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
