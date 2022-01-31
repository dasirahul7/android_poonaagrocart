package com.poona.agrocart.ui.nav_explore.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class ExploreItems {
    String id,name,img;
    int border = R.color.exp_card_color1,background=R.color.exp_border1;

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public ExploreItems(String id, String name, String img) {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

//    @BindingAdapter("setImage")
//    public static void loadImage(ImageView view, String img){
//        Glide.with(view.getContext())
//                .load(img)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder).into(view);
//    }
}
