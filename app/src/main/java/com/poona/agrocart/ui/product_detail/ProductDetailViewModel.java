package com.poona.agrocart.ui.product_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.ui.home.model.ProductOld;
import com.poona.agrocart.ui.product_detail.model.ProductDetail;

import java.util.ArrayList;

public class ProductDetailViewModel extends AndroidViewModel {
    public MutableLiveData<ProductDetail> productDetailMutableLiveData;
    public MutableLiveData<ArrayList<ProductOld>> similarProductLiveData;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        productDetailMutableLiveData = new MutableLiveData<>();
        similarProductLiveData = new MutableLiveData<>();
        productDetailMutableLiveData.setValue(null);
        initSimilarItem();
    }

    private void initSimilarItem() {
        ArrayList<ProductOld> similarItems = new ArrayList<>();
        ProductOld productOld = new ProductOld("1", "Bell Pepper Red", "1Kg"
                , "10", "25",
                "https://www.linkpicture.com/q/capsicon.png", "Pune");
        ProductOld productOld1 = new ProductOld("2", "Ginger", "250 gms"
                , "10",  "140", "https://www.linkpicture.com/q/ginger.png", "Pune");
        productOld1.setOrganic(true);
        ProductOld productOld2 = new ProductOld("3", "Bell Pepper Red", "1Kg"
                , "10", "25", "https://www.linkpicture.com/q/capsicon.png", "Pune");
        ProductOld productOld3 = new ProductOld("4", "Ginger", "500 gms"
                , "10", "280", "https://www.linkpicture.com/q/ginger.png", "Pune");
        ProductOld productOld4 = new ProductOld("4", "Ginger", "250gm"
                , "5",  "80", "https://www.linkpicture.com/q/ginger.png", "Pune");
        ProductOld productOld5 = new ProductOld("4", "Ginger", "1kg"
                , "5", "150", "https://www.linkpicture.com/q/ginger.png", "Pune");
        similarItems.add(productOld);
        similarItems.add(productOld1);
        similarItems.add(productOld2);
        similarItems.add(productOld3);
        similarItems.add(productOld4);
        similarItems.add(productOld5);
        similarProductLiveData.setValue(similarItems);
    }


    public MutableLiveData<ArrayList<ProductOld>> getSimilarProductLiveData() {
        return similarProductLiveData;
    }
}
