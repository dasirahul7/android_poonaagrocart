package com.poona.agrocart.ui.basket_detail.model;

import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.product_detail.model.ProductComment;

import java.util.ArrayList;

public class BasketDetail {
    private String basketName;
    private ArrayList<String> BasketImages;
    private String BasketImage;
    private boolean isFavorite;
    private String basketPrice;
    private String basketQty;
    private Subscription subscription;
    private ArrayList<ProductItem> productList;
    private String basketDetails;
    private String aboutBasket;
    private String basketBenefit;
    private String basketUses;
    private String basketInfo;
    private String basketWeightPolicy;
    private String basketNutrition;
    private String basketRating;
    private String basketNoOfRatings;
    private ArrayList<ProductComment> commentArrayList;


    public String getBasketName() {
        return basketName;
    }

    public void setBasketName(String basketName) {
        this.basketName = basketName;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public ArrayList<String> getBasketImages() {
        return BasketImages;
    }

    public void setBasketImages(ArrayList<String> basketImages) {
        BasketImages = basketImages;
    }

    public String getBasketImage() {
        return BasketImage;
    }

    public void setBasketImage(String basketImage) {
        BasketImage = basketImage;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getBasketPrice() {
        return basketPrice;
    }

    public void setBasketPrice(String basketPrice) {
        this.basketPrice = basketPrice;
    }

    public String getBasketQty() {
        return basketQty;
    }

    public void setBasketQty(String basketQty) {
        this.basketQty = basketQty;
    }

    public ArrayList<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductItem> productList) {
        this.productList = productList;
    }

    public String getBasketDetails() {
        return basketDetails;
    }

    public void setBasketDetails(String basketDetails) {
        this.basketDetails = basketDetails;
    }

    public String getAboutBasket() {
        return aboutBasket;
    }

    public void setAboutBasket(String aboutBasket) {
        this.aboutBasket = aboutBasket;
    }

    public String getBasketBenefit() {
        return basketBenefit;
    }

    public void setBasketBenefit(String basketBenefit) {
        this.basketBenefit = basketBenefit;
    }

    public String getBasketUses() {
        return basketUses;
    }

    public void setBasketUses(String basketUses) {
        this.basketUses = basketUses;
    }

    public String getBasketInfo() {
        return basketInfo;
    }

    public void setBasketInfo(String basketInfo) {
        this.basketInfo = basketInfo;
    }

    public String getBasketWeightPolicy() {
        return basketWeightPolicy;
    }

    public void setBasketWeightPolicy(String basketWeightPolicy) {
        this.basketWeightPolicy = basketWeightPolicy;
    }

    public String getBasketNutrition() {
        return basketNutrition;
    }

    public void setBasketNutrition(String basketNutrition) {
        this.basketNutrition = basketNutrition;
    }

    public String getBasketRating() {
        return basketRating;
    }

    public void setBasketRating(String basketRating) {
        this.basketRating = basketRating;
    }

    public String getBasketNoOfRatings() {
        return basketNoOfRatings;
    }

    public void setBasketNoOfRatings(String basketNoOfRatings) {
        this.basketNoOfRatings = basketNoOfRatings;
    }
    public ArrayList<ProductComment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<ProductComment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }
}
