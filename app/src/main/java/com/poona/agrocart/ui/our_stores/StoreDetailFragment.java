package com.poona.agrocart.ui.our_stores;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.poona.agrocart.R;
import com.poona.agrocart.app.PoonaAgroCartApplication;
import com.poona.agrocart.databinding.FragmentStoreDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.our_stores.model.Store;

import java.util.Objects;

public class StoreDetailFragment extends BaseFragment implements OnMapReadyCallback {

    private static final String TAG = "StoreDetailFragment";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private StoreDetailViewModel mViewModel;
    private FragmentStoreDetailBinding storeBinding;
    private static Store storeValue;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mapView;
    private GoogleMap gmap;
    private boolean locationPermissionGranted;

    public static StoreDetailFragment newInstance(Store store) {
        storeValue = store;
        return new StoreDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        storeBinding = FragmentStoreDetailBinding.inflate(inflater,container,false);
        storeBinding.setLifecycleOwner(this);
        View root = storeBinding.getRoot();
        initTitleWithBackBtn(getString(R.string.store_location));
        initViews();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
//        getLocationPermission();
        mapView = storeBinding.mapView;
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        return root;
    }

    private void initViews() {
        mViewModel.storeMutableLiveData.setValue(storeValue);
        Log.d(TAG, "initViews: "+mViewModel.storeMutableLiveData.getValue().getName());
        storeBinding.setStoreDetailViewModel(mViewModel);
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
}