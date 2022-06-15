package com.example.messheknahalal.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Order {

    private String userEmail;
    private String userName;
    private boolean delivered;
    private double finalPrice;
    private List<Product> products;
    private long date;
    private long number;

    public Order(){}

    public Order(String userEmail, String userName, boolean delivered, double finalPrice, List<Product> products, long date) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.delivered = delivered;
        this.finalPrice = finalPrice;
        this.products = products;
        this.date = -date;
        this.number = Long.parseLong(String.valueOf(-date).substring(3, 9));
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public String toString() {
        return "Order{" +
                "userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", delivered=" + delivered +
                ", finalPrice=" + finalPrice +
                ", products=" + products +
                ", date=" + date +
                ", number=" + number +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}
