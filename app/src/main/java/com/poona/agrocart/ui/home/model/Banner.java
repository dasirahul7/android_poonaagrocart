package com.poona.agrocart.ui.home.model;

public class Banner {
    String id ,Name;
    int Image;

    public Banner(String id, String name, int image) {
        this.id = id;
        Name = name;
        Image = image;
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

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
