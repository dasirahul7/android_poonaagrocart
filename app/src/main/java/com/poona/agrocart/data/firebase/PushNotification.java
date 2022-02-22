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
    String title = "";
    String subtitle = "";
    String message = "";
    String messageBy = "";
    String notificationType = "";
    String msgCount = "";
    String redirectType = "";
    String vibrate = "";
    String orderNo = "";
    String orderId = "";
    String tickerText = "";
    String userType = "";

    protected PushNotification() {
    }

    protected PushNotification(Parcel in) {
        title = in.readString();
        subtitle = in.readString();
        message = in.readString();
        messageBy = in.readString();
        notificationType = in.readString();
        msgCount = in.readString();
        redirectType = in.readString();
        vibrate = in.readString();
        orderNo = in.readString();
        orderId = in.readString();
        tickerText = in.readString();
        userType = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageBy() {
        return messageBy;
    }

    public void setMessageBy(String messageBy) {
        this.messageBy = messageBy;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getRedirectType() {
        return redirectType;
    }

    public void setRedirectType(String redirectType) {
        this.redirectType = redirectType;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTickerText() {
        return tickerText;
    }

    public void setTickerText(String tickerText) {
        this.tickerText = tickerText;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(message);
        dest.writeString(messageBy);
        dest.writeString(notificationType);
        dest.writeString(msgCount);
        dest.writeString(redirectType);
        dest.writeString(vibrate);
        dest.writeString(orderNo);
        dest.writeString(orderId);
        dest.writeString(tickerText);
        dest.writeString(userType);
    }
}