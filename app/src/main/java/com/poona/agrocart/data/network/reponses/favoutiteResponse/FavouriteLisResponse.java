
package com.poona.agrocart.data.network.reponses.favoutiteResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.data.network.reponses.BaseResponse;

public class FavouriteLisResponse extends BaseResponse {
    @SerializedName("favourite_list")
    @Expose
    private List<Favourite> favouriteList = null;

    public List<Favourite> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<Favourite> favouriteList) {
        this.favouriteList = favouriteList;
    }

}
