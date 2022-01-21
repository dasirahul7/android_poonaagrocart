package com.poona.agrocart.ui.nav_explore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.nav_explore.model.ExploreItems;

import java.util.ArrayList;

public class ExploreViewModel extends ViewModel {
    MutableLiveData<ArrayList<ExploreItems>> exploreMutableLiveData = new MutableLiveData<>();

    public ExploreViewModel() {
        ArrayList<ExploreItems> exploreItems = new ArrayList<>();
        for (int i=0;i<10;i++){
            ExploreItems expItem = new ExploreItems("0","Fresh Fruits","https://www.linkpicture.com/q/pngfuel-6.png");

            if (i==0){
                expItem.setName("Basket");
                expItem.setImg("https://www.linkpicture.com/q/dait-basket.png");
                expItem.setBackground(R.color.exp_card_color1);
                expItem.setBorder(R.color.exp_border1);
            }
            if (i==1){
                expItem.setName("Vegetable");
                expItem.setImg("https://www.linkpicture.com/q/Vegetables.png");
                expItem.setBackground(R.color.exp_card_color1);
                expItem.setBorder(R.color.exp_border1);
            }
            if (i==2||i==3){
                expItem.setBackground(R.color.exp_card_color2);
                expItem.setBorder(R.color.exp_border2);
            }
            if (i==4||i==5){
                expItem.setName("Exotic Fruits & Vegetable ");
                expItem.setBackground(R.color.exp_card_color3);
                expItem.setBorder(R.color.exp_border3);
            }
            exploreItems.add(expItem);
        }
        exploreMutableLiveData.setValue(exploreItems);
    }
}