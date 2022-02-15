package com.poona.agrocart.ui.nav_addresses;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_402;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.AddressesResponse;
import com.poona.agrocart.databinding.FragmentAddressesBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_addresses.model.Address;

import java.util.ArrayList;

public class AddressesFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {
    private FragmentAddressesBinding fragmentAddressesBinding;
    private AddressesViewModel addressesViewModel;
    private RecyclerView rvAddress;
    private LinearLayoutManager linearLayoutManager;
    private AddressesAdapter addressesAdapter;
    private ArrayList<AddressesResponse.Address> addressArrayList;

    private View view;

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

    private void setRvAdapter() {
        addressArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvAddress.setHasFixedSize(true);
        rvAddress.setLayoutManager(linearLayoutManager);

        addressesAdapter = new AddressesAdapter(addressArrayList);
        rvAddress.setAdapter(addressesAdapter);

        if (isConnectingToInternet(context)) {
            getAddressesListApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void initView() {
        rvAddress=fragmentAddressesBinding.rvAddress;
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
        Navigation.findNavController(v).navigate(R.id.action_nav_address_to_addressesFormFragment2);
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

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), AddressesFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    getAddressesListApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}