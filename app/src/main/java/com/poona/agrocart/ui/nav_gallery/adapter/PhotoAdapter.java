package com.poona.agrocart.ui.nav_gallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.BR;
import com.poona.agrocart.databinding.RowPhotoItemBinding;
import com.poona.agrocart.ui.nav_gallery.model.Photos;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {
    private Context pContext;
    private ArrayList<Photos> photoList;
    private RowPhotoItemBinding photoItemBinding;

    public PhotoAdapter(Context pContext, ArrayList<Photos> photoList) {
        this.pContext = pContext;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        photoItemBinding = RowPhotoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PhotoHolder(photoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        Photos photo = photoList.get(position);
        photoItemBinding.setModulePhoto(photo);
        holder.bind(photo);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        RowPhotoItemBinding photoItemBinding;
        public PhotoHolder(RowPhotoItemBinding rowPhotoItemBinding) {
            super(rowPhotoItemBinding.getRoot());
            photoItemBinding = rowPhotoItemBinding;
        }

        public void bind(Photos photo) {
            photoItemBinding.setVariable(BR.modulePhoto, photo);
            photoItemBinding.executePendingBindings();
        }
    }
}
