package com.toedro.fao.db;

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
