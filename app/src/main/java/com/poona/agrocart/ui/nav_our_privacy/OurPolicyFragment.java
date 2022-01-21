package com.poona.agrocart.ui.nav_our_privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.poona.agrocart.BR;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentPolicyBinding;
import com.poona.agrocart.ui.BaseFragment;

public class OurPolicyFragment extends BaseFragment implements View.OnClickListener {

    private OurPolicyViewModel mViewModel;
    private FragmentPolicyBinding ourPolicyBinding;
    private View policyViews;
    private Bundle bundle = new Bundle();

    public static OurPolicyFragment newInstance() {
        return new OurPolicyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ourPolicyBinding = FragmentPolicyBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(OurPolicyViewModel.class);
        ourPolicyBinding.setLifecycleOwner(this);
        policyViews = ourPolicyBinding.getRoot();
        initTitleBar(getString(R.string.privacy_policy));
        iniViews();
        return policyViews;
    }

    private void iniViews() {
        mViewModel.policyData1.setValue(context.getString(R.string.privacy_policy));
        mViewModel.policyData2.setValue(context.getString(R.string.terms_condition));
        mViewModel.policyData3.setValue(context.getString(R.string.refund_and_return));
        ourPolicyBinding.setPolicyModule(mViewModel);
        ourPolicyBinding.setVariable(BR.policyModule, mViewModel);
        ourPolicyBinding.executePendingBindings();
        // set all clicks here
        ourPolicyBinding.tvPolicy.setOnClickListener(this);
        ourPolicyBinding.tvTerms.setOnClickListener(this);
        ourPolicyBinding.tvReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_policy:
                bundle.putString("Policy_Title",getString(R.string.privacy_policy));
                moveToPrivacyPolicy(policyViews);
                break;
            case R.id.tv_terms:
                bundle.putString("Policy_Title",getString(R.string.terms_condition));
                moveToTermsConditions(policyViews);
                break;
            case R.id.tv_return:
                bundle.putString("Policy_Title",getString(R.string.refund_and_return));
                moveToReturnRefund(policyViews);
                break;
        }
    }

    private void moveToReturnRefund(View policyViews) {
        Navigation.findNavController(policyViews).navigate(R.id.action_nav_privacy_to_returnRefundFragment,bundle);
    }

    private void moveToTermsConditions(View policyViews) {
        Navigation.findNavController(policyViews).navigate(R.id.action_nav_privacy_to_termsConditionFragment,bundle);
    }

    private void moveToPrivacyPolicy(View policyViews) {
        Navigation.findNavController(policyViews).navigate(R.id.action_nav_privacy_to_privacyPolicyFragment,bundle);
    }
}