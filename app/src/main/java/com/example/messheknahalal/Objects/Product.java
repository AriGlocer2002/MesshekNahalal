package com.example.messheknahalal.Objects;


import androidx.annotation.NonNull;

import java.io.Serializable;

public class Product implements Serializable {
    private String type;
    private String name;
    private String stock;
    private String price;
    private String picture;

    public Product(){}

    public Product(String type, String name, String stock, String price) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    public Product(String type, String name, String stock, String price, String picture) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.picture = picture;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stock='" + stock + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
