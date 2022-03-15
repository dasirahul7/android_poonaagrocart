package com.poona.agrocart.ui.product_detail.model;

import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.ui.basket_detail.model.ProductItem;

import java.util.ArrayList;

public class ProductDetail extends ProductDetailsResponse {
    public String productId;
    public String productName;
    public String productLocation;
    public ArrayList<String> weightOfProduct;
    public ArrayList<String> productImages;
    public String productImage;
    public String price;
    public String OfferPrice;
    public String OfferValue;
    public String quantity;
    public String productDetailBrief;
    public String aboutProduct;
    public String benefitProduct;
    public String storageUses;
    public String otherProductInfo;
    public String validityWeightPolicy;
    public String nutritionDetailBrief;
    public String productReview;
    public String ratings;
    public String productOfferMsg;
    public Boolean basket = false;
    public Boolean organic = false;
    public Boolean isFavourite = false;
    public String brand;
    // Basket details
    private BasketDetailsResponse.Subscription subscription;
    private ArrayList<ProductItem> productList;
    private ArrayList<ProductComment> commentList;
    private ArrayList<BasketContent> basketContents;

    public Boolean getFavourite() {
        return isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getOrganic() {
        return organic;
    }

    public void setOrganic(Boolean organic) {
        this.organic = organic;
    }

    public Boolean isOrganic() {
        return organic;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ArrayList<BasketContent> getBasketContents() {
        return basketContents;
    }

    public void setBasketContents(ArrayList<BasketContent> basketContents) {
        this.basketContents = basketContents;
    }

    public ArrayList<ProductComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<ProductComment> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<ProductItem> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ProductItem> productList) {
        this.productList = productList;
    }

    public BasketDetailsResponse.Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(BasketDetailsResponse.Subscription subscription) {
        this.subscription = subscription;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getBenefitProduct() {
        return benefitProduct;
    }

    public void setBenefitProduct(String benefitProduct) {
        this.benefitProduct = benefitProduct;
    }

    public String getStorageUses() {
        return storageUses;
    }

    public void setStorageUses(String storageUses) {
        this.storageUses = storageUses;
    }

    public String getOtherProductInfo() {
        return otherProductInfo;
    }

    public void setOtherProductInfo(String otherProductInfo) {
        this.otherProductInfo = otherProductInfo;
    }

    public String getValidityWeightPolicy() {
        return validityWeightPolicy;
    }

    public void setValidityWeightPolicy(String validityWeightPolicy) {
        this.validityWeightPolicy = validityWeightPolicy;
    }

    public String getProductReview() {
        return productReview;
    }

    public void setProductReview(String productReview) {
        this.productReview = productReview;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public ArrayList<String> getWeightOfProduct() {
        return weightOfProduct;
    }

    public void setWeightOfProduct(ArrayList<String> weightOfProduct) {
        this.weightOfProduct = weightOfProduct;
    }

    public ArrayList<String> getProductImages() {
        return productImages;
    }

    public void setProductImages(ArrayList<String> productImages) {
        this.productImages = productImages;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductDetailBrief() {
        return productDetailBrief;
    }

    public void setProductDetailBrief(String productDetailBrief) {
        this.productDetailBrief = productDetailBrief;
    }

    public String getNutritionDetailBrief() {
        return nutritionDetailBrief;
    }

    public void setNutritionDetailBrief(String nutritionDetailBrief) {
        this.nutritionDetailBrief = nutritionDetailBrief;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public Boolean getBasket() {
        return basket;
    }

    public void setBasket(Boolean basket) {
        this.basket = basket;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getOfferValue() {
        return OfferValue;
    }

    public void setOfferValue(String offerValue) {
        OfferValue = offerValue;
    }

    public String getProductOfferMsg() {
        return productOfferMsg;
    }

    public void setProductOfferMsg(String productOfferMsg) {
        this.productOfferMsg = productOfferMsg;
    }
}
