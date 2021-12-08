package com.poona.agrocart.ui.product_detail.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class ProductComment
{
    private String userName,date,comment,userImg;
    private float rating;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @BindingAdapter("setImage")
    public static void loadImage(ImageView view, String img){
        Glide.with(view.getContext())
                .load(img).circleCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(view);
    }
}
