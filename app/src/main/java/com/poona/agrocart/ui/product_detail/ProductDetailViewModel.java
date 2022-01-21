package com.poona.agrocart.ui.product_detail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import com.poona.agrocart.ui.product_detail.model.ProductDetail;

import java.util.ArrayList;

public class ProductDetailViewModel extends AndroidViewModel {
    public MutableLiveData<ProductDetail> productDetailMutableLiveData;
    public MutableLiveData<ArrayList<Product>> similarProductLiveData;

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
        productDetailMutableLiveData = new MutableLiveData<>();
        similarProductLiveData = new MutableLiveData<>();
        productDetailMutableLiveData.setValue(null);
        initSimilarItem();
    }

    private void initSimilarItem() {
        ArrayList<Product> similarItems = new ArrayList<>();
        Product product = new Product("1", "Bell Pepper Red", "1Kg"
                , "10", "25",
                "https://www.linkpicture.com/q/capsicon.png", "Pune");
        Product product1 = new Product("2", "Ginger", "250 gms"
                , "10",  "140", "https://www.linkpicture.com/q/ginger.png", "Pune");
        product1.setOrganic(true);
        Product product2 = new Product("3", "Bell Pepper Red", "1Kg"
                , "10", "25", "https://www.linkpicture.com/q/capsicon.png", "Pune");
        Product product3 = new Product("4", "Ginger", "500 gms"
                , "10", "280", "https://www.linkpicture.com/q/ginger.png", "Pune");
        Product product4 = new Product("4", "Ginger", "250gm"
                , "5",  "80", "https://www.linkpicture.com/q/ginger.png", "Pune");
        Product product5 = new Product("4", "Ginger", "1kg"
                , "5", "150", "https://www.linkpicture.com/q/ginger.png", "Pune");
        similarItems.add(product);
        similarItems.add(product1);
        similarItems.add(product2);
        similarItems.add(product3);
        similarItems.add(product4);
        similarItems.add(product5);
        similarProductLiveData.setValue(similarItems);
    }


    public MutableLiveData<ArrayList<Product>> getSimilarProductLiveData() {
        return similarProductLiveData;
    }
}
