package com.poona.agrocart.ui.nav_stores;

import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.STORE_ID;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentStoreDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_stores.model.store_details.OurStoreViewDataResponse;
import com.poona.agrocart.ui.nav_stores.model.store_details.StoreDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreDetailFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = "StoreDetailFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static StoreDetail storeValue;
    private StoreDetailViewModel storeDetailViewModel;
    private FragmentStoreDetailBinding fragmentStoreDetailBinding;
    private final List<StoreDetail> storeDetails = new ArrayList<>();
    private MapView mapView;
    private GoogleMap gmap;
    private boolean locationPermissionGranted;
    private String strAboutStore, strStoreId = "";
    private ImageView imageView;


    public static StoreDetailFragment newInstance(StoreDetail store) {
        storeValue = store;
        return new StoreDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strStoreId = getArguments().getString(STORE_ID);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        storeDetailViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        fragmentStoreDetailBinding = FragmentStoreDetailBinding.inflate(inflater, container, false);
        fragmentStoreDetailBinding.setLifecycleOwner(this);
        View view = fragmentStoreDetailBinding.getRoot();
        initTitleWithBackBtn(getString(R.string.store_location));
        initViews();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }


//        getLocationPermission();
        mapView = fragmentStoreDetailBinding.mapView;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        if (isConnectingToInternet(context)) {
            /*Call Our Store Detail API here*/
            callStoreDetailsApi(showCircleProgressDialog(context, ""));
        } else
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

        return view;
    }

    private void initViews() {
        imageView = fragmentStoreDetailBinding.imgStore;
        fragmentStoreDetailBinding.setStoreDetailViewModel(storeDetailViewModel);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        gmap.setMinZoomPreference(12);
        LatLng ny = new LatLng(18.5695136, 73.879276);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        gmap.addMarker(new MarkerOptions().position(ny).title("Location"));
//        float zoomLevel = 16.0f; //This goes up to 21
//        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        gmap.animateCamera(CameraUpdateFactory.zoomTo(16), 5000, null);

//        gmap.animateCamera(CameraUpdateFactory.zoomTo(100));
//        gmap.resetMinMaxZoomPreference();
//        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(ny, zoomLevel));

    }

    private void callStoreDetailsApi(ProgressDialog progressDialog) {
        Observer<OurStoreViewDataResponse> ourStoreViewDataResponseObserver = ourStoreViewDataResponse -> {
            if (ourStoreViewDataResponse != null) {
                Log.e("Our Store Details Api ResponseData", new Gson().toJson(ourStoreViewDataResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (ourStoreViewDataResponse.getStatus()) {

                    case STATUS_CODE_200://Record Create/Update Successfully
                        //storeDetails.clear();
                        if (ourStoreViewDataResponse.getStoreDetails() != null &&
                                ourStoreViewDataResponse.getStoreDetails().size() > 0) {
                            storeDetails.addAll(ourStoreViewDataResponse.getStoreDetails());
                            setUi(storeDetails);

                            /*About store in the html*/
                            strAboutStore = storeDetails.get(0).getAboutStore();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                fragmentStoreDetailBinding.tvAboutStore.setText(Html.fromHtml("" + strAboutStore, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                fragmentStoreDetailBinding.tvAboutStore.setText(Html.fromHtml("" + strAboutStore));
                            }
                        }
                        break;
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, ourStoreViewDataResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(ourStoreViewDataResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, ourStoreViewDataResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

        };
        storeDetailViewModel.getOurStoreDetailsResponse(progressDialog, context, ourStoreDetailsInputParameter())
                .observe(getViewLifecycleOwner(), ourStoreViewDataResponseObserver);
    }

    private void setUi(List<StoreDetail> storeDetails) {
        storeDetailViewModel.storeName.setValue(storeDetails.get(0).getStoreName());
        storeDetailViewModel.storeArea.setValue(storeDetails.get(0).getAreaName());
        storeDetailViewModel.storeCity.setValue(storeDetails.get(0).getCityName());
        storeDetailViewModel.aboutStore.setValue(storeDetails.get(0).getAboutStore());
        storeDetailViewModel.contactPersonalNumber.setValue(storeDetails.get(0).getMobileNo());
        storeDetailViewModel.personalAddress.setValue(storeDetails.get(0).getAddress());

        if (storeDetails.get(0).getStoreImage() != null) {
            Glide.with(context)
                    .load(IMAGE_DOC_BASE_URL + storeDetails.get(0).getStoreImage())
                    .placeholder(R.drawable.empty_cart_img)
                    .error(R.drawable.empty_cart_img)
                    .into(imageView);
        }

    }

    private HashMap<String, String> ourStoreDetailsInputParameter() {
        HashMap<String, String> map = new HashMap<>();
        map.put(STORE_ID, strStoreId);
        return map;
    }

}