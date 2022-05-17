package com.example.messheknahalal.Objects;

public class Order {

    private String product;
    private String name;

    public Order(){}

    @Override
    public String toString() {
        return "Order{" +
                "product='" + product + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Order(String product, String name) {
        this.product = product;
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
