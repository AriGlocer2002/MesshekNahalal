package com.example.messheknahalal.Objects;

public class Admin extends Person {

    private String code;

    public  Admin(){
        super(null);
    }

    public Admin(String id, String name, String surname, String email, String password, String phone, String type, String code) {
        super(id, name, surname, email, password, phone, type);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "code=" + code +
                '}';
    }
}
