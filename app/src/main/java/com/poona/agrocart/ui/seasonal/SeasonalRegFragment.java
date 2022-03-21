package com.poona.agrocart.ui.seasonal;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.GetUnitResponse;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;
import com.poona.agrocart.data.network.responses.homeResponse.SeasonalProduct;
import com.poona.agrocart.databinding.FragmentSeasonalRegBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.product_detail.ProductDetailFragment;
import com.poona.agrocart.ui.product_detail.adapter.ProductImagesAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeasonalRegFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonalRegFragment extends BaseFragment {

    private static final String IMAGE = "image";
    public int count = 0;
    public ViewPager vpImages;
    private FragmentSeasonalRegBinding fragmentSeasonalRegBinding;
    private SeasonalViewModel seasonalViewModel;
    private View seasonRoot;
    private SeasonalProduct seasonalProduct;
    private ProductImagesAdapter productImagesAdapter;
    private DotsIndicator dotsIndicator;
    private String image;
    private ArrayList<String> images;
    private Button btnRegister;
    private Spinner unitSpinner;
    private String selectedUnitId;

    public static SeasonalRegFragment newInstance(String param1, String param2) {
        SeasonalRegFragment fragment = new SeasonalRegFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getString(IMAGE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSeasonalRegBinding = FragmentSeasonalRegBinding.inflate(getLayoutInflater());
        seasonalViewModel = new ViewModelProvider(this).get(SeasonalViewModel.class);
        seasonRoot = fragmentSeasonalRegBinding.getRoot();
        initView();

        /*Call unit API here*/
        if (isConnectingToInternet(context)){
            callGetUnitApi(showCircleProgressDialog(context,""));
        }else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

        btnRegister.setOnClickListener(view -> {
            if (isConnectingToInternet(context)){
                callSeasonalProductRegistrationAPI(showCircleProgressDialog(context,""));
            }else showNotifyAlert(requireActivity(), context.getString(R.string.info),
                    context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        });
        return seasonRoot;
    }

    private void setUnitAdapter() {
        seasonalViewModel.arrayListUnitLiveData.observe(getViewLifecycleOwner(),unitData -> {
            UnitAdaptor unitAdaptor = new UnitAdaptor(context,unitData);
            unitSpinner.setAdapter(unitAdaptor);
            unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    hideKeyBoard(requireActivity());
                    selectedUnitId = unitData.get(i).getId();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    hideKeyBoard(requireActivity());
                }
            });

        });
    }

    /*Call unit api here*/
    private void callGetUnitApi(ProgressDialog progressDialog){
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getUnitResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetUnitResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull GetUnitResponse getUnitResponse) {
                        if (getUnitResponse!=null){
                            if (progressDialog!=null)
                                progressDialog.dismiss();
                            seasonalViewModel.arrayListUnitLiveData.setValue(getUnitResponse.getUnitList());
                            setUnitAdapter();
                        }
                    }
                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        ((NetworkExceptionListener) context).onNetworkException(0,"");
                    }
                });
    }

    /*Call Seasonal Registration API*/
    private void callSeasonalProductRegistrationAPI(ProgressDialog progressDialog) {
        Observer<BaseResponse> baseResponseObserver = response -> {

        };
        seasonalViewModel.seasonalProductRegistrationResponse(progressDialog,regSeasonalParam(),
                SeasonalRegFragment.this).observe(getViewLifecycleOwner(),baseResponseObserver);
    }

    private HashMap<String, String> regSeasonalParam() {
        HashMap<String,String> map = new HashMap<>();
        map.put(AppConstants.SEASONAL_P_ID,seasonalProduct.getId());
        map.put(AppConstants.NAME,fragmentSeasonalRegBinding.etUsername.getText().toString().trim());
        map.put(AppConstants.MOBILE,fragmentSeasonalRegBinding.etMobileNo.getText().toString().trim());
        map.put(AppConstants.QUANTITY,"1");
//        map.put(AppConstants.UNIT_ID,seasonalProduct.ge);
        return map;
    }

    private void initView() {
        fragmentSeasonalRegBinding.etMobileNo.setHint(preferences.getUserMobile());
        fragmentSeasonalRegBinding.etAddress.setHint(preferences.getUserAddress());
        vpImages = fragmentSeasonalRegBinding.vpProductImages;
        dotsIndicator = fragmentSeasonalRegBinding.dotsIndicator;
        btnRegister = fragmentSeasonalRegBinding.btnRegister;
        unitSpinner = fragmentSeasonalRegBinding.spinnerType;
        initTitleWithBackBtn("Seasonal Product Registration");
       images = new ArrayList<>();
        seasonalProduct = new SeasonalProduct();
//        for (int i = 0; i < 3; i++)
//            images.add(getArguments() != null ? image: null);
//        seasonalProduct.setSeasProductImages(images);
//        setViewPagerAdapterItems();
    }

    /*Set images here*/
    private void setViewPagerAdapterItems() {

        count = images.size();
        if (count>0){
            productImagesAdapter = new ProductImagesAdapter(SeasonalRegFragment.this,
                    getChildFragmentManager(), images,1);
            vpImages.setAdapter(productImagesAdapter);
            productImagesAdapter.notifyDataSetChanged();
            vpImages.addOnPageChangeListener(productImagesAdapter);
            dotsIndicator.setViewPager(vpImages);
        }
    }

}