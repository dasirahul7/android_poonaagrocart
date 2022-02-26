package com.poona.agrocart.ui.product_detail.model;


import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;

public class AllReview  implements Parcelable, Serializable {

    private String name;
    private String image;
    private String rating;
    private String review;
    private String date;



    protected AllReview(Parcel in) {
        name = in.readString();
        image = in.readString();
        rating = in.readString();
        review = in.readString();
        date = in.readString();
    }

    public static final Creator<AllReview> CREATOR = new Creator<AllReview>() {
        @Override
        public AllReview createFromParcel(Parcel in) {
            return new AllReview(in);
        }

        @Override
        public AllReview[] newArray(int size) {
            return new AllReview[size];
        }
    };


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(image);
        parcel.writeString(rating);
        parcel.writeString(review);
        parcel.writeString(date);
    }

}
