package com.poona.agrocart.ui.nav_gallery.fragment;


import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RendererCapabilities;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.TracksInfo;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectorResult;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
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
    private PlayerView playerView;
    private SimpleExoPlayer simpleExoPlayer;


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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.video_image_pop_up_dailog);
        playerView = dialog.findViewById(R.id.pv_video_player);
        ProgressBar progressBar = dialog.findViewById(R.id.progress_bar);
        ImageView crossImage = dialog.findViewById(R.id.iv_close_dialog);


        String strVideoView = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
       // String strVideoView = galleryVideoList.get(position).getVideoUrl();

        /*String test = strVideoView.substring(strVideoView.lastIndexOf("."));
        if(test.equalsIgnoreCase(".mp4") || test.equalsIgnoreCase(".avi")
                || test.equalsIgnoreCase(".mkv") || test.equalsIgnoreCase(".mov")){
            simpleExoPlayer.prepare();
        }else {

            progressDialog.setVisibility(View.GONE);
            infoToast(context, "please check the extension .mp4, .avi, .mkv, .mov");
        }*/


        simpleExoPlayer = new SimpleExoPlayer.Builder(context).build();
        playerView.setPlayer(simpleExoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(strVideoView);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

        ImageView pause = playerView.findViewById(R.id.video_pause);
        ImageView start = playerView.findViewById(R.id.video_play);
        ImageView forward = playerView.findViewById(R.id.forward);

        forward.setOnClickListener(view -> {
            simpleExoPlayer.seekForward();
        });

        start.setOnClickListener(view -> {
            if(simpleExoPlayer.isPlaying()){
                pause.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                simpleExoPlayer.setPlayWhenReady(false);
            }
        });

        pause.setOnClickListener(view -> {

            start.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.getPlaybackState();
        });

        simpleExoPlayer.addListener(new Player.Listener() {

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                switch(playbackState) {
                    case ExoPlayer.STATE_BUFFERING:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case ExoPlayer.STATE_ENDED:
                        simpleExoPlayer.seekTo(0);
                        simpleExoPlayer.setPlayWhenReady(false);
                        break;
                    case ExoPlayer.STATE_IDLE:
                        break;
                    case ExoPlayer.STATE_READY:
                        progressBar.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });

        crossImage.setOnClickListener(view -> {
            simpleExoPlayer.stop();
            dialog.dismiss();
        });
        dialog.show();


        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        // Set alert dialog height equal to screen height 100%
        int dialogWindowHeight = (int) (displayHeight * 1.0f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
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