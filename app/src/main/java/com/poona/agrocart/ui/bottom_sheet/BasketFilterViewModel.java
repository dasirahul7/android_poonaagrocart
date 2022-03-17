package com.poona.agrocart.ui.bottom_sheet;

import android.app.Application;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.filterResponse.BrandFliterList;
import com.poona.agrocart.data.network.responses.filterResponse.CategoryFilterList;
import com.poona.agrocart.data.network.responses.filterResponse.FilterListResponse;
import com.poona.agrocart.data.network.responses.filterResponse.SortByFilterList;
import com.poona.agrocart.ui.nav_explore.model.FilterItem;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListResponse;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

public class BasketFilterViewModel extends AndroidViewModel {
    public static final String TAG = BasketFilterViewModel.class.getSimpleName();

    private ArrayList<CategoryFilterList> categoryItems;
    private ArrayList<SortByFilterList> filterItems;
    private ArrayList<BrandFliterList> brandItems;
    private final MutableLiveData<ArrayList<CategoryFilterList>> categoryLiveData;
    private final MutableLiveData<ArrayList<SortByFilterList>> filterItemLiveData;
    private final MutableLiveData<ArrayList<BrandFliterList>> brandItemLiveData;

    public BasketFilterViewModel(@NonNull Application application) {
        super(application);
        categoryLiveData = new MutableLiveData<>();
        filterItemLiveData = new MutableLiveData<>();
        brandItemLiveData = new MutableLiveData<>();
        categoryLiveData.setValue(null);
        filterItemLiveData.setValue(null);
        brandItemLiveData.setValue(null);
//        initLists();
    }

    private void initLists() {
        categoryItems = new ArrayList<>();
        filterItems = new ArrayList<>();
        brandItems = new ArrayList<>();


        //Sample categories
//        FilterItem category1 = new FilterItem("1", "Vegetable", false);
//        FilterItem category2 = new FilterItem("2", "Leafy Vegetables", false);
//        FilterItem category3 = new FilterItem("3", "Herbs", false);
//        FilterItem category4 = new FilterItem("4", "Fruits", false);
//        FilterItem category5 = new FilterItem("5", "Dry Fruits", false);
//        FilterItem category6 = new FilterItem("6", "Sprouts", false);
//
//        categoryItems.add(category1);
//        categoryItems.add(category2);
//        categoryItems.add(category3);
//        categoryItems.add(category4);
//        categoryItems.add(category5);
//
//        //Sample Sort By Filter
//        FilterItem filterItem1 = new FilterItem("1", "Low to High", false);
//        FilterItem filterItem2 = new FilterItem("2", "High to Low", false);
//        FilterItem filterItem3 = new FilterItem("3", "Newest Arrived", false);
//        filterItems.add(filterItem1);
//        filterItems.add(filterItem2);
//        filterItems.add(filterItem3);
//        filterItems.add(filterItem3);
//        filterItems.add(filterItem3);
//
//        // sample Brand Items
//
//        FilterItem brand1 = new FilterItem("1", "B&G Green", false);
//        FilterItem brand2 = new FilterItem("2", "Del Monte", false);
//        FilterItem brand3 = new FilterItem("3", "Fruttella", false);
//        brandItems.add(brand1);
//        brandItems.add(brand2);
//        brandItems.add(brand3);
//        brandItems.add(brand3);
//
//        // set LiveData values
//        categoryLiveData.setValue(categoryItems);
//        filterItemLiveData.setValue(filterItems);
//        brandItemLiveData.setValue(brandItems);
    }

    public  void iniFiltersList(FilterListResponse filterListResponse){
         if (filterListResponse.getData().getCategoryList()!=null
         && filterListResponse.getData().getCategoryList().size()>0)
        categoryLiveData.setValue(filterListResponse.getData().getCategoryList());
        if (filterListResponse.getData().getPriceFilterList()!=null
                && filterListResponse.getData().getPriceFilterList().size()>0)
        filterItemLiveData.setValue(filterListResponse.getData().getPriceFilterList());
        if (filterListResponse.getData().getBrandList()!=null
                && filterListResponse.getData().getBrandList().size()>0)
        brandItemLiveData.setValue(filterListResponse.getData().getBrandList());
    }



    public MutableLiveData<ArrayList<CategoryFilterList>> getCategoryLiveData() {
        return categoryLiveData;
    }

    public MutableLiveData<ArrayList<SortByFilterList>> getFilterItemLiveData() {
        return filterItemLiveData;
    }

    public MutableLiveData<ArrayList<BrandFliterList>> getBrandItemLiveData() {
        return brandItemLiveData;
    }

    public LiveData<FilterListResponse> getFilterListPesponse(ProgressDialog progressDialog,
                                                              BottomSheetFilterFragment bottomSheetFilterFragment) {
        MutableLiveData<FilterListResponse> filterListResponseMutableLiveData = new MutableLiveData<>();

        ApiClientAuth.getClient(bottomSheetFilterFragment.getContext())
                .create(ApiInterface.class)
                .getFilterListResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<FilterListResponse>() {
                    @Override
                    public void onSuccess(@NonNull FilterListResponse baseResponse) {
                        progressDialog.dismiss();

                        filterListResponseMutableLiveData.setValue(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        FilterListResponse baseResponse = new FilterListResponse();
                        try {
                            baseResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), FilterListResponse.class);

                            filterListResponseMutableLiveData.setValue(baseResponse);
                        } catch (Exception exception) {
                            Log.e(TAG, exception.getMessage());
                            ((NetworkExceptionListener) bottomSheetFilterFragment)
                                    .onNetworkException(0,"");
                        }
                        Log.e(TAG, e.getMessage());
                    }
                });

        return filterListResponseMutableLiveData;
    }
}
