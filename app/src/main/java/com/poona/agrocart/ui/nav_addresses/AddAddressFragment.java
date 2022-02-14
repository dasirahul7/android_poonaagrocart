package com.poona.agrocart.ui.nav_addresses;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentAddressesFormBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_addresses.map_view.SimplePlacePicker;
import com.poona.agrocart.ui.sign_in.SignInViewModel;

public class AddAddressFragment extends BaseFragment implements View.OnClickListener {
    private FragmentAddressesFormBinding fragmentAddressesFormBinding;
    private AddressesViewModel addressesViewModel;
    private final String[] cities={"Pune"};
    private final String[] areas={"Vishrantwadi", "Khadki"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAddressesFormBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_addresses_form, container, false);

        addressesViewModel = new ViewModelProvider(this).get(AddressesViewModel.class);
        fragmentAddressesFormBinding.setAddressesViewModel(addressesViewModel);

        fragmentAddressesFormBinding.setLifecycleOwner(this);
        final View view = fragmentAddressesFormBinding.getRoot();

        initView();
        setupSpinners();

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        fragmentAddressesFormBinding.spinnerCity.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,areas);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        fragmentAddressesFormBinding.spinnerArea.setAdapter(arrayAdapter);
    }

    private void initView() {
        fragmentAddressesFormBinding.tvCurrentLocation.setOnClickListener(this);

        Typeface poppinsRegularFont = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_medium));
        fragmentAddressesFormBinding.rbHome.setTypeface(poppinsRegularFont);
        fragmentAddressesFormBinding.rbOffice.setTypeface(poppinsRegularFont);
        fragmentAddressesFormBinding.rbOther.setTypeface(poppinsRegularFont);

        initTitleWithBackBtn(getString(R.string.addresses_form));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_current_location:
                if (isConnectingToInternet(context))
                    gotoGoogleMapAddressPicker();
                else
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                break;
        }
    }

    /*
    * get address from google map start
    * */
    private String selectedAddressFromGoogleMap = "";
    private String selectedStateFromGoogleMap = "";
    private String selectedCityFromGoogleMap = "";
    private String selectedAreaFromGoogleMap = "";
    private String latitude = "";
    private String longitude = "";

    private void updateUi(Intent data) {
        selectedAddressFromGoogleMap = data.getStringExtra(SimplePlacePicker.SELECTED_ADDRESS);
        selectedStateFromGoogleMap = data.getStringExtra(SimplePlacePicker.SELECTED_STATE);
        selectedCityFromGoogleMap = data.getStringExtra(SimplePlacePicker.SELECTED_CITY);
        selectedAreaFromGoogleMap = data.getStringExtra(SimplePlacePicker.SELECTED_AREA);
        latitude = String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA,-1));
        longitude = String.valueOf(data.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA,-1));

        if(selectedAddressFromGoogleMap == null
                || TextUtils.isEmpty(selectedAddressFromGoogleMap)) {
            selectedAddressFromGoogleMap = "";
        } else {

        }

        if(selectedStateFromGoogleMap == null
                || TextUtils.isEmpty(selectedStateFromGoogleMap)) {
            selectedStateFromGoogleMap = "";
        } else {

        }

        if(selectedCityFromGoogleMap == null
                || TextUtils.isEmpty(selectedCityFromGoogleMap)) {
            selectedCityFromGoogleMap = "";
        } else {

        }

        if(selectedAreaFromGoogleMap == null
                || TextUtils.isEmpty(selectedAreaFromGoogleMap)) {
            selectedAreaFromGoogleMap = "";
        } else {

        }

        if(latitude == null
                || TextUtils.isEmpty(latitude)
                || latitude.equals("-1")) {
            latitude = "";
        } else {

        }

        if(longitude == null
                || TextUtils.isEmpty(longitude)
                || longitude.equals("-1")) {
            longitude = "";
        } else {

        }
    }

    private String [] countryListIso = {"eg","sau","om","mar","usa","ind"};
    private String [] addressLanguageList = {"en","ar"};
    private void selectLocationOnMap() {
        //String apiKey = "AIzaSyDHYoOXbCOt_AgEbUXbj0DSowKgHFwtdGA"; //

        String apiKey = "";

        if(preferences.getGoogleApiKey() != null && !TextUtils.isEmpty(preferences.getGoogleApiKey()))
            apiKey = preferences.getGoogleApiKey(); //dynamic
        else
            apiKey = "AIzaSyDHYoOXbCOt_AgEbUXbj0DSowKgHFwtdGA"; // static

        String mCountry = countryListIso[5]; // countries
        String mLanguage = addressLanguageList[0]; // english/arabic
        String [] mSupportedAreas = {""} /*fragmentRegisterBinding.etShopAddress.getText().toString().split(",")*/;
        startMapActivity(apiKey,mCountry,mLanguage,mSupportedAreas);
    }

    private void gotoGoogleMapAddressPicker() {
        // You must grant user permission for access device location first
        // please don't ignore this step >> Ignoring location permission may cause application to crash !
        if (hasPermissionInManifest(requireActivity(), 1, Manifest.permission.ACCESS_FINE_LOCATION))
            selectLocationOnMap();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null) {
                updateUi(data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectLocationOnMap();
        }
    }

    //check for location permission
    public static boolean hasPermissionInManifest(Activity activity, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(activity, permissionName) != PackageManager.PERMISSION_GRANTED)
        {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(activity,
                    new String[]{permissionName},
                    requestCode);
        } else {
            return true;
        }
        return false;
    }
    /*
     * get address from google map end
     * */
}