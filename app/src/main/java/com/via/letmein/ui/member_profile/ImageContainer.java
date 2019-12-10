package com.via.letmein.ui.member_profile;

public class ImageContainer {
    private String path;
    private boolean isSelected;

    public ImageContainer(String path) {
        this.path = path;
        this.isSelected = false;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

