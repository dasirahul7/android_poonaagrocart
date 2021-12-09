package com.poona.agrocart.ui.basket_page;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class BasketCard
{
    private String imgUrl,productName,price,discountedPrice;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
    @BindingAdapter("setImage")
    public static void loadImage(ImageView view, String img){
        Glide.with(view.getContext())
                .load(img)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(view);
    }
}
