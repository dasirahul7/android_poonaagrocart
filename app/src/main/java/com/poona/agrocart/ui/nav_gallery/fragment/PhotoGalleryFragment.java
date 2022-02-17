package com.poona.agrocart.ui.nav_gallery.fragment;

import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryImage;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryResponse;
import com.poona.agrocart.databinding.FragmentPhotoBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.adapter.PhotoAdapter;
import com.poona.agrocart.ui.nav_gallery.viewModel.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoGalleryFragment extends BaseFragment implements  PhotoAdapter.OnPhotoClickListener, NetworkExceptionListener {
    private FragmentPhotoBinding fragmentPhotoBinding;
    private GalleryViewModel photoViewModel;
    private View photoView;
    private List<GalleryImage> galleryImages = new ArrayList<>();
    private RecyclerView rvPhoto;
    private PhotoAdapter photoAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
      //  GalleryImage = photos.getValue();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initViews();
        callGalleryAPI();
        Log.e("TAG", "onCreateView: photo " );
        return photoView;
    }

    private void callGalleryAPI() {
        if (isConnectingToInternet(context)) {
            callGalleryImageApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(),getString(R.string.info),getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: photo");
        callGalleryAPI();
    }

    private void initViews() {
        fragmentPhotoBinding = FragmentPhotoBinding.inflate(LayoutInflater.from(context));
        photoView = fragmentPhotoBinding.getRoot();
        rvPhoto = fragmentPhotoBinding.rvPhoto;
        photoViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
    }

    private void setGallery(List<GalleryImage> galleryImages) {
       /* PhotoAdapter photoAdapter = new PhotoAdapter(getActivity(),photoViewModel.photoLiveData.getValue());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        rvPhoto.setLayoutManager(gridLayoutManager);
        rvPhoto.setAdapter(photoAdapter);*/


        linearLayoutManager = new LinearLayoutManager(context);
        rvPhoto.setHasFixedSize(true);
        rvPhoto.setLayoutManager(linearLayoutManager);

        //initializing our adapter
        photoAdapter = new PhotoAdapter(context, galleryImages, this);
        GridLayoutManager eLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        rvPhoto.setLayoutManager(eLayoutManager);
        rvPhoto.setItemAnimator(new DefaultItemAnimator());
        //Adding adapter to recyclerview
        rvPhoto.setAdapter(photoAdapter);
    }

    private void callGalleryImageApi(ProgressDialog progressDialog){

        @SuppressLint("NotifyDataSetChanged") Observer<GalleryResponse>galleryResponseObserver = galleryResponse -> {
            if (galleryResponse != null){
                Log.e("Gallery Image Api ResponseData", new Gson().toJson(galleryResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (galleryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully

                        if (galleryResponse.getData().getGalleryImage() != null &&
                        galleryResponse.getData().getGalleryImage().size() > 0){
                         galleryImages.clear();
                           galleryImages.addAll(galleryResponse.getData().getGalleryImage());
                            setGallery(galleryImages);

                            /*if(ourStoreListResponse.getData() != null){
                                llEmptyLayout.setVisibility(View.INVISIBLE);
                                llMainLayout.setVisibility(View.VISIBLE);
                            }*/
                        }
                        break;
                    case STATUS_CODE_404://Validation Errors

                        warningToast(context, galleryResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(galleryResponse.getMessage(), context);
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, galleryResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        photoViewModel.getGalleryImageResponse(progressDialog, context,
                PhotoGalleryFragment.this)
                .observe(getViewLifecycleOwner(), galleryResponseObserver);
    }

    /*Photo up  Dialogue*/
    public void PhotoUpDialog(int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.photo_image_pop_up_dailog);
        ImageView popUpImage = dialog.findViewById(R.id.iv_pop_up_image);
        Glide.with(context)
                .load(IMAGE_DOC_BASE_URL+galleryImages.get(position).getGalleryImage())
                .into(popUpImage);


        dialog.show();
    }

    @Override
    public void itemViewClick(int position) {
        PhotoUpDialog(position);
    }

    @Override
    public void onNetworkException(int from,String type) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), PhotoGalleryFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    //call Banner Api
                    callGalleryImageApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}