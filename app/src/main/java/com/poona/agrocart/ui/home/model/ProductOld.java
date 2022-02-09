package com.poona.agrocart.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductOld implements Parcelable {
    String id, name, offer, price, offerPrice, img, location, weight, quantity, brand;
    boolean organic = false;
    boolean inBasket = false, isFavorite = false;

    public ProductOld(String productId, String productName,
                      String weight, String offerValue, String price,
                      String productImage, String productLocation,
                      String brand, Boolean isFavourite) {
        this.id = productId;
        this.name = productName;
        this.weight = weight;
        this.offer = offerValue;
        this.price = price;
        this.img = productImage;
        this.location = productLocation;
        this.brand = brand;
        this.isFavorite = isFavourite;
    }

    public boolean isInBasket() {
        return inBasket;
    }

    public void setInBasket(boolean inBasket) {
        this.inBasket = inBasket;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public ProductOld() {
    }

    public ProductOld(String id, String name,
                      String weight, String offer, String price,
                      String img, String location, String brand) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.offer = offer;
        this.price = price;
        this.img = img;
        this.location = location;
        this.brand = brand;
    }

    public ProductOld(String id, String name,
                      String weight, String offer,
                      String price, String img, String location) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.offer = offer;
        this.price = price;
        this.img = img;
        this.location = location;
    }

    protected ProductOld(Parcel in) {
        id = in.readString();
        name = in.readString();
        offer = in.readString();
        price = in.readString();
        offerPrice = in.readString();
        img = in.readString();
        location = in.readString();
        weight = in.readString();
        quantity = in.readString();
        brand = in.readString();
        organic = in.readByte() != 0;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public static final Creator<ProductOld> CREATOR = new Creator<ProductOld>() {
        @Override
        public ProductOld createFromParcel(Parcel in) {
            return new ProductOld(in);
        }

        @Override
        public ProductOld[] newArray(int size) {
            return new ProductOld[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }


    public String getOffer() {
        return offer + "% Off";
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getPrice() {
        return "Rs." + price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferPrice() {
        if (this.price.equals(""))
            return "";
        else {
            float price = Float.parseFloat(this.price);
            float offer = Float.parseFloat(this.offer);
            float off = (price/ 100.0f)*offer;
            int offer_price =(int) (price - off);
            this.offerPrice = "Rs." + String.valueOf(offer_price);
        }
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

    public boolean isOrganic() {
        return organic;
    }

    public void setOrganic(boolean organic) {
        this.organic = organic;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

//    @BindingAdapter("setImage")
//    public static void loadImage(ImageView view, String img) {
//        Glide.with(view.getContext())
//                .load(img)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.placeholder).into(view);
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(offer);
        dest.writeString(price);
        dest.writeString(offerPrice);
        dest.writeString(img);
        dest.writeString(location);
        dest.writeString(weight);
        dest.writeString(quantity);
        dest.writeByte((byte) (organic ? 1 : 0));
    }
}
