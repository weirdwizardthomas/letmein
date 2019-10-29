package com.via.letmein.entities;
public class Member {

    private String name;
    private String role;
    private int imageID;

    public Member(String name, String role, int imageID) {
        this.name = name;
        this.role = role;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
