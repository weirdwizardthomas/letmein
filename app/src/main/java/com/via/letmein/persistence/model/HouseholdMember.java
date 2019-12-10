package com.via.letmein.persistence.model;

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
    private String profilePhoto;

    public HouseholdMember(int id, boolean isAdministrator, String name, String role, String profilePhoto) {
        this.id = id;
        this.isAdministrator = isAdministrator;
        this.name = name;
        this.role = role;
        this.profilePhoto = profilePhoto;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

}
