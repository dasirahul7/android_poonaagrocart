package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.app.AppConstants.AREA_ID;
import static com.poona.agrocart.app.AppConstants.CITY_ID;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SelectLocationFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private FragmentSelectLocationBinding fragmentSelectLocationBinding;

    private List<AreaResponse.Area> areaArrayList;
    private List<CityResponse.City> cityArrayList;
    private BasicDetails basicDetails;
    private CommonViewModel commonViewModel;
    private SelectLocationViewModel selectLocationViewModel;

    private String selectedCityId = "1";
    private String selectedAreaId = "1";
    private String selectedCity, selectedArea;

    private View rootSelectView;
    private ArrayList<String> areas;
    private ArrayList<String> cities;
    private ArrayList<String> areaIds;
    private ArrayList<String> cityIds;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSelectLocationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false);
        fragmentSelectLocationBinding.setLifecycleOwner(this);
        rootSelectView = fragmentSelectLocationBinding.getRoot();

        initViews();

        ivBack.setOnClickListener(v -> {
            Navigation.findNavController(rootSelectView).popBackStack();
        });

        return rootSelectView;
    }

    private void initViews() {
        /*checkGpsStatus();*/
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        selectLocationViewModel = new ViewModelProvider(this).get(SelectLocationViewModel.class);
//        fragmentSelectLocationBinding.setCommonViewModel(commonViewModel);

        fragmentSelectLocationBinding.btnSubmit.setOnClickListener(this);
        fragmentSelectLocationBinding.ivPoonaAgroMainLogo.bringToFront();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        basicDetails = new BasicDetails();
        areaArrayList = new ArrayList<AreaResponse.Area>();
        cityArrayList = new ArrayList<CityResponse.City>();
        callAreaApi(showCircleProgressDialog(context, ""));
        callCityApi(showCircleProgressDialog(context, ""));
        setUpSpinnerCity();
        setUpSpinnerArea();
    }

    /* Area API*/
    private void callAreaApi(ProgressDialog showCircleProgressDialog) {
        Observer<AreaResponse> areaResponseObserver = areaResponse -> {
            if (areaResponse != null) {
                showCircleProgressDialog.dismiss();
                switch (areaResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (areaResponse.getStatus() == 200) {
                            successToast(context, "" + areaResponse.getMessage());
                            if (areaResponse.getAreas() != null) {
                                if (areaResponse.getAreas().size() > 0) {
                                    areaArrayList = areaResponse.getAreas();
                                    areas = new ArrayList<>();
                                    areaIds = new ArrayList<>();
                                    for (AreaResponse.Area area : areaArrayList) {
                                        areas.add(area.getAreaName());
                                        areaIds.add(area.getId());
                                        System.out.println("area " + area.getAreaName());
                                    }
                                    ArrayAdapter<String> areasArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.text_spinner, areas);
                                    areasArrayAdapter.setDropDownViewResource(R.layout.text_spinner);
                                    selectedAreaId = areaArrayList.get(0).getId();
                                    selectedArea = areaArrayList.get(0).getAreaName();
                                    fragmentSelectLocationBinding.spinnerArea.setAdapter(areasArrayAdapter);
                                }
                            }

                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
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
        selectLocationViewModel.getAreaResponse(showCircleProgressDialog, SelectLocationFragment.this)
                .observe(getViewLifecycleOwner(), areaResponseObserver);
    }

    /* City API*/
    private void callCityApi(ProgressDialog showCircleProgressDialog) {
        Observer<CityResponse> cityResponseObserver = new Observer<CityResponse>() {
            @Override
            public void onChanged(CityResponse cityResponse) {
                if (cityResponse != null) {
                    switch (cityResponse.getStatus()) {
                        case STATUS_CODE_200://Record Create/Update Successfully
                            if (cityResponse.getStatus() == 200) {
                                successToast(context, "" + cityResponse.getMessage());
                                if (cityResponse.getCities() != null) {
                                    if (cityResponse.getCities().size() > 0) {
                                        cityArrayList = cityResponse.getCities();
                                        cityIds = new ArrayList<>();
                                        cities = new ArrayList<>();
                                        for (CityResponse.City city : cityArrayList) {
                                            cities.add(city.getCityName());
                                            cityIds.add(city.getId());
                                            System.out.println("city " + city.getCityName());
                                        }
                                        ArrayAdapter<String> areasArrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.text_spinner, cities);
                                        areasArrayAdapter.setDropDownViewResource(R.layout.text_spinner);
                                        selectedCityId = cityIds.get(0);
                                        selectedCity = cities.get(0);
                                        fragmentSelectLocationBinding.spinnerCity.setAdapter(areasArrayAdapter);
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
            }
        };
        selectLocationViewModel.getCityResponse(showCircleProgressDialog, SelectLocationFragment.this)
                .observe(getViewLifecycleOwner(), cityResponseObserver);
    }

    private void setUpSpinnerArea() {
        fragmentSelectLocationBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicDetails.setArea(parent.getItemAtPosition(position).toString());
                if (areaIds != null) {
                    System.out.println("selected area " + areaIds.get(position));
                    selectedAreaId = areaIds.get(position);
                    selectedArea = areas.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setUpSpinnerCity() {
        fragmentSelectLocationBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicDetails.setCity(parent.getItemAtPosition(position).toString());
                commonViewModel.city.setValue(basicDetails.getCity());
                if (cityArrayList != null) {
                    System.out.println("selected city " + cityArrayList.get(position).getId());
                    selectedCityId = cityArrayList.get(position).getId();
                    selectedCity = cityArrayList.get(position).getCityName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
//        if (selectedArea==null|| selectedCity ==null)
//            warningToast(context,"select city and area");
        callUpdateLocationApi(showCircleProgressDialog(context, ""));

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
                            redirectToLoginFragment(rootSelectView);
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
    public void onNetworkException(int from) {
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