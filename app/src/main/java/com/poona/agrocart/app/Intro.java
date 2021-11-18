package com.poona.agrocart.app;

public class Intro {
    String id, Title,Description;
    Integer imgFile;

    public Intro(String id, String title, String description, Integer imgFile) {
        this.id = id;
        Title = title;
        Description = description;
        this.imgFile = imgFile;
    }

    public Integer getImgFile() {
        return imgFile;
    }

    public void setImgFile(Integer imgFile) {
        this.imgFile = imgFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
