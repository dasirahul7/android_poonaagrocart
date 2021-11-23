package com.poona.agrocart.ui.my_profile;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyProfileBinding;
import com.poona.agrocart.ui.BaseFragment;

public class MyProfileFragment extends BaseFragment {

  private FragmentMyProfileBinding binding;
  private MyProfileViewModel myProfileViewModel;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
         //   mParam1 = getArguments().getString(ARG_PARAM1);
          //  mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myProfileViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);
        binding = FragmentMyProfileBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        initTitleBar(getString(R.string.my_profile));
        return root;
    }
}