package com.poona.agrocart.ui.nav_about_us;

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
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.databinding.FragmentAboutUsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.intro.IntroScreenFragment;
import com.poona.agrocart.ui.nav_about_us.model.CmsPagesData;
import com.poona.agrocart.ui.nav_about_us.model.CmsResponse;

import java.util.ArrayList;
import java.util.List;

public class AboutUsFragment extends BaseFragment implements NetworkExceptionListener {

    private AboutUsViewModel aboutUsViewModel;
    private FragmentAboutUsBinding fragmentAboutUsBinding;
    private List<CmsPagesData> cmsPagesDataList = new ArrayList<>();
    private String aboutUsContent = "";
    private View view;

    public static AboutUsFragment newInstance() {
        return new AboutUsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentAboutUsBinding = FragmentAboutUsBinding.inflate(getLayoutInflater());
        fragmentAboutUsBinding.setLifecycleOwner(this);

        aboutUsViewModel = new ViewModelProvider(this).get(AboutUsViewModel.class);
        fragmentAboutUsBinding.setAboutUsViewModel(aboutUsViewModel);

        view = fragmentAboutUsBinding.getRoot();

        initTitleBar(getString(R.string.menu_about_us));
        if (isConnectingToInternet(context)){
            callAboutUsApi(showCircleProgressDialog(context, ""));
        }else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        return view;
    }


    /* About us api*/
    private void callAboutUsApi(ProgressDialog progressDialog) {

        @SuppressLint("NotifyDataSetChanged")
        Observer<CmsResponse> aboutUsResponseObserver = cmsPagesDataResponse -> {

            if (cmsPagesDataResponse != null){
                Log.e("About us Api Response", new Gson().toJson(cmsPagesDataResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (cmsPagesDataResponse.getStatus()) {
                    case STATUS_CODE_200://success

                        if(cmsPagesDataResponse.getData() != null &&
                        cmsPagesDataResponse.getData().size() > 0){
                            cmsPagesDataList = cmsPagesDataResponse.getData();
                            aboutUsContent = cmsPagesDataList.get(0).getCmsText();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                fragmentAboutUsBinding.tvAboutUs.setText(Html.fromHtml(""+aboutUsContent, Html.FROM_HTML_MODE_COMPACT));
                                // fragmentAboutUsBinding.tvDescription.setText(Html.fromHtml(""+strAboutDescription, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                fragmentAboutUsBinding.tvAboutUs.setText(Html.fromHtml(""+aboutUsContent));
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

        aboutUsViewModel.getAboutUsResponse(progressDialog, context, AboutUsFragment.this)
                .observe(getViewLifecycleOwner(), aboutUsResponseObserver);
    }

    @Override
    public void onNetworkException(int from,String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), AboutUsFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callAboutUsApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}