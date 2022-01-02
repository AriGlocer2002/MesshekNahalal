package com.example.messheknahalal;

import android.provider.ContactsContract;

import java.util.Date;

public class User extends Person{

    private int id;
    private Date last_login;

    public User() {
        super(0);
        this.id = 0;
        this.last_login = null;
    }

    public User(int id, String name, String surname,
                ContactsContract.CommonDataKinds.Email email,
                int password, int phone, int id1, Date last_login) {
        super(id, name, surname, email, password, phone);
        this.id = id1;
        this.last_login = last_login;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public Date getLast_login() {
        return last_login;
    }

    public void setLast_login(Date last_login) {
        this.last_login = last_login;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", last_login=" + last_login +
                '}';
    }
}
