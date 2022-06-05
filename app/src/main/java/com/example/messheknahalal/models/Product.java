package com.example.messheknahalal.models;


import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * The type Product.
 */
public class Product implements Serializable {

    private String type;
    private String name;
    private double stock;
    private double price;
    private String picture;
    private double amount;
    private boolean countable;

    /**
     * Instantiates a new Product.
     */
    public Product(){}

    /**
     * Instantiates a new Product.
     *
     * @param type      the type
     * @param name      the name
     * @param stock     the stock
     * @param price     the price
     * @param countable the countable
     */
    public Product(String type, String name, double stock, double price, boolean countable) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.amount = 1;
        this.countable = countable;
    }

    /**
     * Instantiates a new Product.
     *
     * @param type      the type
     * @param name      the name
     * @param stock     the stock
     * @param price     the price
     * @param picture   the picture
     * @param countable the countable
     */
    public Product(String type, String name, double stock, double price, String picture, boolean countable) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.picture = picture;
        this.amount = 1;
        this.countable = countable;
    }

    /**
     * Instantiates a new Product.
     *
     * @param type      the type
     * @param name      the name
     * @param stock     the stock
     * @param price     the price
     * @param amount    the amount
     * @param picture   the picture
     * @param countable the countable
     */
    public Product(String type, String name, double stock, double price, double amount, String picture, boolean countable) {
        this.type = type;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.amount = amount;
        this.picture = picture;
        this.countable = countable;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets stock.
     *
     * @return the stock
     */
    public double getStock() {
        return stock;
    }

    /**
     * Sets stock.
     *
     * @param stock the stock
     */
    public void setStock(double stock) {
        this.stock = stock;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
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
                ", countable=" + countable +
                '}';
    }

    @NonNull
    public String print() {
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
