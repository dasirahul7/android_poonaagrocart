package com.poona.agrocart.ui.select_location;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSelectLocationBinding;
import com.poona.agrocart.ui.BaseFragment;

public class SelectLocationFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSelectLocationBinding fragmentSelectLocationBinding;
    private String[] cities={"Pune"};
    private String[] areas={"Vishrantwadi", "Khadki"};
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
        fragmentSelectLocationBinding.cbtnSubmit.setOnClickListener(this);
        fragmentSelectLocationBinding.ivPoonaAgroMainLogo.bringToFront();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        ArrayAdapter arrayAdapterCity = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,cities);
        arrayAdapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSelectLocationBinding.spinnerCity.setAdapter(arrayAdapterCity);
        ArrayAdapter arrayAdapterArea=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,areas);
        arrayAdapterArea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentSelectLocationBinding.spinnerArea.setAdapter(arrayAdapterArea);

        fragmentSelectLocationBinding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        fragmentSelectLocationBinding.spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
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
                //Navigation.findNavController(v).navigate(R.id.action_selectLocationFragment_to_LoginFragment);
                break;
        }
    }
}