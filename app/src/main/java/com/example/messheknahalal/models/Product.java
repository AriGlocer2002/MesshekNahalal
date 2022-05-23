package com.example.messheknahalal.models;


import androidx.annotation.NonNull;

import java.io.Serializable;

public class Product implements Serializable {

    private String type;
    private String name;
    private double stock;
    private double price;
    private String picture;
    private double amount;
    private boolean countable;

    public Product(){}

    public Product(String type, String name, double stock, double price, boolean countable) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.amount = 1;
        this.countable = countable;
    }

    public Product(String type, String name, double stock, double price, String picture, boolean countable) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.picture = picture;
        this.amount = 1;
        this.countable = countable;
    }

    public Product(String type, String name, double stock, double price, double amount, String picture, boolean countable) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.amount = amount;
        this.picture = picture;
        this.countable = countable;
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

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", stock=" + stock +
                ", price=" + price +
                ", picture='" + picture + '\'' +
                ", amount=" + amount +
                ", countable=" + countable +
                '}';
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isCountable() {
        return countable;
    }

    public void setCountable(boolean countable) {
        this.countable = countable;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
