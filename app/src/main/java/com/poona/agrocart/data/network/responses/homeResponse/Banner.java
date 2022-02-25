package com.poona.agrocart.data.network.responses.homeResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("adv_type")
    @Expose
    private String advType;
    @SerializedName("adv_url")
    @Expose
    private String advUrl;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("product_id")
    @Expose
    private Object productId;
    @SerializedName("adv_image")
    @Expose
    private String advImage;


    private boolean isDummy;

    public Banner() {
    }

    public Banner(String id, String advUrl, String advImage) {
        this.id = id;
        this.advUrl = advUrl;
        this.advImage = advImage;
    }

    public boolean isDummy() {
        return isDummy;
    }

    public void setDummy(boolean dummy) {
        isDummy = dummy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdvType() {
        return advType;
    }

    public void setAdvType(String advType) {
        this.advType = advType;
    }

    public String getAdvUrl() {
        return advUrl;
    }

    public void setAdvUrl(String advUrl) {
        this.advUrl = advUrl;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Object getProductId() {
        return productId;
    }

    public void setProductId(Object productId) {
        this.productId = productId;
    }

    public String getAdvImage() {
        return advImage;
    }

    public void setAdvImage(String advImage) {
        this.advImage = advImage;
    }
}
