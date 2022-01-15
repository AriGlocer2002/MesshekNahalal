package com.example.messheknahalal.Objects;

import com.example.messheknahalal.Objects.Person;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User extends Person {

    private String last_login;

    public User() {
        //required for firebase
    }

    public User(String id, String name, String surname, String email, String password, String phone, String type, String last_login) {
        super(id, name, surname, email, password, phone, type);
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

    public String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = simpleDateFormat.format(c);
        return formattedDate;
    }
}
