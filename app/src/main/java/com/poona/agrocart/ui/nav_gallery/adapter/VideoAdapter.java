package com.poona.agrocart.ui.nav_gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.databinding.RowVideoItemBinding;
import com.poona.agrocart.ui.nav_gallery.model.Videos;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private Context pContext;
    private ArrayList<Videos> videosList;
    private RowVideoItemBinding videoItemBinding;

    public VideoAdapter(Context pContext, ArrayList<Videos> videosList) {
        this.pContext = pContext;
        this.videosList = videosList;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        videoItemBinding = RowVideoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new VideoHolder(videoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        Videos videos = videosList.get(position);
        videoItemBinding.setModuleVideo(videos);
        holder.bind(videos);
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public class VideoHolder extends RecyclerView.ViewHolder {
        RowVideoItemBinding videoHolderBinding;
        public VideoHolder(RowVideoItemBinding binding) {
            super(binding.getRoot());
            videoHolderBinding = binding;
        }

        public void bind(Videos videos) {
            videoHolderBinding.setVariable(BR.modulePhoto, videos);
            videoHolderBinding.executePendingBindings();
        }
    }
}
