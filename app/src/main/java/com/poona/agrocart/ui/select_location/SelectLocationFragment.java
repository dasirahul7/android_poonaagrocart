package com.poona.agrocart.ui.select_location;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSelectLocationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

public class SelectLocationFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSelectLocationBinding fragmentSelectLocationBinding;
    private String[] cities={"Pune"};
    private String[] areas={"Vishrantwadi", "Khadki"};
    private BasicDetails basicDetails;
    private CommonViewModel commonViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSelectLocationBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_select_location, container, false);
        fragmentSelectLocationBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentSelectLocationBinding).getRoot();

        initViews(view);

        ivBack.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });

        return view;
    }

    private void initViews(View view)
    {
        /*checkGpsStatus();*/
        commonViewModel = new ViewModelProvider(this).get(CommonViewModel.class);
        fragmentSelectLocationBinding.setCommonViewModel(commonViewModel);

        fragmentSelectLocationBinding.cbtnSubmit.setOnClickListener(this);
        fragmentSelectLocationBinding.ivPoonaAgroMainLogo.bringToFront();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        basicDetails=new BasicDetails();

        ArrayAdapter arrayAdapterCity = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,cities);
        arrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSelectLocationBinding.spinnerCity.setAdapter(arrayAdapterCity);

        ArrayAdapter arrayAdapterArea=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,areas);
        arrayAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSelectLocationBinding.spinnerArea.setAdapter(arrayAdapterArea);

        fragmentSelectLocationBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicDetails.setCity(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        fragmentSelectLocationBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicDetails.setArea(parent.getItemAtPosition(position).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cbtn_submit:
                redirectToLoginFragment(v);
                break;
        }
    }

    private void redirectToLoginFragment(View v)
    {
        commonViewModel.city.setValue(fragmentSelectLocationBinding.spinnerCity.toString());
        commonViewModel.area.setValue(fragmentSelectLocationBinding.spinnerArea.toString());

        basicDetails.setCity(fragmentSelectLocationBinding.spinnerCity.toString());
        basicDetails.setArea(fragmentSelectLocationBinding.spinnerArea.toString());

        int errorCodeForCity=basicDetails.isValidCity();
        int errorCodeForArea=basicDetails.isValidArea();

        if(errorCodeForCity==0)
        {
            errorToast(requireActivity(),getString(R.string.please_select_city));
        }
        else if(errorCodeForArea==0)
        {
            errorToast(requireActivity(),getString(R.string.please_select_area));
        }
        else
        {
            if (isConnectingToInternet(context)) {
                //add API call here
                Navigation.findNavController(v).navigate(R.id.action_selectLocationFragment_to_LoginFragment);
            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }
}