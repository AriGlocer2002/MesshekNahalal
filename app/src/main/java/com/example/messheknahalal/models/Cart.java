package com.example.messheknahalal.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Cart {

    private String userEmail;
    private String userName;
    private boolean delivered;
    private double finalPrice;
    private List<String> products;
    private long date;
    private long number;
    private String status;
    private String userStatus;

    public Cart(){}

    public Cart(String userEmail, String userName, boolean delivered, double finalPrice, List<String> products, long date) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.delivered = delivered;
        this.finalPrice = finalPrice;
        this.products = products;
        this.date = date;
        this.number = Long.getLong(String.valueOf(date).substring(3, 9));
        this.status = delivered + "-" + date;
        this.userStatus = userEmail + "-" + date;
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

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cart{" +
                "userEmail='" + userEmail + '\'' +
                ", delivered=" + delivered +
                ", finalPrice=" + finalPrice +
                ", products=" + products +
                ", date=" + date +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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
