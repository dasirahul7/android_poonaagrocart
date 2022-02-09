package com.poona.agrocart.ui.nav_favourites;

import com.poona.agrocart.ui.home.model.ProductOld;

public class FavouriteItem extends ProductOld
{
    private boolean isFavorite ;

    public FavouriteItem(String id, String name, String weight, String offer,
                         String price, String img, String location, String brand,
                         boolean isFavorite) {
        super(id, name, weight, offer, price, img, location, brand);
        this.isFavorite = isFavorite;
    }

    @Override
    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
