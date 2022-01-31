package com.poona.agrocart.ui.home.model;

import com.poona.agrocart.data.network.reponses.BannerDetails;

public class Banner extends BannerDetails {
    String id ,Name,Image;

    public Banner(String id, String name, String image) {
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

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
