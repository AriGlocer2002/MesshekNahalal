package com.example.messheknahalal.Objects;

import androidx.annotation.NonNull;

import java.util.List;

public class Cart {

    private String userEmail;
    private boolean delivered;
    private int final_price;
    private List<Order> products;
    private long date;

    public Cart(){}

    public Cart(String userEmail, boolean delivered, int final_price, List<Order> products, long date) {
        this.userEmail = userEmail;
        this.delivered = delivered;
        this.final_price = final_price;
        this.products = products;
        this.date = date;
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

    public int getFinal_price() {
        return final_price;
    }

    public void setFinal_price(int final_price) {
        this.final_price = final_price;
    }

    public List<Order> getProducts() {
        return products;
    }

    public void setProducts(List<Order> products) {
        this.products = products;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cart{" +
                "userEmail='" + userEmail + '\'' +
                ", delivered=" + delivered +
                ", final_price=" + final_price +
                ", products=" + products +
                ", date=" + date +
                '}';
    }
}
