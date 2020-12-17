package com.toedro.fao.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Definition of Pantry class for the related table structure in the DB; here are defined the
 * columns of the table to use it as ORM with Room. In the class we have some fields, a constructor
 * and some getter methods. In this table are added both the manually typed and the ingredients
 * going from the barcode scanner.
 */
@Entity(tableName = "Pantry")
public class Pantry {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    String name; // product name
    Integer quantity; //quantità
    Integer kcal; //kcalorie
    String keywords; // lista parole chiave
    String barcode; //cod numerico
    String image; //image url

    public Pantry(String name, Integer quantity, Integer kcal, String keywords, String barcode, String image) {
        this.name = name;
        this.quantity = quantity;
        this.kcal = kcal;
        this.keywords = keywords;
        this.barcode = barcode;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getQuantity() { return quantity; }
    public Integer getKcal() { return kcal; }
    public String getKeywords() {return keywords; }
    public String getBarcode() {
        return barcode;
    }
    public String getImage() {
        return image;
    }
}

