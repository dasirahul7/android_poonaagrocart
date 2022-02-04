package com.poona.agrocart.ui.nav_explore;

import static com.poona.agrocart.app.AppConstants.SEARCH_CATEGORY;
import static com.poona.agrocart.app.AppConstants.SEARCH_KEY;
import static com.poona.agrocart.app.AppConstants.SEARCH_PRODUCT;
import static com.poona.agrocart.app.AppConstants.SEARCH_TYPE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.databinding.FragmentExploreBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeFragment;
import com.poona.agrocart.ui.home.adapter.CategoryAdapter;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.nav_explore.adapter.ExploreItemAdapter;
import com.poona.agrocart.ui.nav_explore.model.ExploreItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final String TAG = ExploreFragment.class.getSimpleName();
    private ExploreViewModel mViewModel;
    private FragmentExploreBinding exploreFragmentBinding;
    private ExploreItemAdapter exploreItemAdapter;
    private int limit = 0;
    private int offset = 0;
    private ArrayList<ExploreItems> exploreItems = new ArrayList<>();
    private View root;
    Timer timer = new Timer();
    boolean isTyping = false;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exploreFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_explore,container,false);
        exploreFragmentBinding.setLifecycleOwner(this);
        root = exploreFragmentBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        exploreItems.clear();
        callExploreApi(root,showCircleProgressDialog(context,""),limit,offset);
        initTitleBar(getString(R.string.menu_explore));
        searchCategory(root);
        return root;
    }

    private void searchCategory(View root) {
        exploreFragmentBinding.etSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                BeforeSerach = fragmentHomeBinding.etSearch.getText().toString().trim();
            }

            public void afterTextChanged(Editable arg0) {
                if (!isTyping) {
                    Log.d(TAG, "started typing");
                    isTyping = true;
                }
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        isTyping = false;
                        Log.d(TAG, "stopped typing");
                        timer.cancel();
                        try {
                            hideKeyBoard(requireActivity());
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    exploreItems.clear();
                                    callExploreApi(root,showCircleProgressDialog(context,""),limit,offset);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1000);
            }
        });

    }

    private void callExploreApi(View root, ProgressDialog progressDialog, int limit, int offset) {
        limit = limit + 10;

        Observer<CategoryResponse> categoryResponseObserver = categoryResponse -> {
            if (categoryResponse != null) {
                progressDialog.dismiss();
                Log.e("Category Api Response", new Gson().toJson(categoryResponse));
                switch (categoryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if(categoryResponse.getCategoryData() != null){
                            if (categoryResponse.getCategoryData().getCategoryList().size()>0){
                                exploreFragmentBinding.tvNoData.setVisibility(View.GONE);
                                for (Category category : categoryResponse.getCategoryData().getCategoryList()){
                                    ExploreItems expItem = new ExploreItems(category.getId(),category.getCategoryName(),category.getCategoryImage());
                                    expItem.setBackground(R.color.exp_card_color1);
                                    expItem.setBorder(R.color.exp_border1);
                                    exploreItems.add(expItem);
                                }
                                exploreItemAdapter = new ExploreItemAdapter(getActivity(),exploreItems,root);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
                                exploreFragmentBinding.rvExplore.setLayoutManager(gridLayoutManager);
                                exploreFragmentBinding.rvExplore.setHasFixedSize(true);
                                exploreFragmentBinding.rvExplore.setAdapter(exploreItemAdapter);
                            }else exploreFragmentBinding.tvNoData.setVisibility(View.VISIBLE);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        exploreFragmentBinding.tvNoData.setVisibility(View.VISIBLE);
                        warningToast(context, "Data not found");
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        exploreFragmentBinding.tvNoData.setVisibility(View.VISIBLE);
                        goToAskSignInSignUpScreen(categoryResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        exploreFragmentBinding.tvNoData.setVisibility(View.VISIBLE);
                        infoToast(context, categoryResponse.getMessage());
                        break;
                }
            }
        };
        mViewModel.categoryResponseLiveData(progressDialog, listingParams(limit, offset), ExploreFragment.this)
                .observe(getViewLifecycleOwner(), categoryResponseObserver);
    }

    private HashMap<String, String> listingParams(int limit, int offset) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, exploreFragmentBinding.etSearch.getText().toString());
        return map;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetworkException(int from) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), ExploreFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from){
                    case 0:
                        exploreItems.clear();
                        callExploreApi(root,showCircleProgressDialog(context,""),limit,offset);
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }

//    private void setExploreList(View root) {
//        mViewModel.exploreMutableLiveData.observe(requireActivity(),exploreItems -> {
//            exploreItemAdapter = new ExploreItemAdapter(getActivity(),exploreItems,root);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
//            exploreFragmentBinding.rvExplore.setLayoutManager(gridLayoutManager);
//            exploreFragmentBinding.rvExplore.setHasFixedSize(true);
//            exploreFragmentBinding.rvExplore.setAdapter(exploreItemAdapter);
//        });
//    }

}