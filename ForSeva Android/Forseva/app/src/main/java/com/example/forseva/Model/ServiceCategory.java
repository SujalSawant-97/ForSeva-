package com.example.forseva.Model;

public class ServiceCategory {
    private String name;
    private int iconResId;

    public ServiceCategory(String name, int iconResId) {
        this.name = name;
        this.iconResId = iconResId;
    }

    public ServiceCategory() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
