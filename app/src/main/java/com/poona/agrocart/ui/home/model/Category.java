package com.poona.agrocart.ui.home.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.poona.agrocart.R;

public class Category {
    String id, name;
    int img;

    public Category(String id, String name, int img) {
        this.id = id;
        this.name = name;
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

    public int  getImg() {
        return img;
    }

    public void setImg(int  img) {
        this.img = img;
    }

    @BindingAdapter("setImage")
    public static void loadImage(ImageView view, int imageUrl) {

        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.green_leafy_vegetable)
                .error(R.drawable.green_leafy_vegetable).into(view);
    }
}
