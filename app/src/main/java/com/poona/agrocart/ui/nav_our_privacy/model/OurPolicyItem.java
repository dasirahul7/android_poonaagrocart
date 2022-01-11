package com.poona.agrocart.ui.nav_our_privacy.model;

import androidx.lifecycle.MutableLiveData;

public class OurPolicyItem {
    private String policyData1;
    private String policyData2;
    private String policyData3;
    private String privacyPolicyData;
    private String termsConditionData;
    private String returnAndRefundData;

    public void setPolicyData1(String policyData1) {
        this.policyData1 = policyData1;
    }

    public void setPolicyData2(String policyData2) {
        this.policyData2 = policyData2;
    }

    public void setPolicyData3(String policyData3) {
        this.policyData3 = policyData3;
    }

    public void setPrivacyPolicyData(String privacyPolicyData) {
        this.privacyPolicyData = privacyPolicyData;
    }

    public void setTermsConditionData(String termsConditionData) {
        this.termsConditionData = termsConditionData;
    }

    public void setReturnAndRefundData(String returnAndRefundData) {
        this.returnAndRefundData = returnAndRefundData;
    }

    public String getPolicyData1() {
        return policyData1;
    }

    public String getPolicyData2() {
        return policyData2;
    }

    public String getPolicyData3() {
        return policyData3;
    }

    public String getPrivacyPolicyData() {
        return privacyPolicyData;
    }

    public String getTermsConditionData() {
        return termsConditionData;
    }

    public String getReturnAndRefundData() {
        return returnAndRefundData;
    }
}
