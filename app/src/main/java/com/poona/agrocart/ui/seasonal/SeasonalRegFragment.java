package com.poona.agrocart.ui.seasonal;

import static com.poona.agrocart.app.AppConstants.SEASONAL_P_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

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
import com.poona.agrocart.ui.product_detail.adapter.ProductImagesAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeasonalRegFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeasonalRegFragment extends BaseFragment implements NetworkExceptionListener {

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
    private String selectedUnitId, selectedUnitName = "";
    private String seasonal_id;

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
            if (isConnectingToInternet(context)) {
                if (isValidSeasonalReg())

                        callSeasonalProductRegistrationAPI(showCircleProgressDialog(context, ""));

            } else showNotifyAlert(requireActivity(), context.getString(R.string.info),
                    context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        });
        return seasonRoot;
    }

    private boolean isValidSeasonalReg() {

        if (TextUtils.isEmpty(fragmentSeasonalRegBinding.etUsername.getText().toString())) {
//            infoToast(context,"name can't be Empty");
            fragmentSeasonalRegBinding.etUsername.setError("enter name");
            fragmentSeasonalRegBinding.etUsername.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(fragmentSeasonalRegBinding.etAddress.toString())) {
            fragmentSeasonalRegBinding.etAddress.setError("enter address");
            fragmentSeasonalRegBinding.etAddress.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(fragmentSeasonalRegBinding.etMobileNo.toString())) {
            fragmentSeasonalRegBinding.etMobileNo.setError("enter mobile");
            fragmentSeasonalRegBinding.etMobileNo.requestFocus();
            return false;
        } else if (!selectedUnitName.equalsIgnoreCase("Select unit")) {
            fragmentSeasonalRegBinding.etMobileNo.setError("Select Unit");
            fragmentSeasonalRegBinding.spinnerType.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(fragmentSeasonalRegBinding.etQuantity.getText().toString())) {
            fragmentSeasonalRegBinding.etQuantity.setError("enter quantity");
            fragmentSeasonalRegBinding.etQuantity.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(fragmentSeasonalRegBinding.etAddress.getText().toString())) {
            fragmentSeasonalRegBinding.etAddress.setError("enter Address");
            fragmentSeasonalRegBinding.etAddress.requestFocus();
            return false;
        } else return true;
    }

    private void setUnitAdapter() {
        seasonalViewModel.arrayListUnitLiveData.observe(getViewLifecycleOwner(), unitData -> {
            UnitAdaptor unitAdaptor = new UnitAdaptor(context, unitData);
            unitSpinner.setAdapter(unitAdaptor);
            unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    hideKeyBoard(requireActivity());
                    selectedUnitId = unitData.get(i).getId();
                    selectedUnitName = unitData.get(i).getUnitName();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    hideKeyBoard(requireActivity());
                }
            });

        });
    }

    /*Call unit api here*/
    private void callGetUnitApi(ProgressDialog progressDialog) {
        ApiClientAuth.getClient(context)
                .create(ApiInterface.class)
                .getUnitResponse()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetUnitResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull GetUnitResponse getUnitResponse) {
                        if (getUnitResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            seasonalViewModel.arrayListUnitLiveData.setValue(getUnitResponse.getUnitList());
                            setUnitAdapter();
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        ((NetworkExceptionListener) context).onNetworkException(1, "");
                    }
                });
    }

    /*Call Seasonal Registration API*/
    private void callSeasonalProductRegistrationAPI(ProgressDialog progressDialog) {
        Observer<BaseResponse> baseResponseObserver = response -> {
            if (response != null) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                successToast(context,response.getMessage());
                switch (response.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, response.getMessage());
                        Navigation.findNavController(seasonRoot).popBackStack();
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors

                        warningToast(context, response.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(response.getMessage(), context);
                        break;
                    case STATUS_CODE_402://Unauthorized user
                        goToAskAndDismiss(response.getMessage(),context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, response.getMessage());
                        break;

                }
            }
        };
        seasonalViewModel.seasonalProductRegistrationResponse(progressDialog, regSeasonalParam(),
                SeasonalRegFragment.this).observe(getViewLifecycleOwner(), baseResponseObserver);
    }

    /*Call seasonal product details API*/
    private void callSeasonalDetailsAPI(ProgressDialog progressDialog) {
        Observer<SeasonalProductResponse> seasonalProductResponseObserver = seasonalProductResponse -> {
            if (seasonalProductResponse!=null){
                if (progressDialog!=null)
                    progressDialog.dismiss();
                switch (seasonalProductResponse.getStatus()){
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (seasonalProductResponse.getSeasonalProducts().size()>0){
                            fragmentSeasonalRegBinding.itemLayout.setVisibility(View.VISIBLE);
                            SeasonalProduct seasonalProduct = seasonalProductResponse.getSeasonalProducts().get(0);
                            seasonalViewModel.productNameMutable.setValue(seasonalProduct.getSeasonalProductName());
                            seasonalViewModel.productImageMutable.setValue(seasonalProduct.getProductAdsAmage());
                            seasonalViewModel.productDetailMutable.setValue(seasonalProduct.getProductDetails());
                            fragmentSeasonalRegBinding.setSeasonalModule(seasonalViewModel);
                            fragmentSeasonalRegBinding.setVariable(BR.seasonalModule,seasonalViewModel);
                            fragmentSeasonalRegBinding.executePendingBindings();
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors

                        warningToast(context, seasonalProductResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(seasonalProductResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_402://Unauthorized user
                        goToAskAndDismiss(seasonalProductResponse.getMessage(),context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, seasonalProductResponse.getMessage());
                        break;
                }
            }
        };
        seasonalViewModel.getSeasonalResponseLiveData(progressDialog, seasonalParam(),SeasonalRegFragment.this)
                .observe(getViewLifecycleOwner(),seasonalProductResponseObserver);
    }

    private HashMap<String, String> seasonalParam() {
        HashMap<String,String> map = new HashMap<>();
        map.put(SEASONAL_P_ID, getArguments().getString(SEASONAL_P_ID));
        return map;
    }

    private HashMap<String, String> regSeasonalParam() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.SEASONAL_P_ID, seasonal_id);
        map.put(AppConstants.NAME, fragmentSeasonalRegBinding.etUsername.getText().toString());
        map.put(AppConstants.MOBILE, fragmentSeasonalRegBinding.etMobileNo.getText().toString());
        map.put(AppConstants.QUANTITY, fragmentSeasonalRegBinding.etQuantity.getText().toString());
        map.put(AppConstants.UNIT_ID, selectedUnitId);
        map.put(AppConstants.ADDRESS, fragmentSeasonalRegBinding.etAddress.getText().toString().trim());
        return map;
    }

    private void initView() {
        if (getArguments().getString(SEASONAL_P_ID)!=null) {
            seasonal_id = getArguments().getString(SEASONAL_P_ID);
        }
        if (isConnectingToInternet(context)){
            fragmentSeasonalRegBinding.itemLayout.setVisibility(View.GONE);
            callSeasonalDetailsAPI(showCircleProgressDialog(context,""));
        }else showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        fragmentSeasonalRegBinding.etMobileNo.setText(preferences.getUserMobile());
        fragmentSeasonalRegBinding.etAddress.setText(preferences.getUserAddress());
//        vpImages = fragmentSeasonalRegBinding.vpProductImages;
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
        if (count > 0) {
            productImagesAdapter = new ProductImagesAdapter(SeasonalRegFragment.this,
                    getChildFragmentManager(), images, 1);
            vpImages.setAdapter(productImagesAdapter);
            productImagesAdapter.notifyDataSetChanged();
            vpImages.addOnPageChangeListener(productImagesAdapter);
            dotsIndicator.setViewPager(vpImages);
        }
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), SeasonalRegFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from){
                    case 0:
                        break;
                    /*call seasonal product detail*/
                    case 1:
                        /*call all unit*/
                        callGetUnitApi(showCircleProgressDialog(context,""));
                        break;
                    case 2:
                        /*call seasonal product regostration*/
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}