package com.poona.agrocart.ui.nav_profile;

import static android.view.View.GONE;
import static androidx.core.content.ContextCompat.checkSelfPermission;
import static com.poona.agrocart.app.AppConstants.ALTERNATE_MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.AREA_ID;
import static com.poona.agrocart.app.AppConstants.CITY_ID;
import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;
import static com.poona.agrocart.app.AppConstants.DATE_OF_BIRTH;
import static com.poona.agrocart.app.AppConstants.EMAIL;
import static com.poona.agrocart.app.AppConstants.GENDER;
import static com.poona.agrocart.app.AppConstants.MOBILE_NUMBER;
import static com.poona.agrocart.app.AppConstants.NAME;
import static com.poona.agrocart.app.AppConstants.PROFILE_IMAGE;
import static com.poona.agrocart.app.AppConstants.STATE_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.data.network.reponses.StateResponse;
import com.poona.agrocart.databinding.DialogSelectPhotoBinding;
import com.poona.agrocart.databinding.FragmentEditProfileBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.login.BasicDetails;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.HttpException;

public class EditProfileFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = EditProfileFragment.class.getSimpleName();
    private FragmentEditProfileBinding fragmentEditProfileBinding;
    private MyProfileViewModel myProfileViewModel;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private View view;

    private BasicDetails basicDetails;

    private String selectedStateId = "0";
    private String selectedCityId = "0";
    private String selectedAreaId = "0";
    private String selectedState = "";
    private String selectedCity = "";
    private String selectedArea = "";

    private ProfileResponse profileResponse = null;
    private StateResponse stateResponse = null;
    private CityResponse cityResponse = null;
    private AreaResponse areaResponse = null;
    private List<BasicDetails> stateList;
    private List<BasicDetails> cityList;
    private List<BasicDetails> areaList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentEditProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);

        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        fragmentEditProfileBinding.setMyProfileViewModel(myProfileViewModel);
        fragmentEditProfileBinding.setLifecycleOwner(this);

        view = fragmentEditProfileBinding.getRoot();

        basicDetails = new BasicDetails();

        initView();

        fragmentEditProfileBinding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
            switch(checkedId){
                case R.id.rb_male:
                    myProfileViewModel.gender.setValue("male");
                    break;
                case R.id.rb_female:
                    myProfileViewModel.gender.setValue("female");
                    break;
                case R.id.rb_other:
                    myProfileViewModel.gender.setValue("other");
                    break;
            }
        });

        // Register the permissions callback, which handles the user's response to the
        // system permissions dialog. Save the return value, an instance of
        // ActivityResultLauncher, as an instance variable.
        multiplePermissionActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
            Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                // Permission is granted. Continue the action or workflow in your app.
                Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                errorToast(context, getResources().getString(R.string.ensure_your_all_permissions));
                //multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
            } else {
                if(clickedOn.equals("gallery")) {
                    galleryIntent();
                } else if(clickedOn.equals("camera")) {
                    cameraIntent();
                }
            }
        });

        return view;
    }

    private void initView() {
        initTitleBar(getString(R.string.menu_my_profile));

        fragmentEditProfileBinding.tvDateOfBirthInput.setOnClickListener(this);
        fragmentEditProfileBinding.frameLayout.setOnClickListener(this);
        fragmentEditProfileBinding.cbtSave.setOnClickListener(this);
        fragmentEditProfileBinding.ivChooseProfilePhoto.setOnClickListener(this);
        fragmentEditProfileBinding.llMobileNumber.setOnClickListener(this);

        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();

        if (isConnectingToInternet(context)) {
            getProfileStateApiResponses(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void getProfileStateApiResponses(ProgressDialog progressDialog) {
        /*print user input parameters*/
        myProfileViewModel.getProfileStateResponses(context, getProfileParameters())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<String> strings) {
                        Type profileType = new TypeToken<ProfileResponse>(){}.getType();
                        profileResponse = new GsonBuilder().create().fromJson(strings.get(0), profileType);

                        Type stateType = new TypeToken<StateResponse>(){}.getType();
                        stateResponse = new GsonBuilder().create().fromJson(strings.get(1), stateType);

                        selectedStateId = profileResponse.getProfile().getStateId();
                        selectedState = profileResponse.getProfile().getStateName();

                        selectedCityId = profileResponse.getProfile().getCityId();
                        selectedCity = profileResponse.getProfile().getCityName();

                        selectedAreaId = profileResponse.getProfile().getAreaId();
                        selectedArea = profileResponse.getProfile().getAreaName();

                        setupStateSpinner();
                        getCityAreaApiResponses(showCircleProgressDialog(context, ""));

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        if(e instanceof HttpException) {
                            try {
                                profileResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), ProfileResponse.class);
                                stateResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), StateResponse.class);

                                setupStateSpinner();
                                getCityAreaApiResponses(showCircleProgressDialog(context, ""));
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                showServerErrorDialog(getString(R.string.for_better_user_experience), EditProfileFragment.this, () -> {
                                    if (isConnectingToInternet(context)) {
                                        getProfileStateApiResponses(showCircleProgressDialog(context, ""));
                                    } else {
                                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                                    }
                                }, context);
                            }
                        } else {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    private void getCityAreaApiResponses(ProgressDialog progressDialog) {
        /*print user input parameters*/
        myProfileViewModel.getCityAreaResponses(context, getCityParameters(), getAreaParameters())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<String> strings) {
                        Type cityType = new TypeToken<CityResponse>(){}.getType();
                        cityResponse = new GsonBuilder().create().fromJson(strings.get(0), cityType);

                        Type areaType = new TypeToken<AreaResponse>(){}.getType();
                        areaResponse = new GsonBuilder().create().fromJson(strings.get(1), areaType);

                        setupCitySpinner();
                        setDefaultSelectedValues();

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        if(e instanceof HttpException) {
                            try {
                                cityResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), CityResponse.class);
                                areaResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), AreaResponse.class);

                                setupCitySpinner();
                                setDefaultSelectedValues();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                showServerErrorDialog(getString(R.string.for_better_user_experience), EditProfileFragment.this, () -> {
                                    if (isConnectingToInternet(context)) {
                                        getCityAreaApiResponses(showCircleProgressDialog(context, ""));
                                    } else {
                                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                                    }
                                }, context);
                            }
                        } else {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                    }
                });
    }

    private HashMap<String, String> getProfileParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CUSTOMER_ID, preferences.getUid());
        return map;
    }

    private int checkState = 0;
    private void setupStateSpinner() {
        BasicDetails basicDetails = new BasicDetails();
        basicDetails.setId("0");
        basicDetails.setName("Select");
        stateList.add(basicDetails);

        if(stateResponse != null && stateResponse.getStates() != null && stateResponse.getStates().size() > 0) {
            for(int i = 0; i < stateResponse.getStates().size(); i++) {
                BasicDetails details = new BasicDetails();
                details.setId(stateResponse.getStates().get(i).getId());
                details.setName(stateResponse.getStates().get(i).getStateName());
                stateList.add(details);
            }
        }

        CustomArrayAdapter stateArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, stateList);
        fragmentEditProfileBinding.spinnerState.setAdapter(stateArrayAdapter);

        fragmentEditProfileBinding.spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EditProfileFragment.this.basicDetails.setState(adapterView.getItemAtPosition(i).toString());
                myProfileViewModel.state.setValue(EditProfileFragment.this.basicDetails.getState());
                if (stateList != null) {
                    System.out.println("selected state " + stateList.get(i).getId());
                    selectedStateId = stateList.get(i).getId();
                    selectedState = stateList.get(i).getName();

                    if(++checkState > 1 && !selectedStateId.equals("0"))
                        callCityApi(showCircleProgressDialog(context, ""));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*for first time*/
        setupCitySpinner();
    }

    private int checkCity = 0;
    private void setupCitySpinner() {
        if(cityList != null && cityList.size() > 0) {
            cityList.clear();
        }

        BasicDetails basicDetails = new BasicDetails();
        basicDetails.setId("0");
        basicDetails.setName("Select");
        cityList.add(basicDetails);

        if(cityResponse != null && cityResponse.getCities() != null && cityResponse.getCities().size() > 0) {
            for(int i = 0; i < cityResponse.getCities().size(); i++) {
                BasicDetails details = new BasicDetails();
                details.setId(cityResponse.getCities().get(i).getId());
                details.setName(cityResponse.getCities().get(i).getCityName());
                cityList.add(details);
            }
        }

        CustomArrayAdapter cityArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, cityList);
        fragmentEditProfileBinding.spinnerCity.setAdapter(cityArrayAdapter);

        fragmentEditProfileBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EditProfileFragment.this.basicDetails.setCity(adapterView.getItemAtPosition(i).toString());
                myProfileViewModel.city.setValue(EditProfileFragment.this.basicDetails.getCity());
                if (cityList != null) {
                    System.out.println("selected city " + cityList.get(i).getId());
                    selectedCityId = cityList.get(i).getId();
                    selectedCity = cityList.get(i).getName();

                    if(++checkCity > 1 && !selectedCityId.equals("0"))
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

    private int checkArea = 0;
    private void setupAreaSpinner() {
        if(areaList != null && areaList.size() > 0) {
            areaList.clear();
        }

        BasicDetails basicDetails = new BasicDetails();
        basicDetails.setId("0");
        basicDetails.setName("Select");
        areaList.add(basicDetails);

        if(areaResponse != null && areaResponse.getAreas() != null && areaResponse.getAreas().size() > 0) {
            for(int i = 0; i < areaResponse.getAreas().size(); i++) {
                BasicDetails details = new BasicDetails();
                details.setId(areaResponse.getAreas().get(i).getId());
                details.setName(areaResponse.getAreas().get(i).getAreaName());
                areaList.add(details);
            }
        }

        CustomArrayAdapter areaArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, areaList);
        fragmentEditProfileBinding.spinnerArea.setAdapter(areaArrayAdapter);

        fragmentEditProfileBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EditProfileFragment.this.basicDetails.setArea(adapterView.getItemAtPosition(i).toString());
                myProfileViewModel.area.setValue(EditProfileFragment.this.basicDetails.getArea());
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

    private void callCityApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : getCityParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<CityResponse> cityResponseObserver = cityResponse -> {
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
                        this.cityResponse = null;
                        setupCitySpinner();
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
        myProfileViewModel.getCityResponse(progressDialog, EditProfileFragment.this, getCityParameters())
                .observe(getViewLifecycleOwner(), cityResponseObserver);
    }

    private HashMap<String, String> getCityParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(STATE_ID, selectedStateId);
        return map;
    }

    private void callAreaApi(ProgressDialog showCircleProgressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : getAreaParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<AreaResponse> areaResponseObserver = areaResponse -> {
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
        myProfileViewModel.getAreaResponse(showCircleProgressDialog,
                EditProfileFragment.this, getAreaParameters())
                .observe(getViewLifecycleOwner(), areaResponseObserver);
    }

    private HashMap<String, String> getAreaParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CITY_ID, selectedCityId);
        return map;
    }

    private void setDefaultSelectedValues() {
        if(profileResponse != null) {
            if(profileResponse.getProfile().getImage() != null && !TextUtils.isEmpty(profileResponse.getProfile().getImage())) {
                myProfileViewModel.profilePhoto.setValue(profileResponse.getProfile().getImage());
                loadingImage(context, myProfileViewModel.profilePhoto.getValue(), fragmentEditProfileBinding.ivProfilePicture);
                loadingImage(context, myProfileViewModel.profilePhoto.getValue(), ((HomeActivity)context).civProfilePhoto);
            } else {
                myProfileViewModel.profilePhoto.setValue("");
            }

            if(profileResponse.getProfile().getName() != null && !TextUtils.isEmpty(profileResponse.getProfile().getName())) {
                myProfileViewModel.name.setValue(profileResponse.getProfile().getName());
                ((HomeActivity)context).tvUserName.setText("Hello! "+myProfileViewModel.name.getValue());
            } else {
                myProfileViewModel.name.setValue("");
                ((HomeActivity)context).tvUserName.setText("Hello!");
            }

            if(profileResponse.getProfile().getMobile() != null && !TextUtils.isEmpty(profileResponse.getProfile().getMobile())) {
                myProfileViewModel.mobileNo.setValue(profileResponse.getProfile().getMobile());
            } else {
                myProfileViewModel.mobileNo.setValue("");
            }

            if(profileResponse.getProfile().getAlternateMobile() != null && !TextUtils.isEmpty(profileResponse.getProfile().getAlternateMobile())) {
                myProfileViewModel.alternateMobileNo.setValue(profileResponse.getProfile().getAlternateMobile());;
            } else {
                myProfileViewModel.alternateMobileNo.setValue("");;
            }

            if(profileResponse.getProfile().getEmail() != null && !TextUtils.isEmpty(profileResponse.getProfile().getEmail())) {
                myProfileViewModel.emailId.setValue(profileResponse.getProfile().getEmail());
            } else {
                myProfileViewModel.emailId.setValue("");
            }

            if(profileResponse.getProfile().getStateId() != null && !TextUtils.isEmpty(profileResponse.getProfile().getStateId())) {
                myProfileViewModel.state.setValue(profileResponse.getProfile().getStateId());
                if(stateList != null && stateList.size() > 0) {
                    for(int i = 0; i < stateList.size(); i++) {
                        if(stateList.get(i).getId().equals(myProfileViewModel.state.getValue())) {
                            fragmentEditProfileBinding.spinnerState.setSelection(i);
                        }
                    }
                }
            } else {
                myProfileViewModel.state.setValue("");
            }

            if(profileResponse.getProfile().getCityId() != null && !TextUtils.isEmpty(profileResponse.getProfile().getCityId())) {
                myProfileViewModel.city.setValue(profileResponse.getProfile().getCityId());
                if(cityList != null && cityList.size() > 0) {
                    for(int i = 0; i < cityList.size(); i++) {
                        if(cityList.get(i).getId().equals(myProfileViewModel.city.getValue())) {
                            fragmentEditProfileBinding.spinnerCity.setSelection(i);
                        }
                    }
                }
            } else {
                myProfileViewModel.city.setValue("");
            }

            if(profileResponse.getProfile().getAreaId() != null && !TextUtils.isEmpty(profileResponse.getProfile().getAreaId())) {
                myProfileViewModel.area.setValue(profileResponse.getProfile().getAreaId());
                if(areaList != null && areaList.size() > 0) {
                    for(int i = 0; i < areaList.size(); i++) {
                        if(areaList.get(i).getId().equals(myProfileViewModel.area.getValue())) {
                            fragmentEditProfileBinding.spinnerArea.setSelection(i);
                        }
                    }
                }
            } else {
                myProfileViewModel.area.setValue("");
            }

            if(profileResponse.getProfile().getGender() != null && !TextUtils.isEmpty(profileResponse.getProfile().getGender())) {
                myProfileViewModel.gender.setValue(profileResponse.getProfile().getGender());
                if(myProfileViewModel.gender.getValue().equals("male")) {
                    fragmentEditProfileBinding.rgGender.check(R.id.rb_male);
                } else if(myProfileViewModel.gender.getValue().equals("female")) {
                    fragmentEditProfileBinding.rgGender.check(R.id.rb_female);
                } else if(myProfileViewModel.gender.getValue().equals("other")) {
                    fragmentEditProfileBinding.rgGender.check(R.id.rb_other);
                }
            } else {
                myProfileViewModel.gender.setValue("");
            }

            if(profileResponse.getProfile().getDateOfBirth() != null && !TextUtils.isEmpty(profileResponse.getProfile().getDateOfBirth())) {
                try {
                    myProfileViewModel.dateOfBirth.setValue(formatDate(profileResponse.getProfile().getDateOfBirth(), "yyyy-MM-dd", "dd MMM yyyy"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                myProfileViewModel.dateOfBirth.setValue("");
            }
        }
    }

    public void showCalendar() {
        //showing date picker dialog
        DatePickerDialog dpd;
        calendar = Calendar.getInstance();
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
                String txtDisplayDate = null;
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fragmentEditProfileBinding.tvDateOfBirthInput.setText(txtDisplayDate);
            calendar.set(year, month, dayOfMonth);
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_of_birth_input:
                showCalendar();
                break;
            case R.id.iv_choose_profile_photo:
                showDialogForAddPhotos();
                break;
            case R.id.ll_mobile_number:
                warningToast(context, getString(R.string.you_cannot_edit_mobile_number));
                break;
            case R.id.cbt_save:
                getUserInputAndSetIntoPojo();
                checkValidInputFields();
                break;
        }
    }

    private void getUserInputAndSetIntoPojo() {
        if(stateList != null && stateList.size() > 0) {
            for(int i = 0; i < stateList.size(); i++) {
                if(stateList.get(i).getName().equals(fragmentEditProfileBinding.spinnerState.getSelectedItem().toString())) {
                    myProfileViewModel.state.setValue(stateList.get(i).getId());
                }
            }
        }
        if(cityList != null && cityList.size() > 0) {
            for(int i = 0; i < cityList.size(); i++) {
                if(cityList.get(i).getName().equals(fragmentEditProfileBinding.spinnerCity.getSelectedItem().toString())) {
                    myProfileViewModel.city.setValue(cityList.get(i).getId());
                }
            }
        }
        if(areaList != null && areaList.size() > 0) {
            for(int i = 0; i < areaList.size(); i++) {
                if(areaList.get(i).getName().equals(fragmentEditProfileBinding.spinnerArea.getSelectedItem().toString())) {
                    myProfileViewModel.area.setValue(areaList.get(i).getId());
                }
            }
        }

        basicDetails.setName(myProfileViewModel.name.getValue());
        basicDetails.setMobileNumber(myProfileViewModel.mobileNo.getValue());
        basicDetails.setAlternateMobileNumber(myProfileViewModel.alternateMobileNo.getValue());
        basicDetails.setEmailId(myProfileViewModel.emailId.getValue());
        basicDetails.setState(myProfileViewModel.state.getValue());
        basicDetails.setCity(myProfileViewModel.city.getValue());
        basicDetails.setArea(myProfileViewModel.area.getValue());
        basicDetails.setGender(myProfileViewModel.gender.getValue());
        basicDetails.setDob(myProfileViewModel.dateOfBirth.getValue());
    }
    private void checkValidInputFields() {
        int errorCodeName = basicDetails.isValidName();
        int errorCodeMobileNumber = basicDetails.isValidMobileNumber();
        int errorCodeAlternateMobileNumber = basicDetails.isValidAlternateMobileNumber();
        int errorCodeEmailId = basicDetails.isValidEmailId();
        int errorCodeState = basicDetails.isValidState();
        int errorCodeCity = basicDetails.isValidCity();
        int errorCodeArea = basicDetails.isValidArea();
        int errorCodeGender = basicDetails.isValidGender();
        int errorCodeDob = basicDetails.isValidDob();

        if(errorCodeName == 0) {
            errorToast(requireActivity(), getString(R.string.name_should_not_be_empty));
        } else if(errorCodeMobileNumber == 0) {
            errorToast(requireActivity(), getString(R.string.mobile_number_should_not_be_empty));
        } else if(errorCodeMobileNumber == 1) {
            errorToast(requireActivity(), getString(R.string.enter_valid_mobile_number));
        } else if(errorCodeAlternateMobileNumber == 0) {
            errorToast(requireActivity(), getString(R.string.enter_valid_mobile_number));
        } else if(errorCodeEmailId == 0) {
            errorToast(requireActivity(), getString(R.string.email_id_should_not_be_empty));
        } else if(errorCodeEmailId == 1) {
            errorToast(requireActivity(), getString(R.string.please_enter_valid_email_id));
        } else if(errorCodeState == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_state));
        } else if(errorCodeCity == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_city));
        } else if(errorCodeArea == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_area));
        } else if(errorCodeGender == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_gender));
        } else if(errorCodeDob == 0) {
            errorToast(requireActivity(), getString(R.string.please_select_dob));
        } else {
            updateProfileApi(showCircleProgressDialog(context, ""));
        }
    }

    private void updateProfileApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, RequestBody> entry : updateProfileParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + stringifyRequestBody(entry.getValue()));
        }

        androidx.lifecycle.Observer<ProfileResponse> updateProfileResponseObserver = profileResponse -> {
            if (profileResponse != null) {
                progressDialog.dismiss();
                Log.e("Update Profile Api Response", new Gson().toJson(profileResponse));
                switch (profileResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, ""+profileResponse.getMessage());
                        NavHostFragment.findNavController(EditProfileFragment.this).popBackStack();
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

        myProfileViewModel
                .updateProfileResponse(progressDialog, EditProfileFragment.this, updateProfileParameters(), multipartBodyImageFile)
                .observe(getViewLifecycleOwner(), updateProfileResponseObserver);
    }

    private HashMap<String, RequestBody> updateProfileParameters() {
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(NAME, toRequestBody(basicDetails.getName()));
        map.put(MOBILE_NUMBER, toRequestBody(basicDetails.getMobileNumber()));
        map.put(ALTERNATE_MOBILE_NUMBER, toRequestBody(basicDetails.getAlternateMobileNumber()));
        map.put(EMAIL, toRequestBody(basicDetails.getEmailId()));
        map.put(STATE_ID, toRequestBody(basicDetails.getState()));
        map.put(CITY_ID, toRequestBody(basicDetails.getCity()));
        map.put(AREA_ID, toRequestBody(basicDetails.getArea()));
        map.put(GENDER, toRequestBody(basicDetails.getGender()));
        try {
            map.put(DATE_OF_BIRTH, toRequestBody(formatDate(basicDetails.getDob(), "dd MMM yyyy", "yyyy-MM-dd")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (compressedImageFile != null) {
            if (!compressedImageFile.equals("")) {
                RequestBody reqFile = RequestBody.create(compressedImageFile, MediaType.parse("*/*"));
                multipartBodyImageFile = MultipartBody.Part.createFormData(PROFILE_IMAGE,
                        compressedImageFile.getName(), reqFile);
            }
        }

        return map;
    }

    /*
     * start to image update
     * */
    private Dialog dialogForAddPhotos = null;
    private void showDialogForAddPhotos() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.StyleDataConfirmationDialog));

        DialogSelectPhotoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_select_photo, null, false);
        View dialogView = binding.getRoot();

        //binding.setViewModel(new ViewModel(this, event.olaBooking));

        builder.setView(dialogView);
        builder.setCancelable(false);

        dialogForAddPhotos = builder.create();
        dialogForAddPhotos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogForAddPhotos.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        binding.tvGallery.setOnClickListener(v -> {
            dialogForAddPhotos.dismiss();
            clickedOn = "gallery";
            askPermissions();
        });

        binding.tvCamera.setOnClickListener(v -> {
            dialogForAddPhotos.dismiss();
            clickedOn = "camera";
            askPermissions();
        });

        if(myProfileViewModel.profilePhoto.getValue() == null
                || TextUtils.isEmpty(myProfileViewModel.profilePhoto.getValue())) {
            binding.tvDeletePhoto.setVisibility(GONE);
            binding.horizontalView1.setVisibility(GONE);
        }

        /*binding.tvDeletePhoto.setOnClickListener(v -> {
            dialog.dismiss();
            if(profileViewModel.profilePhoto.getValue() == null
                    || TextUtils.isEmpty(profileViewModel.profilePhoto.getValue())) {
                compressedImageFile = null;
            }
            else
            {
                calDeletePhotoApi(showCircleProgressDialog(context, ""));
            }
        });*/

        binding.ivCloseDialog.setOnClickListener(v -> {
            dialogForAddPhotos.dismiss();
            dialogForAddPhotos = null;
        });

        dialogForAddPhotos.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    dialogForAddPhotos.dismiss();
                    dialogForAddPhotos = null;
                }
                return true;
            }
        });

        dialogForAddPhotos.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialogForAddPhotos.getWindow().getAttributes());

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        // Set alert dialog height equal to screen height 100%
        int dialogWindowHeight = (int) (displayHeight * 1.0f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialogForAddPhotos.getWindow().setAttributes(layoutParams);
    }

    private File compressedImageFile = null;
    private MultipartBody.Part multipartBodyImageFile = null;
    final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                dialogForAddPhotos = null;
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    final Uri resultUri = getSelectedImageUri(data, 1); //code 1 for camera
                    File f = new File(resultUri.getPath());

                    /*Cropping image Start*/
                    String outputFileName = Calendar.getInstance().getTimeInMillis() + ".jpeg";
                    Uri selectedImage = Uri.fromFile(f);
                    gotoUCropImageActivity(selectedImage, Uri.fromFile(new File(context.getExternalCacheDir(), outputFileName)));
                    /*Cropping image End*/
                }
            });

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                dialogForAddPhotos = null;
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    final Uri resultUri = getSelectedImageUri(data, 2); //code 2 for gallery
                    File f = new File(resultUri.getPath());

                    /*Cropping image Start*/
                    String outputFileName = Calendar.getInstance().getTimeInMillis() + ".jpeg";
                    Uri selectedImage = Uri.fromFile(f);
                    gotoUCropImageActivity(selectedImage, Uri.fromFile(new File(context.getExternalCacheDir(), outputFileName)));
                    /*Cropping image End*/
                }
            });

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> cropActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                dialogForAddPhotos = null;
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    final Uri resultUri = UCrop.getOutput(data);
                    try {
                        File f = new File(resultUri.getPath());

                        /*Compressing gallery image using Compressor library Start*/
                        compressedImageFile = new Compressor(getActivity()).setQuality(75).compressToFile(f);

                        Bitmap myBitmap = BitmapFactory.decodeFile(compressedImageFile.getAbsolutePath());
                        fragmentEditProfileBinding.ivProfilePicture.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    private void askPermissions() {
        if (!hasPermissions(PERMISSIONS)) {
            Log.d("PERMISSIONS", "Launching multiple contract permission launcher for ALL required permissions");
            multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
        } else {
            if(clickedOn.equals("gallery")) {
                galleryIntent();
            } else if(clickedOn.equals("camera")) {
                cameraIntent();
            }
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if(shouldShowRequestPermissionRationale(permission)){
                    //denied
                    Log.d("PERMISSIONS", "Permission denied: " + permission);
                    return false;
                } else {
                    if(checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
                        //allowed
                        Log.d("PERMISSIONS", "Permission already granted: " + permission);
                        return true;
                    } else{
                        //set to never ask again
                        //do something here.
                        Log.d("PERMISSIONS", "Permission Set to never ask again: " + permission);
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public String fileName = "";
    private String clickedOn = "0";
    public void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            fileName = Calendar.getInstance().getTimeInMillis() +".jpg";
            File f = new File(context.getExternalCacheDir().getAbsolutePath(), fileName);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f));
            cameraActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
    }

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        try {
            galleryActivityResultLauncher.launch(intent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoUCropImageActivity(Uri sourceUri, Uri destinationUri) {
        Intent intent = UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1)
                .getIntent(context);
        cropActivityResultLauncher.launch(intent);
    }

    public Uri getSelectedImageUri(Intent data, int code) {
        Uri selectedImage = null;
        switch (code) {
            case 1: //1 is camera
                try {
                    File f = new File(context.getExternalCacheDir().getAbsolutePath());
                    for (File temp : Objects.requireNonNull(f.listFiles())) {
                        if (temp.getName().equals(fileName)) {
                            f = temp;
                            break;
                        }
                    }
                    if (!f.exists()) {
                        Toast.makeText(getActivity(), "Error while capturing image", Toast.LENGTH_LONG).show();
                    }

                    selectedImage = Uri.fromFile(f);
                }catch (Exception e){e.printStackTrace();}
                break;
            case 2: //2 is gallery
                Bitmap bm = null;
                String path = "";

                if (data != null) {
                    try {
                        bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                        Uri tempUri = getImageUri(bm);
                        path = getRealPathFromURI(tempUri.toString());
                        File f = new File(path);

                        selectedImage = Uri.fromFile(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        return selectedImage;
    }

    public Uri getImageUri(Bitmap thumbnail) {
        String path = "";
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), thumbnail, String.valueOf(Calendar.getInstance().getTimeInMillis()), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.parse(path);
    }

    public String getRealPathFromURI(String contentURI) {
        try {
            Uri contentUri = Uri.parse(contentURI);
            Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
            if (cursor == null) {
                return contentUri.getPath();
            } else {
                cursor.moveToFirst();
                int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                return cursor.getString(index);
            }
        }catch (Exception e){e.printStackTrace();}
        return contentURI;
    }

    public ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();
        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    public boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    public boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    /*end to image update*/

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), EditProfileFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    updateProfileApi(showCircleProgressDialog(context, ""));
                } else if(from == 1) {

                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}