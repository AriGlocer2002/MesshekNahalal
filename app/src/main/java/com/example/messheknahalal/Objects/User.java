package com.example.messheknahalal.Objects;

import com.example.messheknahalal.Objects.Person;

import java.util.Date;

public class User extends Person {

    private String last_login;

    public User() {
        super(null);
    }

    public User(String id, String name, String surname, String email, String password, String phone, String last_login) {
        super(id, name, surname, email, password, phone);
        this.last_login = last_login;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    @Override
    public String toString() {
        return "User{" +
                "last_login=" + last_login +
                '}';
    }
}
