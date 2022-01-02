package com.example.messheknahalal;

import android.provider.ContactsContract;

import java.net.PasswordAuthentication;

public class Person {

    private int id;
    private String name;
    private String surname;
    private ContactsContract.CommonDataKinds.Email email;
    private int password;
    private int phone;

    public Person(){
        this.id = 0;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = 0;
        this.phone = 0;
    }

    public Person(int id){
        this.id = id;
        this.name = null;
        this.surname = null;
        this.email = null;
        this.password = 0;
        this.phone = 0;
    }

    public Person(int id, String name, String surname,
                  ContactsContract.CommonDataKinds.Email email,
                  int password, int phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public ContactsContract.CommonDataKinds.Email getEmail() {
        return email;
    }

    public void setEmail(ContactsContract.CommonDataKinds.Email email) {
        this.email = email;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
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
