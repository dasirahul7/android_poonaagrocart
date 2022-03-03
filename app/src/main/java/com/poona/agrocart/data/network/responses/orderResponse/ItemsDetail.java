package com.poona.agrocart.data.network.responses.orderResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItemsDetail {
    @SerializedName("item_type")
    @Expose
    public String itemType;
    @SerializedName("cart_id")
    @Expose
    public String cartId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("pu_id")
    @Expose
    public String puId;
    @SerializedName("unit_id")
    @Expose
    public String unitId;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("price_per_quantity")
    @Expose
    public String pricePerQuantity;
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("feature_img")
    @Expose
    public String featureImg;
    @SerializedName("weight")
    @Expose
    public String weight;
    @SerializedName("unit_name")
    @Expose
    public String unitName;
    @SerializedName("selling_price")
    @Expose
    public String sellingPrice;
    @SerializedName("total_price")
    @Expose
    public String totalPrice;
    @SerializedName("basket_id")
    @Expose
    public String basketId;
    @SerializedName("basket_name")
    @Expose
    public String basketName;
    @SerializedName("basket_rate")
    @Expose
    public String basketRate;

}
