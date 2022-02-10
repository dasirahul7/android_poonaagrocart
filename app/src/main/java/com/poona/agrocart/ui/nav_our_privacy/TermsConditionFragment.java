package com.poona.agrocart.ui.nav_our_privacy;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.poona.agrocart.BR;
import com.poona.agrocart.databinding.FragmentPolicyItemBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_about_us.model.CmsPagesData;
import com.poona.agrocart.ui.nav_about_us.model.CmsPagesDataResponse;

import java.util.ArrayList;
import java.util.List;

public class TermsConditionFragment extends BaseFragment {
    private FragmentPolicyItemBinding fragmentPolicyItemBinding;
    private OurPolicyViewModel ourPolicyViewModel;
    private View view;
    private String title = " Title";
    private List<CmsPagesData> cmsPagesDataList = new ArrayList<>();
    private String termsConditionContent = "";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPolicyItemBinding = FragmentPolicyItemBinding.inflate(getLayoutInflater());
        fragmentPolicyItemBinding.setLifecycleOwner(this);

        ourPolicyViewModel = new ViewModelProvider(this).get(OurPolicyViewModel.class);
        fragmentPolicyItemBinding.setModelPolicy(ourPolicyViewModel);

        view = fragmentPolicyItemBinding.getRoot();
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            title = bundle.getString("Policy_Title");
        }
       initTitleWithBackBtn(title);  //change
        initViews();

       callTermsConditionApi(showCircleProgressDialog(context,""));

        return view;
    }

    private void initViews() {
        fragmentPolicyItemBinding.setModelPolicy(ourPolicyViewModel);
        fragmentPolicyItemBinding.setVariable(BR.modelPolicy, ourPolicyViewModel);
        fragmentPolicyItemBinding.executePendingBindings();
    }

    /* Terms Condition api*/
    private void callTermsConditionApi(ProgressDialog progressDialog) {

        @SuppressLint("NotifyDataSetChanged")
        Observer<CmsPagesDataResponse> cmsPagesDataResponseObserver = cmsPagesDataResponse -> {

            if (cmsPagesDataResponse != null){
                Log.e("Privacy and Policy Api Response", new Gson().toJson(cmsPagesDataResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (cmsPagesDataResponse.getStatus()) {
                    case STATUS_CODE_200://success

                        if(cmsPagesDataResponse.getData() != null ){
                            cmsPagesDataList = cmsPagesDataResponse.getData();
                            termsConditionContent = cmsPagesDataList.get(1).getCmsText();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                fragmentPolicyItemBinding.tvContent.setText(Html.fromHtml(""+ termsConditionContent, Html.FROM_HTML_MODE_COMPACT));
                                // fragmentAboutUsBinding.tvDescription.setText(Html.fromHtml(""+strAboutDescription, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                fragmentPolicyItemBinding.tvContent.setText(Html.fromHtml(""+ termsConditionContent));
                                // fragmentAboutUsBinding.tvDescription.setText(Html.fromHtml(""+strAboutDescription));
                            }
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, cmsPagesDataResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, cmsPagesDataResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, cmsPagesDataResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, cmsPagesDataResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        ourPolicyViewModel.getTermsConditionResponse(progressDialog, context, TermsConditionFragment.this)
                .observe(getViewLifecycleOwner(), cmsPagesDataResponseObserver);
    }
}
