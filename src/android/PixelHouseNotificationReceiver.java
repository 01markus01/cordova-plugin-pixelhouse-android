
package com.pixelhouse.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class PixelHouseNotificationReceiver extends BroadcastReceiver {

    private static final String HIGH_CHANNEL_ID = "pixelhouse_high_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String message = intent.getStringExtra("message");

        if (title == null) {
            title = "PixelHouse";
        }

        if (message == null) {
            message = "Notification";
        }

        createHighNotificationChannel(context);
        showNotification(context, title, message);
    }

    private void showNotification(Context context, String title, String message) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent launchIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(context, HIGH_CHANNEL_ID);
        } else {
            builder = new Notification.Builder(context);
            builder.setPriority(Notification.PRIORITY_HIGH);
        }

        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(context.getApplicationInfo().icon);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);

        notificationManager.notify(2001, builder.build());
    }

    private void createHighNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                    HIGH_CHANNEL_ID,
                    "PixelHouse High Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );

            channel.setDescription("Native PixelHouse notifications");
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(channel);
        }
    }
}