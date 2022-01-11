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
import com.poona.agrocart.ui.BaseFragment;

public class TermsConditionFragment extends BaseFragment {
    private FragmentPolicyItemBinding termsPolicyBinding;
    private OurPolicyViewModel tmViewModel;
    private View termsViews;
    private String title = " Title";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        termsPolicyBinding = FragmentPolicyItemBinding.inflate(LayoutInflater.from(context));
        tmViewModel = new ViewModelProvider(this).get(OurPolicyViewModel.class);
        termsViews = termsPolicyBinding.getRoot();
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            title = bundle.getString("Policy_Title");
        }
        initTitleWithBackBtn(title);
        initViews();
        return termsViews;
    }

    private void initViews() {
        tmViewModel.privacyPolicyData.setValue(context.getString(R.string.sample_policy_data));
        termsPolicyBinding.setModelPolicy(tmViewModel);
        termsPolicyBinding.setVariable(BR.modelPolicy,tmViewModel);
        termsPolicyBinding.executePendingBindings();
    }
}
