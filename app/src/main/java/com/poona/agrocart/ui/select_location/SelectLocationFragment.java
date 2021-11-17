package com.poona.agrocart.ui.select_location;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSelectLocationBinding;
import com.poona.agrocart.databinding.FragmentSelectLocationBindingImpl;
import com.poona.agrocart.ui.BaseFragment;

public class SelectLocationFragment extends BaseFragment implements View.OnClickListener {

    private FragmentSelectLocationBinding fragmentSelectLocationBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSelectLocationBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_select_location, container, false);
        fragmentSelectLocationBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentSelectLocationBinding).getRoot();

        initViews(view);

        return view;
    }

    private void initViews(View view)
    {
        fragmentSelectLocationBinding.cbtnSubmit.setOnClickListener(this);
        fragmentSelectLocationBinding.ivPoonaAgroMainLogo.bringToFront();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cbtn_submit:
                Navigation.findNavController(v).navigate(R.id.action_selectLocationFragment_to_signInFragment);
                break;
        }

    }
}