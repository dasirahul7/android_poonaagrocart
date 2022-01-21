package com.poona.agrocart.ui.nav_stores.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class Store implements Parcelable {
    private String id,name,location,img,about,address,contact;

    public Store()
    {
    }
    public Store(String id, String name, String location, String img) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.img = img;
    }

    protected Store(Parcel in) {
        id = in.readString();
        name = in.readString();
        location = in.readString();
        img = in.readString();
        about = in.readString();
        address = in.readString();
        contact = in.readString();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @BindingAdapter("setStoreImage")
    public static void setStoreImage(ImageView view, String img){
        Glide.with(view.getContext())
                .load(img)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(view);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(img);
        dest.writeString(about);
        dest.writeString(address);
        dest.writeString(contact);
    }
}
