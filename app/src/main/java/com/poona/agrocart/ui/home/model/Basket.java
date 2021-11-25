package com.poona.agrocart.ui.home.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class Basket {
    String name,price;
    int img;

    public Basket(String name, String price, int img) {
        this.name = name;
        this.price = price;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    @BindingAdapter("setBImage")
    public static void loadBImage(ImageView view, int img){
        Glide.with(view.getContext())
                .load(img)
                .placeholder(R.drawable.capsicon)
                .error(R.drawable.capsicon).into(view);
    }
}
