package com.poona.agrocart.ui.sign_up;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSelectLocationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.login.BasicDetails;
import com.poona.agrocart.ui.login.CommonViewModel;

import java.util.Objects;

public class SelectLocationFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSelectLocationBinding fragmentSelectLocationBinding;
    private final String[] cities={"Pune"};
    private final String[] areas={"Vishrantwadi", "Khadki"};
    private BasicDetails basicDetails;
    private CommonViewModel commonViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSelectLocationBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_select_location, container, false);
        fragmentSelectLocationBinding.setLifecycleOwner(this);
        final View view = fragmentSelectLocationBinding.getRoot();

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

        fragmentSelectLocationBinding.btnSubmit.setOnClickListener(this);
        fragmentSelectLocationBinding.ivPoonaAgroMainLogo.bringToFront();
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        basicDetails=new BasicDetails();

        /*Typeface poppinsRegularFont = Typeface.createFromAsset(getContext().getAssets(), getString(R.string.font_poppins_medium));
        fragmentSelectLocationBinding.spinnerArea.setTypeface(poppinsRegularFont);
        fragmentSelectLocationBinding.spinnerCity.setTypeface(poppinsRegularFont);*/

        setUpSpinnerCity();
        setUpSpinnerArea();
    }

    private void setUpSpinnerArea()
    {
        ArrayAdapter arrayAdapterArea=new ArrayAdapter(getActivity(),R.layout.text_spinner,areas);
        arrayAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSelectLocationBinding.spinnerArea.setAdapter(arrayAdapterArea);

        fragmentSelectLocationBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicDetails.setArea(parent.getItemAtPosition(position).toString());
                commonViewModel.area.setValue(basicDetails.getArea());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void setUpSpinnerCity()
    {
        ArrayAdapter arrayAdapterCity = new ArrayAdapter(getActivity(),R.layout.text_spinner,cities);
        arrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSelectLocationBinding.spinnerCity.setAdapter(arrayAdapterCity);

        fragmentSelectLocationBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                basicDetails.setCity(parent.getItemAtPosition(position).toString());
                commonViewModel.city.setValue(basicDetails.getCity());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public void onClick(View v)
    {
        redirectToLoginFragment(v);
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
                preferences.setIsLoggedIn(true);
                Intent intent = new Intent(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                requireActivity().finish();
                startActivity(intent);
            }
            else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }
    }
}