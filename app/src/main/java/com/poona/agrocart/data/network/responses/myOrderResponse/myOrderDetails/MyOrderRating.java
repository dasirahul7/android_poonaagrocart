
package com.poona.agrocart.data.network.responses.myOrderResponse.myOrderDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyOrderRating {

    @SerializedName("rating_id")
    @Expose
    private String ratingId;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("review")
    @Expose
    private String review;

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
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

}
