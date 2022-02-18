package com.poona.agrocart.ui.nav_addresses;

import static com.poona.agrocart.app.AppConstants.ADDRESS_DETAILS;
import static com.poona.agrocart.app.AppConstants.ADDRESS_ID;
import static com.poona.agrocart.app.AppConstants.ADDRESS_TYPE;
import static com.poona.agrocart.app.AppConstants.APARTMENT_NAME;
import static com.poona.agrocart.app.AppConstants.AREA_;
import static com.poona.agrocart.app.AppConstants.CITY_;
import static com.poona.agrocart.app.AppConstants.HOUSE_NO;
import static com.poona.agrocart.app.AppConstants.LANDMARK;
import static com.poona.agrocart.app.AppConstants.MOBILE;
import static com.poona.agrocart.app.AppConstants.NAME;
import static com.poona.agrocart.app.AppConstants.PIN_CODE;
import static com.poona.agrocart.app.AppConstants.STATE_DETAILS;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.STREET;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.databinding.FragmentAddressesBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressesFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private static final String TAG = AddressesFragment.class.getSimpleName();
    private FragmentAddressesBinding fragmentAddressesBinding;
    private AddressesViewModel addressesViewModel;
    private RecyclerView rvAddress;
    private LinearLayoutManager linearLayoutManager;
    private AddressesAdapter addressesAdapter;
    private ArrayList<AddressesResponse.Address> addressArrayList;

    private View view;

    private String stateId = "";
    private String state = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentAddressesBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_addresses, container, false);

        addressesViewModel = new ViewModelProvider(this).get(AddressesViewModel.class);
        fragmentAddressesBinding.setAddressesViewModel(addressesViewModel);
        fragmentAddressesBinding.setLifecycleOwner(this);

        view = fragmentAddressesBinding.getRoot();

        initView();
        setRvAdapter();

        return view;
    }

    private int deletePosition = 0;
    private int editPosition = 0;
    private View itemView;
    private void setRvAdapter() {
        addressArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvAddress.setHasFixedSize(true);
        rvAddress.setLayoutManager(linearLayoutManager);

        addressesAdapter = new AddressesAdapter(addressArrayList);
        rvAddress.setAdapter(addressesAdapter);

        addressesAdapter.setOnEditButtonClickListener(position -> {
            this.editPosition = position;
            redirectToAddressFormWithData(view);
        });

        addressesAdapter.setOnDeleteButtonClickListener((itemView, position) -> {
            this.itemView = itemView;
            this.deletePosition = position;
            dialogDeleteAddress();
        });

        if (isConnectingToInternet(context)) {
            getAddressesListApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void initView() {
        rvAddress = fragmentAddressesBinding.rvAddress;
        fragmentAddressesBinding.btnAddAddress.setOnClickListener(this);
        initTitleBar(getString(R.string.menu_addresses));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_address:
                redirectToAddressForm(v);
                break;
        }
    }

    private void redirectToAddressForm(View v) {
        Bundle bundle = new Bundle();
        AddressesResponse.Address address = new AddressesResponse.Address();
        address.setStateId(stateId);
        address.setStateName(state);
        bundle.putSerializable(STATE_DETAILS, address);
        Navigation.findNavController(v).navigate(R.id.action_nav_address_to_addressesFormFragment2, bundle);
    }

    private void redirectToAddressFormWithData(View v) {
        Bundle bundle = new Bundle();
        AddressesResponse.Address address = addressArrayList.get(editPosition);
        bundle.putSerializable(ADDRESS_DETAILS, address);
        Navigation.findNavController(v).navigate(R.id.action_nav_address_to_addressesFormFragment2, bundle);
    }

    private void getAddressesListApi(ProgressDialog progressDialog) {
        androidx.lifecycle.Observer<AddressesResponse> responseObserver = addressesResponse -> {
            if (addressesResponse != null) {
                progressDialog.dismiss();
                Log.e("Addresses List Api ResponseData", new Gson().toJson(addressesResponse));
                switch (addressesResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        fragmentAddressesBinding.rlErrorMessage.setVisibility(View.GONE);
                        fragmentAddressesBinding.rvAddress.setVisibility(View.VISIBLE);
                        addressArrayList.addAll(addressesResponse.getAddresses());

                        if(addressArrayList != null && addressArrayList.size() > 0) {
                            for(int i = 0; i < addressArrayList.size(); i++) {
                                AddressesResponse.Address address = new AddressesResponse.Address();

                                String id = addressArrayList.get(i).getAddressPrimaryId();
                                String name = addressArrayList.get(i).getName();
                                String mobileNumber = addressArrayList.get(i).getMobile();
                                String addressType = addressArrayList.get(i).getAddressType();

                                if(addressType != null
                                        && !TextUtils.isEmpty(addressType)
                                        && addressType.equals("home"))
                                    addressType = "Home";
                                else if(addressType != null
                                        && !TextUtils.isEmpty(addressType)
                                        && addressType.equals("office"))
                                    addressType = "Office";
                                else if(addressType != null
                                        && !TextUtils.isEmpty(addressType)
                                        && addressType.equals("other"))
                                    addressType = "Other";

                                String houseNumber = addressArrayList.get(i).getHouseNo();
                                String apartmentName = addressArrayList.get(i).getAppartmentName();
                                String street = addressArrayList.get(i).getStreet();
                                String landmark = addressArrayList.get(i).getLandmark();
                                String stateId = addressArrayList.get(i).getStateId();
                                String state = addressArrayList.get(i).getStateName();
                                String cityId = addressArrayList.get(i).getCityId();
                                String city = addressArrayList.get(i).getCityName();
                                String areaId = addressArrayList.get(i).getAreaId();
                                String area = addressArrayList.get(i).getAreaName();
                                String pinCode = addressArrayList.get(i).getPincode();

                                StringBuilder fullAddressSb = new StringBuilder();
                                if(houseNumber != null && !TextUtils.isEmpty(houseNumber))
                                    fullAddressSb.append(houseNumber+", ");
                                if(apartmentName != null && !TextUtils.isEmpty(apartmentName))
                                    fullAddressSb.append(apartmentName+", ");
                                if(street != null && !TextUtils.isEmpty(street))
                                    fullAddressSb.append(street+", ");
                                if(landmark != null && !TextUtils.isEmpty(landmark))
                                    fullAddressSb.append(landmark+", ");
                                if(area != null && !TextUtils.isEmpty(area))
                                    fullAddressSb.append(area+", ");
                                if(city != null && !TextUtils.isEmpty(city))
                                    fullAddressSb.append(city+", ");
                                if(pinCode != null && !TextUtils.isEmpty(pinCode))
                                    fullAddressSb.append(pinCode);

                                address.setAddressPrimaryId(id);
                                address.setName(name);
                                address.setMobile(mobileNumber);
                                address.setAddressType(addressType);
                                address.setHouseNo(houseNumber);
                                address.setAppartmentName(apartmentName);
                                address.setStreet(street);
                                address.setLandmark(landmark);
                                address.setStateId(stateId);
                                address.setStateName(state);
                                address.setCityId(cityId);
                                address.setCityName(city);
                                address.setAreaId(areaId);
                                address.setAreaName(area);
                                address.setPincode(pinCode);
                                address.setFullAddress(fullAddressSb.toString());
                                address.setLatitude(fullAddressSb.toString());
                                address.setLongitude(fullAddressSb.toString());

                                addressArrayList.set(i, address);
                            }
                        }

                        stateId = addressesResponse.getStateDetails().getStateId();
                        state = addressesResponse.getStateDetails().getStateName();

                        addressesAdapter.notifyDataSetChanged();
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        goToAskAndDismiss(addressesResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://no records found
                        fragmentAddressesBinding.rlErrorMessage.setVisibility(View.VISIBLE);
                        fragmentAddressesBinding.rvAddress.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(addressesResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, addressesResponse.getMessage());
                        break;
                }
            } else {
                progressDialog.dismiss();
            }
        };

        addressesViewModel
                .getAddressesResponse(progressDialog, AddressesFragment.this)
                .observe(getViewLifecycleOwner(), responseObserver);
    }

    public void dialogDeleteAddress() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUp;
        dialog.setContentView(R.layout.dialog_delete_notification);

        ImageView closeImg = dialog.findViewById(R.id.iv_cross);
        CustomTextView tvHeading = dialog.findViewById(R.id.tv_heading);
        CustomButton buttonYes = dialog.findViewById(R.id.yes_btn);
        CustomButton buttonNo = dialog.findViewById(R.id.no_btn);

        tvHeading.setText("Do you really want to delete this address?");

        closeImg.setOnClickListener(view -> {
            dialog.dismiss();
        });

        buttonYes.setOnClickListener(view -> {
            dialog.dismiss();
            deleteAddressApi(showCircleProgressDialog(context, ""));
        });

        buttonNo.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void deleteAddressApi(ProgressDialog progressDialog) {
        /*print user input parameters*/
        for (Map.Entry<String, String> entry : deleteAddressParameters().entrySet()) {
            Log.e(TAG, "Key : " + entry.getKey() + " : " + entry.getValue());
        }

        androidx.lifecycle.Observer<BaseResponse> responseObserver = baseResponse -> {
            if (baseResponse != null) {
                progressDialog.dismiss();
                Log.e("Delete Address Api ResponseData", new Gson().toJson(baseResponse));
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        successToast(context, baseResponse.getMessage());
                        deleteItem();
                        break;
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_402://Validation Errors
                        goToAskAndDismiss(baseResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_404://no records found
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
                .deleteAddressResponse(progressDialog, AddressesFragment.this, deleteAddressParameters())
                .observe(getViewLifecycleOwner(), responseObserver);
    }

    private HashMap<String, String> deleteAddressParameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(ADDRESS_ID, addressArrayList.get(deletePosition).getAddressPrimaryId());
        return map;
    }

    private void deleteItem() {

        Animation anim = AnimationUtils.loadAnimation(requireContext(),
                android.R.anim.slide_out_right);
        anim.setDuration(300);
        itemView.startAnimation(anim);

        new Handler().postDelayed(() -> {
            addressArrayList.remove(deletePosition);
            addressesAdapter.notifyDataSetChanged();
            if(addressArrayList != null && addressArrayList.size() > 0) {
                fragmentAddressesBinding.rlErrorMessage.setVisibility(View.GONE);
                fragmentAddressesBinding.rvAddress.setVisibility(View.VISIBLE);
            } else {
                fragmentAddressesBinding.rlErrorMessage.setVisibility(View.VISIBLE);
                fragmentAddressesBinding.rvAddress.setVisibility(View.GONE);
            }
        }, anim.getDuration());
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), AddressesFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    getAddressesListApi(showCircleProgressDialog(context, ""));
                } else if(from == 1) {
                    deleteAddressApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}