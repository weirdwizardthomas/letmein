package com.via.letmein.persistence.model;

import java.io.Serializable;

/**
 * A POJO class representing the household member retrieved from the server.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HouseholdMember implements Serializable {

    private int id;
    private boolean isOwner;
    private String name;
    private String role;
    /**
     * Relative URL path (from the base url) to the picture
     */
    private String profilePhoto;

    public HouseholdMember() {
    }

    public HouseholdMember(int id, boolean isOwner, String name, String role, String profilePhoto) {
        this.id = id;
        this.isOwner = isOwner;
        this.name = name;
        this.role = role;
        this.profilePhoto = profilePhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean isAdministrator) {
        this.isOwner = isAdministrator;
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

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
}
