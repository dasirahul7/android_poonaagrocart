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
import com.poona.agrocart.databinding.FragmentAboutUsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.data.network.reponses.CmsResponse;

import java.util.ArrayList;
import java.util.List;

public class CmsFragment extends BaseFragment {
    private CmsViewModel cmsViewModel;
    private FragmentAboutUsBinding fragmentAboutUsBinding;
    private List<CmsResponse.Cms> cmsList = new ArrayList<>();
    private String cmsContent = "";
    private View view;

    public static CmsFragment newInstance() {
        return new CmsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentAboutUsBinding = FragmentAboutUsBinding.inflate(getLayoutInflater());
        fragmentAboutUsBinding.setLifecycleOwner(this);

        cmsViewModel = new ViewModelProvider(this).get(CmsViewModel.class);
        fragmentAboutUsBinding.setAboutUsViewModel(cmsViewModel);

        view = fragmentAboutUsBinding.getRoot();

        initTitleBar(getString(R.string.menu_about_us));

        if (isConnectingToInternet(context)) {
            callCmsApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        return view;
    }


    /* About us api*/
    private void callCmsApi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged")
        Observer<CmsResponse> aboutUsResponseObserver = cmsResponse -> {
            if (cmsResponse != null) {
                Log.e("Cms Api Response", new Gson().toJson(cmsResponse));
                if (progressDialog !=null) {
                    progressDialog.dismiss();
                }
                switch (cmsResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if(cmsResponse.getData() != null &&
                        cmsResponse.getData().size() > 0) {
                            cmsList = cmsResponse.getData();
                            cmsContent = cmsList.get(0).getCmsText();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                fragmentAboutUsBinding.tvAboutUs.setText(Html.fromHtml(""+ cmsContent, Html.FROM_HTML_MODE_COMPACT));
                                //fragmentAboutUsBinding.tvDescription.setText(Html.fromHtml(""+strAboutDescription, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                fragmentAboutUsBinding.tvAboutUs.setText(Html.fromHtml(""+ cmsContent));
                                //fragmentAboutUsBinding.tvDescription.setText(Html.fromHtml(""+strAboutDescription));
                            }
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, cmsResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, cmsResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, cmsResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, cmsResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        cmsViewModel.getCmsResponse(progressDialog, context, CmsFragment.this)
                .observe(getViewLifecycleOwner(), aboutUsResponseObserver);
    }
}