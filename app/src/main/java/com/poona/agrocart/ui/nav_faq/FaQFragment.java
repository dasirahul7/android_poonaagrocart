package com.poona.agrocart.ui.nav_faq;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.databinding.FragmentFaqBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_faq.adaptor.FaqListAdaptor;
import com.poona.agrocart.ui.nav_faq.model.FaqListData;
import com.poona.agrocart.ui.nav_faq.model.FaqListResponse;

import java.util.ArrayList;
import java.util.List;

public class FaQFragment extends BaseFragment implements NetworkExceptionListener {

    private FaQViewModel mViewModel;
    private FragmentFaqBinding fragmentFaqBinding;
    private FaQViewModel faQViewModel;
    private List<FaqListData> faqListDataList = new ArrayList<>();
    private FaqListAdaptor faqListAdaptor;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private View view;

    public static FaQFragment newInstance() {
        return new FaQFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentFaqBinding = FragmentFaqBinding.inflate(LayoutInflater.from(context));
        view = fragmentFaqBinding.getRoot();
        faQViewModel = new ViewModelProvider(this).get(FaQViewModel.class);
        fragmentFaqBinding.setFaQViewModel(faQViewModel);
        initViews();

        setAdaptor();
        return view;
    }

    private void setAdaptor() {
        //Initializing our list
        faqListDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        if (isConnectingToInternet(context))
            callAddFaqsApi(showCircleProgressDialog(context, ""));
        else
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //initializing our adapter
        faqListAdaptor = new FaqListAdaptor(context, faqListDataList, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(faqListAdaptor);
    }

    private void initViews() {
        recyclerView = fragmentFaqBinding.rvFaq;
        initTitleBar(getString(R.string.menu_faq));

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FaQViewModel.class);
        // TODO: Use the ViewModel
    }

    /* Faqs api*/
    private void callAddFaqsApi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged")
        Observer<FaqListResponse> faqListResponseObserver = addFaqsResponse -> {
            if (addFaqsResponse != null) {
                Log.e("FAQs Api ResponseData", new Gson().toJson(addFaqsResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (addFaqsResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        faqListDataList.clear();
                        if (addFaqsResponse.getData() != null) {
                            faqListDataList.addAll(addFaqsResponse.getData());
                            faqListAdaptor.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, addFaqsResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, addFaqsResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, addFaqsResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, addFaqsResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };

        faQViewModel.getAddFaqsResponse(progressDialog, context, FaQFragment.this)
                .observe(getViewLifecycleOwner(), faqListResponseObserver);
    }

    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), FaQFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callAddFaqsApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }
}