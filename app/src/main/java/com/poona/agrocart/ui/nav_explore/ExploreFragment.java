package com.poona.agrocart.ui.nav_explore;

import static com.poona.agrocart.app.AppConstants.CATEGORY_ID;
import static com.poona.agrocart.app.AppConstants.LIST_TITLE;
import static com.poona.agrocart.app.AppConstants.LIST_TYPE;
import static com.poona.agrocart.app.AppConstants.SEARCH_KEY;
import static com.poona.agrocart.app.AppConstants.SEARCH_PRODUCT;
import static com.poona.agrocart.app.AppConstants.SEARCH_TYPE;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.CategoryResponse;
import com.poona.agrocart.data.network.responses.homeResponse.Category;
import com.poona.agrocart.databinding.FragmentExploreBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_explore.adapter.ExploreItemAdapter;
import com.poona.agrocart.ui.nav_explore.model.ExploreItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ExploreFragment extends BaseFragment implements View.OnClickListener, NetworkExceptionListener {

    private static final String TAG = ExploreFragment.class.getSimpleName();
    Timer timer = new Timer();
    boolean isTyping = false;
    private ExploreViewModel mViewModel;
    private FragmentExploreBinding exploreFragmentBinding;
    private ExploreItemAdapter exploreItemAdapter;
    private final int limit = 8;
    private int offset = 0;
    private final ArrayList<ExploreItems> exploreItems = new ArrayList<>();
    private View root;
    private int visibleItemCount = 0;
    private int totalCount = 0;

    private SwipeRefreshLayout pullToRefreshExplore;
    private ActivityResultLauncher<Intent> recognizerIntentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                ArrayList<String> resultArrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (resultArrayList.get(0) != null && !TextUtils.isEmpty(resultArrayList.get(0))) {
                    Bundle bundle = new Bundle();
                    bundle.putString(SEARCH_TYPE, SEARCH_PRODUCT);
                    bundle.putString(SEARCH_KEY, resultArrayList.get(0));

                    exploreFragmentBinding.etExploreSearch.setText(resultArrayList.get(0));
                }
            }
        }
    });
    private RecyclerView rvExplore;
    private GridLayoutManager gridLayoutManager;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exploreFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        exploreFragmentBinding.setLifecycleOwner(this);
        root = exploreFragmentBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        exploreItems.clear();
        initViews();
        if (isConnectingToInternet(context)) {
            callExploreApi( showCircleProgressDialog(context, ""),"load");
        } else
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

        initTitleBar(getString(R.string.menu_explore));
        searchCategory(root);

        pullToRefreshExplore.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshExplore.setRefreshing(true);
                if(isConnectingToInternet(context)){
                    callExploreApi(showCircleProgressDialog(context,""),"load");
                }else{
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

        setOnScrollListener();
        return root;
    }


    private void searchCategory(View root) {
        exploreFragmentBinding.etExploreSearch.addTextChangedListener(new TextWatcher() {
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
                                    if (isConnectingToInternet(context)) {
                                        callExploreApi(showCircleProgressDialog(context, ""),"load");
                                    } else
                                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

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

    private void callExploreApi(ProgressDialog progressDialog,String loadType) {
        if (loadType.equalsIgnoreCase("onScrolled"))
        offset = offset + 1;
        else offset = 0;
        Observer<CategoryResponse> categoryResponseObserver = categoryResponse -> {
            if (categoryResponse != null) {
                progressDialog.dismiss();
                pullToRefreshExplore.setRefreshing(false);
                Log.e("Category Api ResponseData", new Gson().toJson(categoryResponse));
                switch (categoryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if ((categoryResponse.getCategoryData() != null )&&(categoryResponse.getTotalCount()!=0)) {
                            totalCount = categoryResponse.getTotalCount();
                            if (categoryResponse.getCategoryData().getCategoryList().size() > 0) {
                                if (loadType.equalsIgnoreCase("load")){
                                    exploreItems.clear();
                                }
                                exploreFragmentBinding.tvNoData.setVisibility(View.GONE);
                                for (Category category : categoryResponse.getCategoryData().getCategoryList()) {
                                    ExploreItems expItem = new ExploreItems(category.getId(), category.getCategoryName(),
                                            category.getCategoryImage(), category.getCategoryType());
                                    expItem.setBackground(R.color.exp_card_color1);
                                    expItem.setBorder(R.color.exp_border1);
                                    exploreItems.add(expItem);
                                }
                                mViewModel.arrayListMutableLiveData.setValue(exploreItems);
                                setRvAdapters();
                            }
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
                        if (loadType.equalsIgnoreCase("load"))
                        exploreFragmentBinding.tvNoData.setVisibility(View.VISIBLE);
                        warningToast(context, "Data not found");
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(categoryResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        if (loadType.equalsIgnoreCase("load"))
                        exploreFragmentBinding.tvNoData.setVisibility(View.VISIBLE);
                        infoToast(context, categoryResponse.getMessage());
                        break;
                }
            }
        };
        mViewModel.categoryResponseLiveData(progressDialog, listingParams(limit, offset), ExploreFragment.this)
                .observe(getViewLifecycleOwner(), categoryResponseObserver);
    }

    private void setRvAdapters() {
        mViewModel.arrayListMutableLiveData.observe(getViewLifecycleOwner(),exploreItemsList -> {
            exploreItemAdapter = new ExploreItemAdapter(getActivity(), exploreItemsList, root, items -> {
                Bundle bundle = new Bundle();
                bundle.putString(CATEGORY_ID, items.getId());
                bundle.putString(LIST_TITLE, items.getName());
                bundle.putString(LIST_TYPE, items.getType());
                NavHostFragment.findNavController(ExploreFragment.this).navigate(R.id.action_nav_explore_to_nav_products_list, bundle);
            });
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            rvExplore.setLayoutManager(gridLayoutManager);
            rvExplore.setHasFixedSize(true);
            rvExplore.setAdapter(exploreItemAdapter);

        });

    }

    private void setOnScrollListener() {
        rvExplore.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = exploreFragmentBinding.nvExplore;
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = gridLayoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    if (isConnectingToInternet(context)) {
                        /*Call Our Store List API here*/
                        callExploreApi(showCircleProgressDialog(context, ""), "onScrolled");
                    } else
                        showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);

                }
//                else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
//                        && visibleItemCount == totalCount) {
//                    infoToast(requireActivity(), getString(R.string.no_result_found));  //change
//                }
            }
        });
    }


    private HashMap<String, String> listingParams(int limit, int offset) {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.LIMIT, String.valueOf(limit));
        map.put(AppConstants.OFFSET, String.valueOf(offset));
        map.put(AppConstants.SEARCH, exploreFragmentBinding.etExploreSearch.getText().toString());
        return map;
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onNetworkException(int from, String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), ExploreFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                switch (from) {
                    case 0:
                        exploreItems.clear();
                        callExploreApi( showCircleProgressDialog(context, ""),"load");
                        break;
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);

    }

    private void initViews() {
        rvExplore = exploreFragmentBinding.rvExplore;
        pullToRefreshExplore = exploreFragmentBinding.rlExplore;
        exploreFragmentBinding.imgExploreMice.setOnClickListener(view -> {
            startVoiceInput();
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search Store...");
        try {
            recognizerIntentLauncher.launch(intent);
        } catch (ActivityNotFoundException a) {

        }
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