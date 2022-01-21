package com.poona.agrocart.ui.nav_our_privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentPolicyItemBinding;
import com.poona.agrocart.databinding.FragmentTermsConditionBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_our_privacy.model.OurPolicyItem;

public class PrivacyPolicyFragment extends BaseFragment {
    private FragmentPolicyItemBinding privacyPolicyBinding;
    private OurPolicyViewModel prViewModel;
    private View privacyViews;
    private String title = " Title";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        privacyPolicyBinding = FragmentPolicyItemBinding.inflate(LayoutInflater.from(context));
        prViewModel = new ViewModelProvider(this).get(OurPolicyViewModel.class);
        privacyViews = privacyPolicyBinding.getRoot();
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            title = bundle.getString("Policy_Title");
        }
        initTitleWithBackBtn(title);
        initViews();
        return privacyViews;
    }

    private void initViews() {
        prViewModel.privacyPolicyData.setValue(context.getString(R.string.sample_policy_data));
        privacyPolicyBinding.setModelPolicy(prViewModel);
        privacyPolicyBinding.setVariable(BR.modelPolicy,prViewModel);
        privacyPolicyBinding.executePendingBindings();
    }
}
