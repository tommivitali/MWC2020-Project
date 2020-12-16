package com.toedro.fao.ui.pantry;

/**
 * class that provides data for modifying Pantry
 */
public class PantryListData {
    Integer id;
    String name;
    Integer quantity;
    String image;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public PantryListData(Integer id, String name, Integer quantity, String image) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.image = image;
    }
}
