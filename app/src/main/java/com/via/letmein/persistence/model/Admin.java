package com.via.letmein.persistence.model;

public class Admin {
    private final String password;
    private final int id;

    public Admin(String password, int id) {
        this.password = password;
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }
}
