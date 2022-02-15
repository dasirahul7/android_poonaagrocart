package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.app.AppConstants.AREA_ID;
import static com.poona.agrocart.app.AppConstants.CITY_ID;
import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.databinding.FragmentSelectLocationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;
import com.poona.agrocart.ui.nav_profile.CustomArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SelectLocationFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = SelectLocationFragment.class.getSimpleName();
    private FragmentSelectLocationBinding fragmentSelectLocationBinding;

    private List<BasicDetails> cityList;
    private List<BasicDetails> areaList;

    private BasicDetails basicDetails;
    private CommonViewModel commonViewModel;
    private SelectLocationViewModel selectLocationViewModel;

    private String selectedCityId = "0";
    private String selectedAreaId = "0";
    private String selectedCity, selectedArea;

    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSelectLocationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false);
        fragmentSelectLocationBinding.setLifecycleOwner(this);
        view = fragmentSelectLocationBinding.getRoot();

        initViews(view);

        ivBack.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }

    private void initViews(View view) {
        /*checkGpsStatus();*/
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        selectLocationViewModel = new ViewModelProvider(this).get(SelectLocationViewModel.class);

        fragmentSelectLocationBinding.btnSubmit.setOnClickListener(this);
        fragmentSelectLocationBinding.ivPoonaAgroMainLogo.bringToFront();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        basicDetails = new BasicDetails();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();

        if (isConnectingToInternet(context)) {
            hideKeyBoard(requireActivity());
            callCityApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private int check = 0;
    private CityResponse cityResponse = null;
    private AreaResponse areaResponse = null;
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
        fragmentSelectLocationBinding.spinnerCity.setAdapter(cityArrayAdapter);

        fragmentSelectLocationBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basicDetails.setCity(adapterView.getItemAtPosition(i).toString());
                commonViewModel.city.setValue(basicDetails.getCity());
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
        fragmentSelectLocationBinding.spinnerArea.setAdapter(areaArrayAdapter);

        fragmentSelectLocationBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                basicDetails.setArea(adapterView.getItemAtPosition(i).toString());
                commonViewModel.area.setValue(basicDetails.getArea());
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
        selectLocationViewModel.getCityResponse(showCircleProgressDialog, SelectLocationFragment.this)
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
        selectLocationViewModel.getAreaResponse(showCircleProgressDialog,
                SelectLocationFragment.this, getAreaParameters())
                .observe(getViewLifecycleOwner(), areaResponseObserver);
    }

    private HashMap<String, String> getAreaParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CITY_ID, selectedCityId);
        return map;
    }

    @Override
    public void onClick(View v) {
        if (isConnectingToInternet(context)) {
            hideKeyBoard(requireActivity());
            callUpdateLocationApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    /* Update Location API*/
    private void callUpdateLocationApi(ProgressDialog showCircleProgressDialog) {
        Observer<BaseResponse> updateLocationObserver = updateLocationResponse -> {
            if (updateLocationResponse != null) {
                switch (updateLocationResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (updateLocationResponse.getStatus() == 200) {
                            successToast(context, "" + updateLocationResponse.getMessage());
                            preferences.setUserAddress(selectedArea + ", " + selectedCity);
                            redirectToLoginFragment(view);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        warningToast(context, updateLocationResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(updateLocationResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, updateLocationResponse.getMessage());
                        break;
                }
            }
        };
        selectLocationViewModel.updateLocation(showCircleProgressDialog, updateLocationParams(),
                SelectLocationFragment.this)
                .observe(getViewLifecycleOwner(), updateLocationObserver);
    }

    private HashMap<String, String> updateLocationParams() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CITY_ID, selectedCityId);
        map.put(AREA_ID, selectedAreaId);
        return map;
    }

    private void redirectToLoginFragment(View v) {
        commonViewModel.city.setValue(fragmentSelectLocationBinding.spinnerCity.toString());
        commonViewModel.area.setValue(fragmentSelectLocationBinding.spinnerArea.toString());

        basicDetails.setCity(fragmentSelectLocationBinding.spinnerCity.toString());
        basicDetails.setArea(fragmentSelectLocationBinding.spinnerArea.toString());

        int errorCodeForCity = basicDetails.isValidCity();
        int errorCodeForArea = basicDetails.isValidArea();

        if (errorCodeForCity == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_city));
        } else if (errorCodeForArea == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_area));
        } else {
            if (isConnectingToInternet(context)) {
                //add API call here
                preferences.setIsLoggedIn(true);
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                requireActivity().finish();
                startActivity(intent);
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), SelectLocationFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        callAreaApi(showCircleProgressDialog(context, ""));
                        break;
                    case 1:
                        callCityApi(showCircleProgressDialog(context, ""));
                        break;
                    case 2:
                        callUpdateLocationApi(showCircleProgressDialog(context, ""));
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}