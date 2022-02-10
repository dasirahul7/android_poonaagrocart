package com.poona.agrocart.ui.nav_profile;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyProfileBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;

import java.text.ParseException;
import java.util.Calendar;

public class MyProfileFragment extends BaseFragment implements View.OnClickListener {
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
        setupSpinner();

        return view;
    }

    private void setupSpinner() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,states);
        arrayAdapter.setDropDownViewResource(R.layout.custom_list_item_checked);
        fragmentMyProfileBinding.spinnerState.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,cities);
        arrayAdapter.setDropDownViewResource(R.layout.custom_list_item_checked);
        fragmentMyProfileBinding.spinnerCity.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.text_spinner_wallet_transactions,areas);
        arrayAdapter.setDropDownViewResource(R.layout.custom_list_item_checked);
        fragmentMyProfileBinding.spinnerArea.setAdapter(arrayAdapter);

        /*fragmentMyProfileBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });*/
    }

    private void initView() {
        initTitleBar(getString(R.string.menu_my_profile));
        Typeface font = Typeface.createFromAsset(context.getAssets(), getString(R.string.font_poppins_regular));
        fragmentMyProfileBinding.rbMale.setTypeface(font);
        fragmentMyProfileBinding.rbFemale.setTypeface(font);
        fragmentMyProfileBinding.rbOther.setTypeface(font);

        fragmentMyProfileBinding.tvDateOfBirthInput.setOnClickListener(this);
        fragmentMyProfileBinding.frameLayout.setOnClickListener(this);

        myProfileViewModel= new ViewModelProvider(this).get(MyProfileViewModel.class);
        fragmentMyProfileBinding.setMyProfileViewModel(myProfileViewModel);
    }

    public void showCalendar() {
        //showing date picker dialog
        DatePickerDialog dpd;
        calendar = Calendar.getInstance();
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String txtDisplayDate = null;
                    String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    try {
                        txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    fragmentMyProfileBinding.tvDateOfBirthInput.setText(txtDisplayDate);
                calendar.set(year, month, dayOfMonth);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_date_of_birth_input:
                showCalendar();
                break;
            case R.id.frameLayout:
                openGallery();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SELECT_IMAGE_FROM_GALLERY && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            fragmentMyProfileBinding.ivProfilePicture.setImageURI(uri);
        }

    }

    private void openGallery() {
        askForGalleryPermissions();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_IMAGE_FROM_GALLERY);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.ensure_your_all_permissions), Toast.LENGTH_SHORT).show();
        }
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

        }
    }
}