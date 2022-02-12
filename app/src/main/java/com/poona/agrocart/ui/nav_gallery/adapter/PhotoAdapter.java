package com.poona.agrocart.ui.nav_gallery.adapter;

import static com.poona.agrocart.app.AppConstants.IMAGE_DOC_BASE_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poona.agrocart.BR;
import com.poona.agrocart.data.network.reponses.gallery.GalleryImage;
import com.poona.agrocart.data.network.reponses.gallery.GalleryVideo;
import com.poona.agrocart.databinding.RowPhotoItemBinding;
import com.poona.agrocart.ui.nav_gallery.fragment.PhotoGalleryFragment;
import com.poona.agrocart.ui.nav_gallery.fragment.VideoGalleryFragment;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private Context pContext;
    private List<GalleryImage> galleryImages = new ArrayList<>();
    private RowPhotoItemBinding photoItemBinding;
    private OnPhotoClickListener onPhotoClickListener;
    private PhotoGalleryFragment photoGalleryFragment;
    private ImageView imageView;


    public PhotoAdapter(Context context, List<GalleryImage> galleryImages, PhotoGalleryFragment photoGalleryFragment) {
        this.pContext = context;
        this.galleryImages = galleryImages;
        this.onPhotoClickListener = photoGalleryFragment;

    }


    public interface OnPhotoClickListener{

        void itemViewClick(int position);
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        photoItemBinding = RowPhotoItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        return new PhotoHolder(photoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        GalleryImage photo = galleryImages.get(position);
        photoItemBinding.setGalleryImage(photo);
        holder.bind(photo);

        imageView = holder.photoItemBinding.itemImg;
        // set page image
        Glide.with(pContext)
                .load(IMAGE_DOC_BASE_URL+galleryImages.get(position).getGalleryImage())
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return galleryImages.size();
    }

    public class PhotoHolder extends RecyclerView.ViewHolder {
        RowPhotoItemBinding photoItemBinding;

        public PhotoHolder(RowPhotoItemBinding rowPhotoItemBinding) {
            super(rowPhotoItemBinding.getRoot());
            photoItemBinding = rowPhotoItemBinding;

            itemView.setOnClickListener(v ->{
                if (onPhotoClickListener != null) {
                    int postion = getAdapterPosition();
                    if (postion != RecyclerView.NO_POSITION) {
                        onPhotoClickListener.itemViewClick(postion);
                    }
                }
            });

        }

        public void bind(GalleryImage photo) {
            photoItemBinding.setVariable(BR.galleryImage, photo);
            photoItemBinding.executePendingBindings();
        }
    }
}
