package com.example.messheknahalal.Objects;

import android.provider.ContactsContract;

import java.net.PasswordAuthentication;

public class Person {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;


    public Person(){
    }

    public Person(String id){
        this.id = id;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = null;
        this.phone = null;
    }

    public Person(String id, String name, String surname,
                  String email,
                  String password, String phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phone = phone;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email=" + email +
                ", password=" + password +
                ", phone=" + phone +
                '}';
    }
}
