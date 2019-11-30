package com.via.letmein.persistence.pojo;

/**
 * A POJO class representing the household member retrieved from the server.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HouseholdMember {
    private int id;
    private boolean isAdministrator;
    private String name;
    private String role;
    /**
     * Relative URL path (from the base url) to the picture
     */
    private String picturePath;

    public HouseholdMember(int id, boolean isAdministrator, String name, String role, String picturePath) {
        this.id = id;
        this.isAdministrator = isAdministrator;
        this.name = name;
        this.role = role;
        this.picturePath = picturePath;
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

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public boolean isAdministrator() {
        return isAdministrator;
    }

    public void setAdministrator(boolean administrator) {
        isAdministrator = administrator;
    }
}
