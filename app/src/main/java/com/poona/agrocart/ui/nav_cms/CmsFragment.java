package com.poona.agrocart.ui.nav_cms;

import static com.poona.agrocart.app.AppConstants.CMS_TYPE;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.CmsResponse;
import com.poona.agrocart.databinding.FragmentCmsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.sign_in.SignInFragment;
import com.poona.agrocart.ui.sign_up.SignUpFragment;

import java.util.ArrayList;
import java.util.List;

public class CmsFragment extends BaseFragment implements NetworkExceptionListener {
    private final String fromScreenSignIn = SignInFragment.class.getSimpleName();
    private final String fromScreenSignUp = SignUpFragment.class.getSimpleName();
    private final String fromScreenHome = HomeActivity.class.getSimpleName();
    private CmsViewModel cmsViewModel;
    private FragmentCmsBinding fragmentCmsBinding;
    private List<CmsResponse.Cms> cmsList = new ArrayList<>();
    private String cmsContent = "";
    private WebView wvCms;
    private View view;
    private String fromScreen = "";
    private int cmsType = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentCmsBinding = FragmentCmsBinding.inflate(getLayoutInflater());
        fragmentCmsBinding.setLifecycleOwner(this);

        cmsViewModel = new ViewModelProvider(this).get(CmsViewModel.class);
        fragmentCmsBinding.setCmsViewModel(cmsViewModel);

        view = fragmentCmsBinding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null) {
            fromScreen = bundle.getString(FROM_SCREEN);
            cmsType = bundle.getInt(CMS_TYPE);
        }

        /*visible back button action bar if navigating from  sign in screen*/
        if (fromScreen.equals(fromScreenSignIn) || fromScreen.equals(fromScreenSignUp)) {
            fragmentCmsBinding.actionBar.setVisibility(View.VISIBLE);
            /*set title to toolbar*/
            if (cmsType == 0) {
                fragmentCmsBinding.tvTitle.setText(R.string.about_us);
            } else if (cmsType == 1) {
                fragmentCmsBinding.tvTitle.setText(R.string.terms_and_conditions);
            } else if (cmsType == 2) {
                fragmentCmsBinding.tvTitle.setText(R.string.privacy_policy);
            } else if (cmsType == 3) {
                fragmentCmsBinding.tvTitle.setText(R.string.return_and_refund_policy);
            }
        } else if (fromScreen.equals(fromScreenHome)) {
            fragmentCmsBinding.actionBar.setVisibility(View.GONE);
            if (cmsType == 0) {
                initTitleWithBackBtn(getString(R.string.about_us));
            } else if (cmsType == 1) {
                initTitleWithBackBtn(getString(R.string.terms_and_conditions));
            } else if (cmsType == 2) {
                initTitleWithBackBtn(getString(R.string.privacy_policy));
            } else if (cmsType == 3) {
                initTitleWithBackBtn(getString(R.string.return_and_refund_policy));
            }
        }

        wvCms = fragmentCmsBinding.wvCms;

        if (isConnectingToInternet(context)) {
            callCmsApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        fragmentCmsBinding.ivBack.setOnClickListener(view -> NavHostFragment.findNavController(this).popBackStack());

        return view;
    }


    /* About us api*/
    private void callCmsApi(ProgressDialog progressDialog) {
        Observer<CmsResponse> aboutUsResponseObserver = cmsResponse -> {
            if (cmsResponse != null) {
                Log.e("Cms Api ResponseData", new Gson().toJson(cmsResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (cmsResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (cmsResponse.getData() != null && cmsResponse.getData().size() > 0) {
                            cmsList = cmsResponse.getData();
                            if (cmsType == 0) {
                                cmsContent = cmsList.get(0).getCmsText(); //About Us
                            } else if (cmsType == 1) {
                                cmsContent = cmsList.get(1).getCmsText(); //Terms & Condition
                            } else if (cmsType == 2) {
                                cmsContent = cmsList.get(2).getCmsText(); //Privacy Policy
                            } else if (cmsType == 3) {
                                cmsContent = cmsList.get(3).getCmsText(); //Return and Refund Policy
                            }

                            if (cmsContent != null && !TextUtils.isEmpty(cmsContent)) {
                                fragmentCmsBinding.wvCms.setVisibility(View.VISIBLE);
                                fragmentCmsBinding.rlMessage.setVisibility(View.GONE);
                                String htmlText = "<html><body style=\"text-align:justify\"> %s </body></Html>";
                                wvCms.getSettings().setJavaScriptEnabled(true);
                                //wvCms.setBackgroundColor(Color.parseColor("#233D69"));
                                wvCms.getSettings().setMinimumFontSize(40);
                                wvCms.getSettings().setDomStorageEnabled(true);
                                wvCms.getSettings().setUseWideViewPort(true);
                                wvCms.getSettings().setLoadWithOverviewMode(true);
                                wvCms.setWebChromeClient(new WebChromeClient());
                                wvCms.setWebViewClient(new WebViewClient());
                                wvCms.loadData(String.format(htmlText, cmsContent), "text/html", "UTF-8");
                            } else {
                                fragmentCmsBinding.wvCms.setVisibility(View.GONE);
                                fragmentCmsBinding.rlMessage.setVisibility(View.VISIBLE);

                                if (cmsType == 0) {
                                    fragmentCmsBinding.tvMessage.setText(R.string.error_message_about_us);
                                } else if (cmsType == 1) {
                                    fragmentCmsBinding.tvMessage.setText(R.string.error_message_terms_and_conditions);
                                } else if (cmsType == 2) {
                                    fragmentCmsBinding.tvMessage.setText(R.string.error_message_privacy_policy);
                                } else if (cmsType == 3) {
                                    fragmentCmsBinding.tvMessage.setText(R.string.error_message_refund_policy);
                                }
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
//                        warningToast(context, cmsResponse.getMessage());
                        goToAskSignInSignUpScreen(cmsResponse.getMessage(),context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, cmsResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        cmsViewModel.getCmsResponse(progressDialog, context, CmsFragment.this)
                .observe(getViewLifecycleOwner(), aboutUsResponseObserver);
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), CmsFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callCmsApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}