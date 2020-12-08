package com.toedro.fao.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.database.annotations.NotNull;

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

