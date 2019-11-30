package com.via.letmein.ui.member_profile;

public class ImageContainer {
    private String description;
    private boolean isSelected;

    public ImageContainer(String description) {
        this.description = description;
        this.isSelected = false;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void toggleSelected() {
        isSelected = !isSelected;
    }
}

