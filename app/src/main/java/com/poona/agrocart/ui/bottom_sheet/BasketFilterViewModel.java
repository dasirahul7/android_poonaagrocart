package com.poona.agrocart.ui.bottom_sheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.nav_explore.model.FilterItem;

import java.util.ArrayList;

public class BasketFilterViewModel extends AndroidViewModel {
    private ArrayList<FilterItem> categoryItems;
    private ArrayList<FilterItem> filterItems;
    private ArrayList<FilterItem> brandItems;
    private MutableLiveData<ArrayList<FilterItem>> categoryLiveData;
    private MutableLiveData<ArrayList<FilterItem>> filterItemLiveData;
    private MutableLiveData<ArrayList<FilterItem>> brandItemLiveData;
    public BasketFilterViewModel(@NonNull Application application) {
        super(application);
        categoryLiveData = new MutableLiveData<>();
        filterItemLiveData = new MutableLiveData<>();
        brandItemLiveData = new MutableLiveData<>();
        initLists();
    }

    private void initLists() {
        categoryItems = new ArrayList<>();
        filterItems = new ArrayList<>();
        brandItems = new ArrayList<>();
        //Sample categories
        FilterItem category1 = new FilterItem("1", "Vegetable", false);
        FilterItem category2 = new FilterItem("2", "Leafy Vegetables", false);
        FilterItem category3 = new FilterItem("3", "Herbs", false);
        FilterItem category4 = new FilterItem("4", "Fruits", false);
        FilterItem category5 = new FilterItem("5", "Dry Fruits", false);
        FilterItem category6 = new FilterItem("6", "Sprouts", false);

        categoryItems.add(category1);
        categoryItems.add(category2);
        categoryItems.add(category3);
        categoryItems.add(category4);
        categoryItems.add(category5);

        //Sample Sort By Filter
        FilterItem filterItem1 = new FilterItem("1", "Low to High", false);
        FilterItem filterItem2 = new FilterItem("2", "High to Low", false);
        FilterItem filterItem3 = new FilterItem("3", "Newest Arrived", false);
        filterItems.add(filterItem1);
        filterItems.add(filterItem2);
        filterItems.add(filterItem3);
        filterItems.add(filterItem3);
        filterItems.add(filterItem3);

        // sample Brand Items

        FilterItem brand1 = new FilterItem("1", "B&G Green", false);
        FilterItem brand2 = new FilterItem("2", "Del Monte", false);
        FilterItem brand3 = new FilterItem("3", "Fruttella", false);
        brandItems.add(brand1);
        brandItems.add(brand2);
        brandItems.add(brand3);
        brandItems.add(brand3);

        // set LiveData values
        categoryLiveData.setValue(categoryItems);
        filterItemLiveData.setValue(filterItems);
        brandItemLiveData.setValue(brandItems);
    }

    public MutableLiveData<ArrayList<FilterItem>> getCategoryLiveData() {
        return categoryLiveData;
    }

    public MutableLiveData<ArrayList<FilterItem>> getFilterItemLiveData() {
        return filterItemLiveData;
    }

    public MutableLiveData<ArrayList<FilterItem>> getBrandItemLiveData() {
        return brandItemLiveData;
    }
}
