package com.sassan.salathelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.core.app.NotificationManagerCompat;

public class NotificationListener extends NotificationListenerService {

    public static final String ACTION_CANCEL_NOTIFICATIONS = "com.example.app.ACTION_CANCEL_NOTIFICATIONS";
    public static String TAG = NotificationListener.class.getSimpleName();


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && intent.getAction() != null) {
//            if (intent.getAction().equals(ACTION_CANCEL_NOTIFICATIONS)) {
//                cancelAllNotifications();
//            }
//        }
//        return super.onStartCommand(intent, flags, startId);
//    }

//    @Override
//    public void onNotificationPosted(StatusBarNotification sbm) {
//
//        /**
//         * This condition will define how you will identify your test is running
//         * You can have an in memory variable regarding it, or persistant variable,
//         * Or you can use Settings to store current state.
//         * You can have your own approach
//         */
//        boolean condition = true; // say your test is running
//
//        if (condition)
//            cancelAllNotifications(); //Cancel all the notifications . No Disturbance
//
//        //else
//        // nothing.
//    }

    private boolean isAppInForeground() {
        return ((BaseActivity) getApplicationContext()).activityCounter > 0;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (isAppInForeground()) {
            // Modify the notification to suppress sound and vibration
            Notification notification = sbn.getNotification();
            if (notification != null) {
                notification.defaults = 0; // Clear all default behaviors (sound, vibration)
                notification.sound = null; // Remove the sound
                notification.vibrate = null; // Remove vibration
                // Update the modified notification
                updateNotification(sbn.getKey(), notification);
            }
        }
    }

    // Helper method to update the modified notification
    private void updateNotification(String key, Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android Oreo and above, use the following method to update the notification
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.notify(Integer.parseInt(key), notification);
            }
        } else {
            // For earlier Android versions, use the deprecated method to update the notification
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(Integer.parseInt(key), notification);
        }
    }
}