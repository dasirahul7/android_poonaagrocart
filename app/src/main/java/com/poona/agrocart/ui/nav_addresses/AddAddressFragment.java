package com.poona.agrocart.ui.nav_addresses;

import static android.app.Activity.RESULT_OK;
import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.CITY;

import static com.poona.agrocart.app.AppConstants.ADDRESS_TYPE;
import static com.poona.agrocart.app.AppConstants.ALTERNATE_MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.APARTMENT_NAME;
import static com.poona.agrocart.app.AppConstants.AREA_;
import static com.poona.agrocart.app.AppConstants.AREA_ID;
import static com.poona.agrocart.app.AppConstants.CITY_;
import static com.poona.agrocart.app.AppConstants.CITY_ID;
import static com.poona.agrocart.app.AppConstants.DATE_OF_BIRTH;
import static com.poona.agrocart.app.AppConstants.EMAIL;
import static com.poona.agrocart.app.AppConstants.GENDER;
import static com.poona.agrocart.app.AppConstants.GOOGLE_MAP_ADDRESS;
import static com.poona.agrocart.app.AppConstants.HOUSE_NO;
import static com.poona.agrocart.app.AppConstants.LANDMARK;
import static com.poona.agrocart.app.AppConstants.LATITUDE;
import static com.poona.agrocart.app.AppConstants.LONGITUDE;
import static com.poona.agrocart.app.AppConstants.MOBILE;
import static com.poona.agrocart.app.AppConstants.MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.NAME;
import static com.poona.agrocart.app.AppConstants.PIN_CODE;
import static com.poona.agrocart.app.AppConstants.PROFILE_IMAGE;
import static com.poona.agrocart.app.AppConstants.STATE_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.STREET;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.databinding.FragmentAddressesFormBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.nav_addresses.map_view.SimplePlacePicker;
import com.poona.agrocart.ui.nav_profile.CustomArrayAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddAddressFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = AddAddressFragment.class.getSimpleName();
    private FragmentAddressesFormBinding fragmentAddressesFormBinding;
    private AddressesViewModel addressesViewModel;

    private View view;

    private List<BasicDetails> cityList;
    private List<BasicDetails> areaList;

    private BasicDetails basicDetails;
    private String selectedCityId = "0";
    private String selectedAreaId = "0";
    private String selectedCity, selectedArea;

    private int check = 0;
    private CityResponse cityResponse = null;
    private AreaResponse areaResponse = null;

    private boolean checkIsValidPinCode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAddressesFormBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_addresses_form, container, false);

        addressesViewModel = new ViewModelProvider(this).get(AddressesViewModel.class);
        fragmentAddressesFormBinding.setAddressesViewModel(addressesViewModel);

        fragmentAddressesFormBinding.setLifecycleOwner(this);
        view = fragmentAddressesFormBinding.getRoot();

        initView();

        basicDetails = new BasicDetails();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();

        if (isConnectingToInternet(context)) {
            hideKeyBoard(requireActivity());
            callCityApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        return view;
    }

    private void initView() {
        fragmentAddressesFormBinding.tvCurrentLocation.setOnClickListener(this);
        fragmentAddressesFormBinding.btnAddAddress.setOnClickListener(this);
        fragmentAddressesFormBinding.btCheckAvailability.setOnClickListener(this);

        Typeface poppinsRegularFont = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_medium));
        fragmentAddressesFormBinding.rbHome.setTypeface(poppinsRegularFont);
        fragmentAddressesFormBinding.rbOffice.setTypeface(poppinsRegularFont);
        fragmentAddressesFormBinding.rbOther.setTypeface(poppinsRegularFont);

        initTitleWithBackBtn(getString(R.string.addresses_form));

        fragmentAddressesFormBinding.rgAddressesType.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.rb_home:
                    addressesViewModel.addressType.setValue("home");
                    break;
                case R.id.rb_office:
                    addressesViewModel.addressType.setValue("office");
                    break;
                case R.id.rb_other:
                    addressesViewModel.addressType.setValue("other");
                    break;
            }
        });
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
            case R.id.btn_add_address:
                getUserInputAndSetIntoPojo();
                checkValidInputFields();
                break;
            case R.id.bt_check_availability:
                getUserInputAndSetIntoPojo();
                checkValidPinCodeEntered();
                break;
        }
    }

    private void setupCitySpinner() {
        BasicDetails basicDetails2 = new BasicDetails();
        basicDetails2.setId("0");
        basicDetails2.setName("Select");
        cityList.add(basicDetails2);

        if(cityResponse != null && cityResponse.getCities() != null && cityResponse.getCities().size() > 0) {
            for(int i = 0; i < cityResponse.getCities().size(); i++) {
                BasicDetails basicDetails = new BasicDetails();
                basicDetails.setId(cityResponse.getCities().get(i).getId());
                basicDetails.setName(cityResponse.getCities().get(i).getCityName());
                cityList.add(basicDetails);
            }
        }

        CustomArrayAdapter cityArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, cityList);
        fragmentAddressesFormBinding.spinnerCity.setAdapter(cityArrayAdapter);

        fragmentAddressesFormBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*basicDetails.setCity(adapterView.getItemAtPosition(i).toString());
                addressesViewModel.city.setValue(basicDetails.getCity());*/
                if (cityList != null) {
                    System.out.println("selected city " + cityList.get(i).getId());
                    selectedCityId = cityList.get(i).getId();
                    selectedCity = cityList.get(i).getName();

                    if(++check > 1)
                        callAreaApi(showCircleProgressDialog(context, ""));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*for first time*/
        setupAreaSpinner();
    }

    private void setupAreaSpinner() {
        if(areaList != null && areaList.size() > 0) {
            areaList.clear();
        }

        BasicDetails basicDetails3 = new BasicDetails();
        basicDetails3.setId("0");
        basicDetails3.setName("Select");
        areaList.add(basicDetails3);

        if(areaResponse != null && areaResponse.getAreas() != null && areaResponse.getAreas().size() > 0) {
            for(int i = 0; i < areaResponse.getAreas().size(); i++) {
                BasicDetails basicDetails = new BasicDetails();
                basicDetails.setId(areaResponse.getAreas().get(i).getId());
                basicDetails.setName(areaResponse.getAreas().get(i).getAreaName());
                areaList.add(basicDetails);
            }
        }

        CustomArrayAdapter areaArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, areaList);
        fragmentAddressesFormBinding.spinnerArea.setAdapter(areaArrayAdapter);

        fragmentAddressesFormBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*basicDetails.setArea(adapterView.getItemAtPosition(i).toString());
                addressesViewModel.area.setValue(basicDetails.getArea());*/
                if (areaList != null) {
                    System.out.println("selected area " + areaList.get(i).getId());
                    selectedAreaId = areaList.get(i).getId();
                    selectedArea = areaList.get(i).getName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /*City API*/
    private void callCityApi(ProgressDialog showCircleProgressDialog) {
        Observer<CityResponse> cityResponseObserver = cityResponse -> {
            if (cityResponse != null) {
                switch (cityResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (cityResponse.getStatus() == 200) {
                            if (cityResponse.getCities() != null) {
                                if (cityResponse.getCities().size() > 0) {
                                    this.cityResponse = cityResponse;
                                    setupCitySpinner();
                                }

                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, cityResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(cityResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, cityResponse.getMessage());
                        break;
                }

            }
        };
        addressesViewModel.getCityResponse(showCircleProgressDialog, AddAddressFragment.this)
                .observe(getViewLifecycleOwner(), cityResponseObserver);
    }

    /* Area API*/
    private void callAreaApi(ProgressDialog showCircleProgressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : getAreaParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        Observer<AreaResponse> areaResponseObserver = areaResponse -> {
            if (areaResponse != null) {
                showCircleProgressDialog.dismiss();
                switch (areaResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (areaResponse.getStatus() == 200) {
                            if (areaResponse.getAreas() != null) {
                                if (areaResponse.getAreas().size() > 0) {
                                    this.areaResponse = areaResponse;
                                    setupAreaSpinner();
                                }
                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://No records found
                        this.areaResponse = null;
                        setupAreaSpinner();
                        warningToast(context, areaResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(areaResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, areaResponse.getMessage());
                        break;
                }

            }
        };
        addressesViewModel.getAreaResponse(showCircleProgressDialog,
                AddAddressFragment.this, getAreaParameters())
                .observe(getViewLifecycleOwner(), areaResponseObserver);
    }

    private HashMap<String, String> getAreaParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CITY_ID, selectedCityId);
        return map;
    }

    private void getUserInputAndSetIntoPojo() {
        if(cityList != null && cityList.size() > 0) {
            for(int i = 0; i < cityList.size(); i++) {
                if(cityList.get(i).getName().equals(fragmentAddressesFormBinding.spinnerCity.getSelectedItem().toString())) {
                    addressesViewModel.city.setValue(cityList.get(i).getId());
                }
            }
        }
        if(areaList != null && areaList.size() > 0) {
            for(int i = 0; i < areaList.size(); i++) {
                if(areaList.get(i).getName().equals(fragmentAddressesFormBinding.spinnerArea.getSelectedItem().toString())) {
                    addressesViewModel.area.setValue(areaList.get(i).getId());
                }
            }
        }

        basicDetails.setAddressType(addressesViewModel.addressType.getValue());
        basicDetails.setName(addressesViewModel.name.getValue());
        basicDetails.setMobileNumber(addressesViewModel.mobile.getValue());
        basicDetails.setCity(addressesViewModel.city.getValue());
        basicDetails.setArea(addressesViewModel.area.getValue());
        basicDetails.setPinCode(addressesViewModel.pinCode.getValue());
        basicDetails.setApartmentName(addressesViewModel.apartmentName.getValue());
        basicDetails.setHouseNumber(addressesViewModel.houseNumber.getValue());
        basicDetails.setStreet(addressesViewModel.street.getValue());
        basicDetails.setLandmark(addressesViewModel.landmark.getValue());
    }

    private void checkValidInputFields() {
        int errorCodeAddressType = basicDetails.isValidAddressType();
        int errorCodeName = basicDetails.isValidName();
        int errorCodeMobileNumber = basicDetails.isValidMobileNumber();
        int errorCodeCity = basicDetails.isValidCity();
        int errorCodeArea = basicDetails.isValidArea();
        int errorCodePinCode = basicDetails.isValidPinCode();
        int errorCodeApartmentName = basicDetails.isValidApartmentName();
        int errorCodeHouseNumber = basicDetails.isValidHouseNumber();
        int errorCodeStreet = basicDetails.isValidStreet();
        int errorCodeLandmark = basicDetails.isValidLandmark();

        if(errorCodeAddressType == 0) {
            errorToast(requireActivity(), getString(R.string.address_type_should_not_be_empty));
        } else if(errorCodeName == 0) {
            errorToast(requireActivity(), getString(R.string.name_should_not_be_empty));
        } else if(errorCodeMobileNumber == 0) {
            errorToast(requireActivity(), getString(R.string.mobile_number_should_not_be_empty));
        } else if(errorCodeMobileNumber == 1) {
            errorToast(requireActivity(), getString(R.string.enter_valid_mobile_number));
        } else if(errorCodeCity == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_city));
        } else if(errorCodeArea == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_area));
        } else if(errorCodePinCode == 0) {
            errorToast(requireActivity(), getString(R.string.pin_code_should_not_be_empty));
        } else if(errorCodePinCode == 1) {
            errorToast(requireActivity(), getString(R.string.invalid_pin_code));
        } else if(errorCodeApartmentName == 0) {
            errorToast(requireActivity(), getString(R.string.apartment_name_should_not_be_empty));
        } else if(errorCodeHouseNumber == 0) {
            errorToast(requireActivity(), getString(R.string.number_should_not_be_empty));
        } else if(errorCodeStreet == 0) {
            errorToast(requireActivity(), getString(R.string.street_should_not_be_empty));
        } else if(errorCodeLandmark == 0) {
            errorToast(requireActivity(), getString(R.string.landmark_should_not_be_empty));
        } else {
            callAddAddressApi(showCircleProgressDialog(context, ""));
        }
    }

    private void checkValidPinCodeEntered() {
        int errorCodePinCode = basicDetails.isValidPinCode();
        if(errorCodePinCode == 0) {
            errorToast(requireActivity(), getString(R.string.pin_code_should_not_be_empty));
        } else if(errorCodePinCode == 1) {
            errorToast(requireActivity(), getString(R.string.invalid_pin_code));
        } else {
            if (isConnectingToInternet(context))
                checkPinCodeAvailableApi(showCircleProgressDialog(context, ""));
            else
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void callAddAddressApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : addAddressParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<BaseResponse> updateProfileResponseObserver = profileResponse -> {
            if (profileResponse != null) {
                progressDialog.dismiss();
                Log.e("Add Address Api Response", new Gson().toJson(profileResponse));
                switch (profileResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, ""+profileResponse.getMessage());
                        Navigation.findNavController(view).popBackStack();
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        goToAskAndDismiss(profileResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, profileResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(profileResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, profileResponse.getMessage());
                        break;
                }
            } else {
                progressDialog.dismiss();
            }
        };

        addressesViewModel
                .addAddressResponse(progressDialog, AddAddressFragment.this, addAddressParameters())
                .observe(getViewLifecycleOwner(), updateProfileResponseObserver);
    }

    private HashMap<String, String> addAddressParameters() {
        HashMap<String, String> map = new HashMap<>();

        map.put(ADDRESS_TYPE, basicDetails.getAddressType());
        map.put(NAME, basicDetails.getName());
        map.put(MOBILE, basicDetails.getMobileNumber());
        map.put(CITY_, basicDetails.getCity());
        map.put(AREA_, basicDetails.getArea());
        map.put(PIN_CODE, basicDetails.getPinCode());
        map.put(APARTMENT_NAME, basicDetails.getApartmentName());
        map.put(HOUSE_NO, basicDetails.getHouseNumber());
        map.put(STREET, basicDetails.getStreet());
        map.put(LANDMARK, basicDetails.getLandmark());
        /*map.put(LATITUDE, basicDetails.getLatitude());
        map.put(LONGITUDE, basicDetails.getLongitude());
        map.put(GOOGLE_MAP_ADDRESS, basicDetails.getMapAddress());*/

        return map;
    }

    private void checkPinCodeAvailableApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : checkPinCodeAvailableParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<BaseResponse> updateProfileResponseObserver = baseResponse -> {
            if (baseResponse != null) {
                progressDialog.dismiss();
                Log.e("Check Pin Code Api Response", new Gson().toJson(baseResponse));
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, ""+baseResponse.getMessage());
                        checkIsValidPinCode = true;
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        checkIsValidPinCode = false;
                        goToAskAndDismiss(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        checkIsValidPinCode = true;
                        warningToast(context, baseResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, baseResponse.getMessage());
                        break;
                }
            } else {
                progressDialog.dismiss();
            }
        };

        addressesViewModel
                .checkPinCodeAvailableResponse(progressDialog, AddAddressFragment.this, checkPinCodeAvailableParameters())
                .observe(getViewLifecycleOwner(), updateProfileResponseObserver);
    }

    private HashMap<String, String> checkPinCodeAvailableParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(PIN_CODE, basicDetails.getPinCode());
        return map;
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
        String apiKey = "";

        if(preferences.getGoogleApiKey() != null && !TextUtils.isEmpty(preferences.getGoogleApiKey()))
            apiKey = preferences.getGoogleApiKey(); //dynamic
        else
            apiKey = "AIzaSyDi2KxP5vuvOxYO1qA2i5Ehx-yXY7hcN1Q"; // static

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
        if (requestCode == SimplePlacePicker.SELECT_LOCATION_REQUEST_CODE
                && resultCode == RESULT_OK){
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

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), AddAddressFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callCityApi(showCircleProgressDialog(context, ""));
                } else if(from == 1) {
                    callAreaApi(showCircleProgressDialog(context, ""));
                } else if(from == 2) {
                    callAddAddressApi(showCircleProgressDialog(context, ""));
                } else if(from == 3) {
                    checkPinCodeAvailableApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}