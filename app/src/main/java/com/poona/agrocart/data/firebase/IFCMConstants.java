package com.poona.agrocart.data.firebase;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public interface IFCMConstants {
    /**
     * broadcast receiver actions to update screen from push notification
     */
    String FCM_NEW_ORDER_ACTION = "com.poona.agrocart.data.firebase.FCM_NEW_ORDER_ACTION";
    String FCM_CANCELLED_ORDER_ACTION = "com.poona.agrocart.data.firebase.FCM_CANCELLED_ORDER_ACTION";
    String FCM_COMPLETED_ORDER_ACTION = "com.poona.agrocart.data.firebase.FCM_COMPLETED_ORDER_ACTION";

    /**
     * Message keys for data with in the broadcast
     **/
    String FCM_PUSH_NOTIFICATION = "com.poona.agrocart.data.firebase.FCM_PUSH_NOTIFICATION";
}