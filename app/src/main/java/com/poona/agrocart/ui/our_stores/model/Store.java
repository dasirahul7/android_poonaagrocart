package com.poona.agrocart.ui.our_stores.model;

public class Store
{
    private String id,name,location;
    private int img;

    public Store()
    {
    }
    public Store(String id, String name, String location, int img) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
