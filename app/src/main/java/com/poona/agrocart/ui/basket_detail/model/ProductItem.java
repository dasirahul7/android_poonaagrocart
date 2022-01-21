package com.poona.agrocart.ui.basket_detail.model;

public class ProductItem {
    String name ,weight;

    public ProductItem() {
    }

    public ProductItem(String name, String weight) {
        this.name = name;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
