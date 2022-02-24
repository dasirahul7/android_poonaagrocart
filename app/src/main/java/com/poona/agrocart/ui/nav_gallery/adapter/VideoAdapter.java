package com.poona.agrocart.ui.nav_gallery.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryVideo;
import com.poona.agrocart.databinding.RowVideoItemBinding;
import com.poona.agrocart.ui.nav_gallery.fragment.VideoGalleryFragment;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private final Context pContext;
    private List<GalleryVideo> galleryVideoList = new ArrayList<>();
    private RowVideoItemBinding videoItemBinding;
    private final OnVideoClickListener onVideoClickListener;
    private VideoGalleryFragment videoGalleryFragment;
    private ImageView popUpImage;

    public VideoAdapter(Context pContext, List<GalleryVideo> videosList, VideoGalleryFragment videoGalleryFragment) {
        this.pContext = pContext;
        this.galleryVideoList = videosList;
        this.onVideoClickListener = videoGalleryFragment;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        videoItemBinding = RowVideoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new VideoHolder(videoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        GalleryVideo videos = galleryVideoList.get(position);
        videoItemBinding.setGalleryVideo(videos);
        holder.bind(videos);

        /*popUpImage = holder.videoHolderBinding.itemImg;
        Glide.with(pContext)
                .load(IMAGE_DOC_BASE_URL+galleryVideoList.get(position).getVideoImage())
                .into(popUpImage);*/

      /*  holder.videoHolderBinding.playButton.setOnClickListener(view -> {
            if (onVideoClickListener != null) {

                holder.videoHolderBinding.playButton.setClickable(false);

                    onVideoClickListener.itemViewClick(position);  // added

                    new Handler().postDelayed(() -> {
                        holder.videoHolderBinding.playButton.setClickable(true);
                    }, 500);
                }

        });*/

    }

    @Override
    public int getItemCount() {
        return galleryVideoList.size();
    }

    public interface OnVideoClickListener {
        void itemViewClick(int position);
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        RowVideoItemBinding videoHolderBinding;

        public VideoHolder(RowVideoItemBinding binding) {
            super(binding.getRoot());
            videoHolderBinding = binding;


            videoHolderBinding.photoCard.setOnClickListener(view -> {
                if (onVideoClickListener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        videoHolderBinding.photoCard.setClickable(false);

                        onVideoClickListener.itemViewClick(position);  // added

                        new Handler().postDelayed(() -> {
                            videoHolderBinding.photoCard.setClickable(true);
                        }, 500);
                    }
                }
            });

           /* itemView.setOnClickListener(v ->{
                if (onVideoClickListener != null) {
                    int postion = getBindingAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION) {

                            itemView.setClickable(false);

                            onVideoClickListener.itemViewClick(postion);  // added

                            new Handler().postDelayed(() -> {
                                itemView.setClickable(true);
                            }, 500);
                    }
                }
            });
*/


        }

        public void bind(GalleryVideo videos) {
            videoHolderBinding.setVariable(BR.galleryVideo, videos);
            videoHolderBinding.executePendingBindings();
        }
    }
}
