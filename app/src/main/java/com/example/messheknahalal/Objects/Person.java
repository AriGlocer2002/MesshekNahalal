package com.example.messheknahalal.Objects;

import android.provider.ContactsContract;

import java.net.PasswordAuthentication;

public class Person {

    private String id;
    private String name;
    private String last_name;
    private String email;
    private String password;
    private String phone;
    private String type;


    public Person(){
    }

    public Person(String id){
        this.id = id;
        this.name = null;
        this.last_name = null;
        this.email = null;
        this.password = null;
        this.phone = null;
        this.type = null;
    }


    public Person(String id, String name, String surname,
                  String email,
                  String password, String phone, String type) {
        this.id = id;
        this.name = name;
        this.last_name = surname;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setLast_name(String surname) {
        this.last_name = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public String toString() {
        return "Person{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
