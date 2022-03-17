
package com.poona.agrocart.data.network.responses.filterResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SortByFilterList {

    @SerializedName("price_filter_id")
    @Expose
    private String priceFilterId;
    @SerializedName("price_filter")
    @Expose
    private String priceFilter;

    public String getPriceFilterId() {
        return priceFilterId;
    }

    public void setPriceFilterId(String priceFilterId) {
        this.priceFilterId = priceFilterId;
    }

    public String getPriceFilter() {
        return priceFilter;
    }

    public void setPriceFilter(String priceFilter) {
        this.priceFilter = priceFilter;
    }

}
