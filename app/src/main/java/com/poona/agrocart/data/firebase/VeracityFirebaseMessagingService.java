package com.poona.agrocart.data.firebase;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static com.poona.agrocart.app.AppConstants.PUSH_NOTIFICATION;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class VeracityFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = VeracityFirebaseMessagingService.class.getSimpleName();

    private AppSharedPreferences preference;

    private Context context;

    public VeracityFirebaseMessagingService() { }

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

        if(remoteMessage == null)
            return;

        //Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0)
        {
            Log.e(TAG, "onMessageReceived Data: " + remoteMessage.getData().toString());
            Map<String, String> params = remoteMessage.getData();

            /*
            * print map
            * */
            for (Map.Entry<String, String> entry : params.entrySet())
            {
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
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleNotificationData(JSONObject jsonObject)
    {
        String title = "", subtitle = "", message = "", messageBy = "", notificationType = "", msgCount = "",
                redirectType = "", vibrate = "", orderNo = "", orderId = "", tickerText = "", userType = "";

        try { title = jsonObject.getString("title"); }
        catch (Exception e) { title = ""; }
        try { subtitle = jsonObject.getString("subtitle"); }
        catch (Exception e) { subtitle = ""; }
        try { message = jsonObject.getString("message"); }
        catch (Exception e) { message = ""; }
        try { messageBy = jsonObject.getString("message_by"); }
        catch (Exception e) { messageBy = ""; }
        try { notificationType = jsonObject.getString("notification_type"); }
        catch (Exception e) { notificationType = ""; }
        try { msgCount = jsonObject.getString("msgcnt"); }
        catch (Exception e) { msgCount = ""; }
        try { redirectType = jsonObject.getString("redirect_type"); }
        catch (Exception e) { redirectType = ""; }
        try { vibrate = jsonObject.getString("vibrate"); }
        catch (Exception e) { vibrate = ""; }
        try { orderNo = jsonObject.getString("order_no"); }
        catch (Exception e) { orderNo = ""; }
        try { orderId = jsonObject.getString("order_id"); }
        catch (Exception e) { orderId = ""; }
        try { tickerText = jsonObject.getString("tickerText"); }
        catch (Exception e) { tickerText = ""; }
        try { userType = jsonObject.getString("user_type"); }
        catch (Exception e) { userType = ""; }

        PushNotification pushNotification = new PushNotification();
        pushNotification.setTitle(title);
        pushNotification.setSubtitle(subtitle);
        pushNotification.setMessage(message);
        pushNotification.setMessageBy(messageBy);
        pushNotification.setNotificationType(notificationType);
        pushNotification.setMsgCount(msgCount);
        pushNotification.setRedirectType(redirectType);
        pushNotification.setVibrate(vibrate);
        pushNotification.setOrderNo(orderNo);
        pushNotification.setOrderId(orderId);
        pushNotification.setTickerText(tickerText);
        pushNotification.setUserType(userType);

        showNotification(pushNotification);
    }

    private static final String CHANNEL_ID = "VeracityChannelId";
    private void showNotification(PushNotification pushNotification)
    {
        // Create an Intent for the activity you want to start
//        Intent resultIntent = null;
//        Bundle bundle = new Bundle();
//
//        if (preference.getIsLoggedIn() && preference.getUserType().equals(VENDOR))
//            resultIntent = new Intent(context, DashboardActivity.class);
//        else if(preference.getIsLoggedIn() && preference.getUserType().equals(DELIVERY_BOY))
//            resultIntent = new Intent(context, DeliveryBoyHomeActivity.class);
//
//        bundle.putString(FROM_SCREEN, VeracityFirebaseMessagingService.class.getSimpleName());
//        bundle.putSerializable(PUSH_NOTIFICATIONS, pushNotification);
//        resultIntent.putExtras(bundle);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Create the TaskStackBuilder and add the intent, which inflates the back stack
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntentWithParentStack(resultIntent);
//        // Get the PendingIntent containing the entire back stack
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
//
//        int NOTIFICATION_RANDOM_ID = new Random().nextInt(1000);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.new_logo);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(pushNotification.getMessage())
//                .setAutoCancel(true)
//                .setColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
//                .setSound(defaultSoundUri)
//                .setLargeIcon(largeIcon)
//                .setSmallIcon(R.drawable.fcm_notification_icon)
//                .setContentIntent(resultPendingIntent)
//                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
//                .setPriority(Notification.PRIORITY_MAX) //Important for heads-up notification
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .setBigContentTitle(pushNotification.getSubtitle()).bigText(pushNotification.getMessage()));
//
//        /*if (bitmap != null) {
//            NotificationCompat.BigPictureStyle bpStyle = new NotificationCompat.BigPictureStyle();
//            bpStyle.bigPicture(bitmap);
//            bpStyle.build();
//            builder.setStyle(bpStyle);
//        }*/
//
//        builder.setSmallIcon(R.drawable.fcm_notification_icon);
//        builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            CharSequence _name = getString(R.string.channel_name);
//            String _description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, _name, importance);
//            channel.setDescription(_description);
//            channel.setShowBadge(true);
//            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            channel.setSound(defaultSoundUri, audioAttributes);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//
//            notificationManager.notify(NOTIFICATION_RANDOM_ID, builder.build());
//        }
//        else
//        {
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            // notificationId is a unique int for each notification that you must define
//            notificationManager.notify(NOTIFICATION_RANDOM_ID, builder.build());
//        }
//
//        if(pushNotification.getRedirectType().equals(PENDING))
//        {
//            /*
//             * call broadcast receiver method
//             * */
//            int existingCount = preference.getNewOrderCount();
//            preference.setNewOrderCount(existingCount + 1);
//
//            sendNewOrderFCMBroadcast(pushNotification);
//        }
    }
    /**
     * Send broadcast using LocalBroadcastManager to update UI in activity
     *
     * @param pushNotification
     */
//    private void sendNewOrderFCMBroadcast(PushNotification pushNotification) {
//        Intent locationIntent = new Intent();
//        locationIntent.setAction(FCM_NEW_ORDER_ACTION);
//        this.sendBroadcast(locationIntent);
//    }

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