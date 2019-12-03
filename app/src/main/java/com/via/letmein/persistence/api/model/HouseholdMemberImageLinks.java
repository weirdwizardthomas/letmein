package com.via.letmein.persistence.api.model;

import java.util.List;

/**
 * Container of received links to images.
 * @author Tomas Koristka: 291129@via.dk
 */
public class HouseholdMemberImageLinks {
    private List<String> imagePaths;

    public HouseholdMemberImageLinks(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}
