package com.poona.agrocart.ui.explore;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.poona.agrocart.ui.explore.model.ExploreItems;

import java.util.ArrayList;

public class ExploreViewModel extends ViewModel {
    MutableLiveData<ArrayList<ExploreItems>> exploreMutableLiveData = new MutableLiveData<>();

    public ExploreViewModel() {
        ArrayList<ExploreItems> exploreItems = new ArrayList<>();
        ExploreItems expItem = new ExploreItems("0","Fresh Fruits","https://www.linkpicture.com/q/pngfuel-6.png");
        for (int i=0;i<10;i++){
            exploreItems.add(expItem);
        }
        exploreMutableLiveData.setValue(exploreItems);
    }
}