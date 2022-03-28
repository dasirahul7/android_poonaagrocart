package com.poona.agrocart.data.firebase;

import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.NOTIFICATION;
import static com.poona.agrocart.app.AppConstants.PUSH_NOTIFICATIONS;
import static com.poona.agrocart.data.firebase.IFCMConstants.FCM_NEW_ORDER_ACTION;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.poona.agrocart.R;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class PoonaAgroCartFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = PoonaAgroCartFirebaseMessagingService.class.getSimpleName();
    private static final String CHANNEL_ID = "poona_agro_cart";
    private static final String CHANNEL_NAME = "PoonaAgroCartChannel";
    private AppSharedPreferences preference;
    private Context context;
    private Bitmap image;

    public PoonaAgroCartFirebaseMessagingService() {
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        preference = new AppSharedPreferences(this);
        preference.setFCMToken(s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = getApplicationContext();
        preference = new AppSharedPreferences(this);
        Log.d(TAG, "Notification Body back: " +  remoteMessage.getData());
//        if (remoteMessage == null)
//            return;

        //Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            Log.d(TAG, "Notification Body: " + remoteMessage.getData());
            handleNotification(String.valueOf(remoteMessage.getData()));
        }

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "onMessageReceived Data: " + remoteMessage.getData());
            Map<String, String> params = remoteMessage.getData();

            /*
             * print map
             * */
            for (Map.Entry<String, String> entry : params.entrySet()) {
                Log.e(TAG, "Key : " + entry.getKey() + " : : " + entry.getValue());
            }

            /*
             * convert map to json object
             * */
            JSONObject jsonObject = new JSONObject(params);
            handleNotificationData(jsonObject);
        }
    }

    private void handleNotification(String message) {
        if (!isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATIONS);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleNotificationData(JSONObject jsonObject) {
        String notificationType = "", userId = "", image = "", title = "", message = "",
                redirectId = "",redirectName ="",redirectType ="", redirectTo = "";

        try {
            title = jsonObject.getString("title");
        } catch (Exception e) {
            title = "";
        }
        try {
            userId = jsonObject.getString("user_id");
        } catch (Exception e) {
            userId = "";
        }
        try {
            message = jsonObject.getString("message");
        } catch (Exception e) {
            message = "";
        }
        try {
            image = jsonObject.getString("image");
        } catch (Exception e) {
            image = "";
        }
        try {
            redirectId = jsonObject.getString("redirect_id");
        } catch (Exception e) {
            redirectId = "";
        }
        try {
            redirectName = jsonObject.getString("redirect_name");
        } catch (Exception e) {
            redirectName = "";
        }
        try {
            redirectType = jsonObject.getString("redirect_type");
        } catch (Exception e) {
            redirectType = "";
        }
        try {
            notificationType = jsonObject.getString("notification_type");
        } catch (Exception e) {
            notificationType = "";
        }
        try {
            redirectTo = jsonObject.getString("redirect_to");
        } catch (Exception e) {
            redirectTo = "";
        }

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setNotificationType(notificationType);
        pushNotification.setMessage(message);
        pushNotification.setRedirectId(redirectId);
        pushNotification.setRedirectTo(redirectTo);
        pushNotification.setRedirectName(redirectName);
        pushNotification.setRedirectType(redirectType);
        pushNotification.setImage(image);
        pushNotification.setUserId(userId);

        showNotification(pushNotification);
    }

    private void showNotification(PushNotification pushNotification) {
        //   sendPushNotificationCountFCMBroadcast(pushNotification);
        // Create an Intent for the activity you want to start

        Bundle bundle = new Bundle();
        Intent resultIntent = new Intent(this, HomeActivity.class); //Change

        bundle.putString(FROM_SCREEN, NOTIFICATION);
        bundle.putSerializable(PUSH_NOTIFICATIONS, pushNotification);
        resultIntent.putExtras(bundle);
        //   resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        androidx.core.app.TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        // Get the PendingIntent containing the entire back stack

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(new Random().nextInt(), PendingIntent.FLAG_UPDATE_CURRENT);

        try {
            URL url = new URL(pushNotification.getImage());
            image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }
        int NOTIFICATION_RANDOM_ID = new Random().nextInt(1000);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ansec_small_icon);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(pushNotification.getTitle())
                .setContentText(pushNotification.getMessage())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(resultPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX) //Important for heads-up notification
                .setLargeIcon(image)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(pushNotification.getTitle())
                        .bigPicture(image));

        builder.setSmallIcon(R.drawable.ic_notifications);
        builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // CharSequence _name = getString(R.string.channel_name);
            //  String _description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            //channel.setDescription(_description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel.setSound(defaultSoundUri, audioAttributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(NOTIFICATION_RANDOM_ID, builder.build());
        } else {

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(NOTIFICATION_RANDOM_ID, builder.build());
        }
    }
    /**
     * Send broadcast using LocalBroadcastManager to update UI in activity
     *
     * @param pushNotification
     */
    private void sendNewOrderFCMBroadcast(PushNotification pushNotification) {
        Intent locationIntent = new Intent();
        locationIntent.setAction(FCM_NEW_ORDER_ACTION);
        this.sendBroadcast(locationIntent);
    }

    /**
     * Method checks if the app is in background or not
     */
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                componentInfo = taskInfo.get(0).topActivity;
            }
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}