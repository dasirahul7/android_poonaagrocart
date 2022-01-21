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

public class ReturnRefundFragment extends BaseFragment {
    private FragmentPolicyItemBinding returnPolicyBinding;
    private OurPolicyViewModel rtViewModel;
    private View returnViews;
    private String title = " Title";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        returnPolicyBinding = FragmentPolicyItemBinding.inflate(LayoutInflater.from(context));
        returnViews = returnPolicyBinding.getRoot();
        rtViewModel = new ViewModelProvider(this).get(OurPolicyViewModel.class);
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            title = bundle.getString("Policy_Title");
        }
        initTitleWithBackBtn(title);
        initViews();
        return returnViews;
    }

    private void initViews() {
        rtViewModel.privacyPolicyData.setValue(context.getString(R.string.sample_policy_data));
        returnPolicyBinding.setModelPolicy(rtViewModel);
        returnPolicyBinding.setVariable(BR.modelPolicy,rtViewModel);
        returnPolicyBinding.executePendingBindings();
    }
}
