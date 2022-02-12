package com.poona.agrocart.ui.nav_gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.data.network.reponses.galleryResponse.GalleryVideo;
import com.poona.agrocart.databinding.RowVideoItemBinding;
import com.poona.agrocart.ui.nav_gallery.fragment.VideoGalleryFragment;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Context pContext;
    private List<GalleryVideo> galleryVideoList = new ArrayList<>();
    private RowVideoItemBinding videoItemBinding;
    private OnVideoClickListener onVideoClickListener;
    private VideoGalleryFragment videoGalleryFragment;
    private ImageView popUpImage;

    public VideoAdapter(Context pContext, List<GalleryVideo> videosList, VideoGalleryFragment videoGalleryFragment) {
        this.pContext = pContext;
        this.galleryVideoList = videosList;
        this.onVideoClickListener = videoGalleryFragment;
    }

    public interface OnVideoClickListener{
        void itemViewClick(int position);
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
    }

    @Override
    public int getItemCount() {
        return galleryVideoList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        RowVideoItemBinding videoHolderBinding;
        public VideoHolder(RowVideoItemBinding binding) {
            super(binding.getRoot());
            videoHolderBinding = binding;


            itemView.setOnClickListener(v ->{
                if (onVideoClickListener != null) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onVideoClickListener.itemViewClick(postion);
                    }
                }
            });

        }

        public void bind(GalleryVideo videos) {
            videoHolderBinding.setVariable(BR.galleryVideo, videos);
            videoHolderBinding.executePendingBindings();
        }
    }
}
