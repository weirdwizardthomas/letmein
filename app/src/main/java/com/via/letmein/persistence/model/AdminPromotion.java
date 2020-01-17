package com.via.letmein.persistence.model;

public class AdminPromotion {
    private final String password;
    private final int id;
    private final String name;

    public AdminPromotion(String password, int id, String name) {
        this.password = password;
        this.id = id;
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getUser_name() {
        return name;
    }
}
