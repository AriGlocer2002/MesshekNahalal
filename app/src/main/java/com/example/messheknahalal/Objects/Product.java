package com.example.messheknahalal.Objects;


public class Product {
    private String name;
    private String type;
    private float stock;
    private float price;

    public Product(){

    }

    public Product(String name, String type, float stock, float price) {
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                '}';
    }
}
