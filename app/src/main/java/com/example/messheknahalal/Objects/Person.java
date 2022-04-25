package com.example.messheknahalal.Objects;

import androidx.annotation.NonNull;

public class Person {

    protected String name;
    protected String last_name;
    protected String email;
    protected String phone;
    protected String type;
    protected String token;
    protected String picture;

    public Person(){}

    public Person(String name, String last_name, String email, String phone, String type) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public Person(String name, String last_name, String email, String phone, String type, String picture) {
        this.name = name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                ", token='" + token + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
