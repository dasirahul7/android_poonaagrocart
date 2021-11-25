package com.poona.agrocart.ui.home.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class Product {
    String id, name, qty,offer, price, offerPrice,img;

    public Product(String id, String name,
                   String qty, String offer, String price,
                   String offerPrice, String img) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.offer = offer;
        this.price = price;
        this.offerPrice = offerPrice;
        this.img = img;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @BindingAdapter("setPImage")
    public static void loadPImage(ImageView view,String img){
        Glide.with(view.getContext())
                .load(img)
                .placeholder(R.drawable.capsicon)
                .error(R.drawable.capsicon).into(view);
    }
}
