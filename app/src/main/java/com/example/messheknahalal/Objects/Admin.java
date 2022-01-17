package com.example.messheknahalal.Objects;

public class Admin extends Person {

    private String code;

    public  Admin(){
    }

    public Admin(String code) {
        this.code = code;
    }

    public Admin(String name, String last_name, String email, String phone, String type, String code) {
        super(name, last_name, email, phone, type);
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
                "code='" + code + '\'' +
                '}';
    }
}
