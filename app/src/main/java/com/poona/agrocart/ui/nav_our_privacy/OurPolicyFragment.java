package com.poona.agrocart.ui.nav_our_privacy;

import static com.poona.agrocart.app.AppConstants.CMS_TYPE;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;

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
import com.poona.agrocart.ui.home.HomeActivity;

public class OurPolicyFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private OurPolicyViewModel mViewModel;
    private FragmentPolicyBinding ourPolicyBinding;
    private View view;

    public static OurPolicyFragment newInstance() {
        return new OurPolicyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ourPolicyBinding = FragmentPolicyBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(OurPolicyViewModel.class);
        ourPolicyBinding.setLifecycleOwner(this);
        view = ourPolicyBinding.getRoot();
        initTitleBar(getString(R.string.privacy_policy));
        iniViews();
        return view;
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
                redirectToCmsFragment(2); //Privacy Policy
                break;
            case R.id.tv_terms:
                redirectToCmsFragment(1); //Terms & Conditions
                break;
            case R.id.tv_return:
                redirectToCmsFragment(3); //Return & Refund Policy
                break;
        }
    }

    private void redirectToCmsFragment(int from) {
        Bundle bundle = new Bundle();
        bundle.putString(FROM_SCREEN, TAG);
        bundle.putInt(CMS_TYPE, from);
        Navigation.findNavController(view).navigate(R.id.action_nav_cms, bundle);
    }
}