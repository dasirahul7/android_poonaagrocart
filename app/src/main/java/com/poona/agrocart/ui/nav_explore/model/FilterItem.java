package com.poona.agrocart.ui.nav_explore.model;

public class FilterItem {
    String id,Name;
    boolean isSelected;

    public FilterItem(String id, String name, boolean isSelected) {
        this.id = id;
        Name = name;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
