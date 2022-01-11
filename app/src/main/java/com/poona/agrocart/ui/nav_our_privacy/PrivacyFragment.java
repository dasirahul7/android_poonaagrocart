package com.poona.agrocart.ui.nav_our_privacy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentPolicyBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;

public class PrivacyFragment extends BaseFragment {

    private PrivacyViewModel mViewModel;
    private FragmentPolicyBinding privacyPolicyBinding;

    public static PrivacyFragment newInstance() {
        return new PrivacyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        privacyPolicyBinding = FragmentPolicyBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(PrivacyViewModel.class);
        privacyPolicyBinding.setLifecycleOwner(this);
        View view = privacyPolicyBinding.getRoot();
        initTitleBar(getString(R.string.privacy_policy));
        //TODO here app crashes
        setupSpinner();
        return view;
    }

    private void setupSpinner() {
        ArrayList<PolicyItem> policyList = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(getString(R.string.privacy_policy));
//        arrayList.add(getString(R.string.privacy_policy));
//        arrayList.add(getString(R.string.menu_our_policy));
        policyList.add(new PolicyItem(getString(R.string.policy_p), R.drawable.ic_privacy_policy));
        policyList.add(new PolicyItem(getString(R.string.policy_t), R.drawable.ic_privacy_policy));
        policyList.add(new PolicyItem(getString(R.string.policy_r), R.drawable.ic_privacy_policy));
////        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.row_policy_item,R.id.tv_policy,arrayList);
////        arrayAdapter.setDropDownViewResource(R.layout.row_policy_item);
////        privacyPolicyBinding.privacySpinner.setAdapter(arrayAdapter);
        PolicySpinnerAdapter policyAdapterAdapter = new PolicySpinnerAdapter(getContext(), policyList);
        policyAdapterAdapter.setDropDownViewResource(R.layout.row_policy_item);
        privacyPolicyBinding.privacySpinner.setAdapter(policyAdapterAdapter);
    }
}