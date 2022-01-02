package com.example.messheknahalal;

import android.provider.ContactsContract;

public class Admin extends Person {

    private int id;
    private int code;

    public  Admin(){
        super(0);
        this.id = 0;
        this.code = 0;
    }

    public Admin(int id, String name, String surname,
                 ContactsContract.CommonDataKinds.Email email,
                 int password, int phone, int id1, int code) {
        super(id, name, surname, email, password, phone);
        this.id = id1;
        this.code = code;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", code=" + code +
                '}';
    }
}
