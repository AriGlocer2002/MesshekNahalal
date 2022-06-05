package com.example.messheknahalal.models;

import androidx.annotation.NonNull;

public class User extends Person {

    protected String last_login;

    public User() {
        //required for firebase
    }

    public User(String name, String last_name, String email, String phone, String type) {
        super(name, last_name, email, phone, type);
    }

    public User(String name, String last_name, String email, String phone, String type, String picture) {
        super(name, last_name, email, phone, type, picture);
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString() + "User{" +
                "last_login='" + last_login + '\'' +
                '}';
    }


}
