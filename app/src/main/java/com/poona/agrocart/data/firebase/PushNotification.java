package com.poona.agrocart.data.firebase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class PushNotification implements Parcelable, Serializable {
    public static final Creator<PushNotification> CREATOR = new Creator<PushNotification>() {
        @Override
        public PushNotification createFromParcel(Parcel in) {
            return new PushNotification(in);
        }

        @Override
        public PushNotification[] newArray(int size) {
            return new PushNotification[size];
        }
    };
    String notificationType = "";
    String userId = "";
    String image = "";
    String title = "";
    String message = "";
    String redirectId = "";
    String redirectTo = "";

    protected PushNotification() {
    }

    protected PushNotification(Parcel in) {
        notificationType = in.readString();
        userId = in.readString();
        image = in.readString();
        title = in.readString();
        message = in.readString();
        redirectId = in.readString();
        redirectTo = in.readString();
    }

    public static Creator<PushNotification> getCREATOR() {
        return CREATOR;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirectId() {
        return redirectId;
    }

    public void setRedirectId(String redirectId) {
        this.redirectId = redirectId;
    }

    public String getRedirectTo() {
        return redirectTo;
    }

    public void setRedirectTo(String redirectTo) {
        this.redirectTo = redirectTo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notificationType);
        dest.writeString(userId);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(message);
        dest.writeString(redirectId);
        dest.writeString(redirectTo);
    }
}