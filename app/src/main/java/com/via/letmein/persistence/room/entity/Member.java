package com.via.letmein.persistence.room.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Member {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String role;
    private int imageID;

    public Member(String name, String role, int imageID) {
        this.name = name;
        this.role = role;
        this.imageID = imageID;
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
