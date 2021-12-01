package com.poona.agrocart.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSettingsBinding;
import com.poona.agrocart.ui.BaseFragment;

public class SettingsFragment extends BaseFragment
{
    private FragmentSettingsBinding fragmentSettingsBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentSettingsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false);
        fragmentSettingsBinding.setLifecycleOwner(this);
        final View view = ((ViewDataBinding) fragmentSettingsBinding).getRoot();

        /*initView();
        setRVAdapter();*/

        initTitleBar(getString(R.string.settings));

        return view;
    }
}