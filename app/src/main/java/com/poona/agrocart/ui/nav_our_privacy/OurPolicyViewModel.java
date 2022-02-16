package com.poona.agrocart.ui.nav_our_privacy;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OurPolicyViewModel extends ViewModel {
    private static final String TAG = OurPolicyViewModel.class.getSimpleName();

   public MutableLiveData<String> policyData1;
   public MutableLiveData<String> policyData2;
   public MutableLiveData<String> policyData3;
   public MutableLiveData<String> privacyPolicyData;

    public OurPolicyViewModel() {
        policyData1 = new MutableLiveData<>();
        policyData2 = new MutableLiveData<>();
        policyData3 = new MutableLiveData<>();
        privacyPolicyData = new MutableLiveData<>();
        policyData1.setValue(null);
        policyData2.setValue(null);
        policyData3.setValue(null);
        privacyPolicyData.setValue(null);
    }
}