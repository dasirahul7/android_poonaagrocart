package com.poona.agrocart.ui.nav_gallery.fragment;


import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
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


import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;


import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryResponse;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryVideo;
import com.poona.agrocart.databinding.FragmentVideoBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.adapter.VideoAdapter;
import com.poona.agrocart.ui.nav_gallery.viewModel.GalleryViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoGalleryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoGalleryFragment extends BaseFragment implements VideoAdapter.OnVideoClickListener, NetworkExceptionListener {
   private List<GalleryVideo> galleryVideoList = new ArrayList<>();
    private FragmentVideoBinding videoFragmentBinding;
    private GalleryViewModel videoViewModel;
    private View videoView;
    private RecyclerView rvVideo;
    private VideoAdapter videoAdapter;
    private  ProgressBar progressDialog;
    private MediaController mediaController;


    public static VideoGalleryFragment newInstance() {
        VideoGalleryFragment fragment = new VideoGalleryFragment();

//        videosList = videoLiveData.getValue();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        initViews();
        Log.d("TAG", "onCreateView: video");
        return videoView;
    }

    @Override
    public void onResume() {
        Log.d("TAG", "onResume: video");
        super.onResume();
        if (isConnectingToInternet(context)) {
            callGalleryVideoApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(),getString(R.string.info),getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }

    private void initViews() {
        videoFragmentBinding = FragmentVideoBinding.inflate(LayoutInflater.from(context));
        videoView = videoFragmentBinding.getRoot();
        // Initialize ViewModel
        videoViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        //Initialize recyclerView
        rvVideo = videoFragmentBinding.rvVideo;
    }

    private void callGalleryVideoApi(ProgressDialog progressDialog){

        @SuppressLint("NotifyDataSetChanged") Observer<GalleryResponse> galleryResponseObserver = galleryResponse -> {
            if (galleryResponse != null){
                Log.e("Gallery Video Api ResponseData", new Gson().toJson(galleryResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (galleryResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully

                        if (galleryResponse.getData().getGalleryVideo() != null &&
                                galleryResponse.getData().getGalleryVideo().size() > 0){
                            galleryVideoList.clear();
                            galleryVideoList.addAll(galleryResponse.getData().getGalleryVideo());
                           setGallery(galleryVideoList);

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

        videoViewModel.getGalleryVideoResponse(progressDialog, context,
                VideoGalleryFragment.this)
                .observe(getViewLifecycleOwner(), galleryResponseObserver);
    }

    private void setGallery(List<GalleryVideo> galleryVideoList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager(linearLayoutManager);

        //initializing our adapter
        videoAdapter = new VideoAdapter(context, galleryVideoList,this);
        GridLayoutManager eLayoutManager = new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false);
        rvVideo.setLayoutManager(eLayoutManager);
        rvVideo.setItemAnimator(new DefaultItemAnimator());
        //Adding adapter to recyclerview
        rvVideo.setAdapter(videoAdapter);
    }

    /*Video Player Dialogue*/
    public void VideoPlayerDialog(int position) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.video_image_pop_up_dailog);
        progressDialog = dialog.findViewById(R.id.progress);
        VideoView videoView = dialog.findViewById(R.id.VideoView);
        progressDialog.setVisibility(View.VISIBLE);


       // videoView.setMediaController(new MediaController(getContext()));


       /* Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("**");  // change *//*
        i.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"video/mp4", "video/quicktime"});
        startActivityForResult(i, requestCode);*/
        /*
        Glide.with(context)
                .load(IMAGE_DOC_BASE_URL+galleryVideoList.get(position).getVideoImage())
                .into(imageView);*/

        //String strVideoView = galleryVideoList.get(position).getVideoUrl();
        String strVideoView = "https://jsoncompare.org/LearningContainer/SampleFiles/Video/MP4/sample-mp4-file.mp4";
        Uri uri = Uri.parse(strVideoView);
        videoView.setVideoURI(uri);
        videoView.setZOrderOnTop(true);
        //videoView.start();

        // perform set on prepared listener event on video view
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setZOrderOnTop(false);
                progressDialog.setVisibility(View.GONE);
                videoView.start();
               // mediaController.setAnchorView(videoView);
         // do something when video is ready to play

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            public void onCompletion(MediaPlayer mp)
            {
                // Do whatever u need to do here
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void itemViewClick(int position) {
        VideoPlayerDialog(position);
    }

    @Override
    public void onNetworkException(int from,String type) {

        showServerErrorDialog(getString(R.string.for_better_user_experience), VideoGalleryFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    //call Banner Api
                    callGalleryVideoApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}