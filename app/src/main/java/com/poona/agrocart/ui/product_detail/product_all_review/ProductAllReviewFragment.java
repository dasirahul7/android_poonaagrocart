package com.poona.agrocart.ui.product_detail.product_all_review;

import static com.poona.agrocart.app.AppConstants.REVIEW_LIST;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.data.network.responses.Review;
import com.poona.agrocart.databinding.FragmentProductAllReviewBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.product_detail.ProductDetailViewModel;
import com.poona.agrocart.ui.product_detail.adapter.ProductRatingReviewAdapter;
import com.poona.agrocart.ui.product_detail.model.AllReview;

import java.util.ArrayList;
import java.util.List;

public class ProductAllReviewFragment extends BaseFragment {

    private FragmentProductAllReviewBinding fragmentProductAllReviewBinding;
    private View root;
    private ArrayList<Review> reviewsList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView rvAllReview;
    private ProductRatingReviewAdapter productRatingReviewAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentProductAllReviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_all_review, container, false);
        fragmentProductAllReviewBinding.setLifecycleOwner(this);
        root = fragmentProductAllReviewBinding.getRoot();
        initTitleWithBackBtn("All Review");

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            reviewsList = bundle.getParcelableArrayList(REVIEW_LIST);
        }
        InitView();

        if (reviewsList.size() > 0){
            setAdaptor(reviewsList);
        }
        return root;
    }

    private void InitView() {
        rvAllReview=fragmentProductAllReviewBinding.rvAllReview;
    }

    private void setAdaptor(List<Review> reviewsList) {

        //Initializing our superheroes list
        this.reviewsList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvAllReview.setHasFixedSize(true);
        rvAllReview.setLayoutManager(linearLayoutManager);
        //initializing our adapter
        productRatingReviewAdapter = new ProductRatingReviewAdapter(context, reviewsList,1);
        //Adding adapter to recyclerview
        rvAllReview.setAdapter(productRatingReviewAdapter);
    }
}