package com.poona.agrocart.ui.nav_profile;

import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.data.network.reponses.StateResponse;
import com.poona.agrocart.databinding.FragmentMyProfileBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import retrofit2.HttpException;

public class MyProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MyProfileFragment.class.getSimpleName();
    private FragmentMyProfileBinding fragmentMyProfileBinding;
    private MyProfileViewModel myProfileViewModel;
    private Calendar calendar;
    private int mYear, mMonth, mDay;

    private final String[] cities={"Pune"};
    private final String[] areas={"Vishrantwadi", "Khadki"};
    private final String[] states={"Maharashtra"};

    private View view;

    private BasicDetails basicDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentMyProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_profile, container, false);

        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        fragmentMyProfileBinding.setMyProfileViewModel(myProfileViewModel);
        fragmentMyProfileBinding.setLifecycleOwner(this);

        view = fragmentMyProfileBinding.getRoot();

        basicDetails = new BasicDetails();

        initView();

        fragmentMyProfileBinding.rgGender.setOnCheckedChangeListener((group, checkedId) -> {
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

        return view;
    }

    private void initView() {
        initTitleBar(getString(R.string.menu_my_profile));

        fragmentMyProfileBinding.tvDateOfBirthInput.setOnClickListener(this);
        fragmentMyProfileBinding.frameLayout.setOnClickListener(this);
        fragmentMyProfileBinding.cbtSave.setOnClickListener(this);

        if (isConnectingToInternet(context)) {
            getCommonApiResponses(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void setupSpinner() {
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        areaList = new ArrayList<>();

        if(stateResponse.getStates() != null && stateResponse.getStates().size() > 0) {
            for(int i = 0; i < stateResponse.getStates().size(); i++) {
                BasicDetails basicDetails = new BasicDetails();
                basicDetails.setId(stateResponse.getStates().get(i).getId());
                basicDetails.setName(stateResponse.getStates().get(i).getStateName());
                stateList.add(basicDetails);
            }
        } else {
            BasicDetails basicDetails = new BasicDetails();
            basicDetails.setId("0");
            basicDetails.setName("Select");
            stateList.add(basicDetails);
        }

        if(cityResponse.getCities() != null && cityResponse.getCities().size() > 0) {
            for(int i = 0; i < cityResponse.getCities().size(); i++) {
                BasicDetails basicDetails = new BasicDetails();
                basicDetails.setId(cityResponse.getCities().get(i).getId());
                basicDetails.setName(cityResponse.getCities().get(i).getCityName());
                cityList.add(basicDetails);
            }
        } else {
            BasicDetails basicDetails = new BasicDetails();
            basicDetails.setId("0");
            basicDetails.setName("Select");
            cityList.add(basicDetails);
        }

        if(areaResponse.getAreas() != null && areaResponse.getAreas().size() > 0) {
            for(int i = 0; i < areaResponse.getAreas().size(); i++) {
                BasicDetails basicDetails = new BasicDetails();
                basicDetails.setId(areaResponse.getAreas().get(i).getId());
                basicDetails.setName(areaResponse.getAreas().get(i).getAreaName());
                areaList.add(basicDetails);
            }
        } else {
            BasicDetails basicDetails = new BasicDetails();
            basicDetails.setId("0");
            basicDetails.setName("Select");
            areaList.add(basicDetails);
        }

        CustomArrayAdapter stateArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, stateList);
        fragmentMyProfileBinding.spinnerState.setAdapter(stateArrayAdapter);

        CustomArrayAdapter cityArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, cityList);
        fragmentMyProfileBinding.spinnerCity.setAdapter(cityArrayAdapter);

        CustomArrayAdapter areaArrayAdapter = new CustomArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions, areaList);
        fragmentMyProfileBinding.spinnerArea.setAdapter(areaArrayAdapter);
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
                fragmentMyProfileBinding.tvDateOfBirthInput.setText(txtDisplayDate);
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
            case R.id.frameLayout:
                //openGallery();
                break;
            case R.id.cbt_save:
                checkValidInputFields();
                break;
        }
    }

    private void checkValidInputFields() {
        myProfileViewModel.state.setValue(fragmentMyProfileBinding.spinnerState.getSelectedItem().toString());
        myProfileViewModel.city.setValue(fragmentMyProfileBinding.spinnerCity.getSelectedItem().toString());
        myProfileViewModel.area.setValue(fragmentMyProfileBinding.spinnerArea.getSelectedItem().toString());

        basicDetails.setName(myProfileViewModel.name.getValue());
        basicDetails.setMobileNumber(myProfileViewModel.mobileNo.getValue());
        basicDetails.setAlternateMobileNumber(myProfileViewModel.alternateMobileNo.getValue());
        basicDetails.setEmailId(myProfileViewModel.emailId.getValue());
        basicDetails.setState(myProfileViewModel.state.getValue());
        basicDetails.setCity(myProfileViewModel.city.getValue());
        basicDetails.setArea(myProfileViewModel.area.getValue());
        basicDetails.setGender(myProfileViewModel.gender.getValue());
        basicDetails.setDob(myProfileViewModel.dateOfBirth.getValue());

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
            Toast.makeText(context, "all ok", Toast.LENGTH_SHORT).show();
        }
    }

    private ProfileResponse profileResponse = null;
    private StateResponse stateResponse = null;
    private CityResponse cityResponse = null;
    private AreaResponse areaResponse = null;
    private List<BasicDetails> stateList;
    private List<BasicDetails> cityList;
    private List<BasicDetails> areaList;
    private void getCommonApiResponses(ProgressDialog progressDialog) {
        /* print user input parameters */
        for (Map.Entry<String, String> entry : profileParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }
        myProfileViewModel.getCommonApiResponses(context, profileParameters())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<String> strings) {
                        //strings.get(0) is Dashboard response
                        Type profileType = new TypeToken<ProfileResponse>(){}.getType();
                        profileResponse = new GsonBuilder().create().fromJson(strings.get(0), profileType);

                        //strings.get(1) is Teacher Lessons response
                        Type stateType = new TypeToken<StateResponse>(){}.getType();
                        stateResponse = new GsonBuilder().create().fromJson(strings.get(1), stateType);

                        //strings.get(2) is Subjects response
                        Type cityType = new TypeToken<CityResponse>(){}.getType();
                        cityResponse = new GsonBuilder().create().fromJson(strings.get(2), cityType);

                        //strings.get(3) is Subjects response
                        Type areaType = new TypeToken<AreaResponse>(){}.getType();
                        areaResponse = new GsonBuilder().create().fromJson(strings.get(3), areaType);

                        /*preferences.setProfileData(profileResponse);
                        preferences.setStateData(stateResponse);
                        preferences.setCityData(cityResponse);
                        preferences.setAreaData(areaResponse);*/

                        setupSpinner();

                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Gson gson = new GsonBuilder().create();
                        if(e instanceof HttpException) {
                            try {
                                profileResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), ProfileResponse.class);
                                stateResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), StateResponse.class);
                                cityResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), CityResponse.class);
                                areaResponse = gson.fromJson(((HttpException) e).response().errorBody().string(), AreaResponse.class);

                                /*preferences.setProfileData(profileResponse);
                                preferences.setStateData(stateResponse);
                                preferences.setCityData(cityResponse);
                                preferences.setAreaData(areaResponse);*/

                                setupSpinner();
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                showServerErrorDialog(getString(R.string.for_better_user_experience), MyProfileFragment.this, () -> {
                                    if (isConnectingToInternet(context)) {
                                        getCommonApiResponses(showCircleProgressDialog(context, ""));
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

    private HashMap<String, String> profileParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(CUSTOMER_ID, preferences.getUid());
        return map;
    }
}