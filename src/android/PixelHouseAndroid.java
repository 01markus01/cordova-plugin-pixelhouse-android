package com.pixelhouse.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class PixelHouseAndroid extends CordovaPlugin {

    private static final String DEFAULT_CHANNEL_ID = "default_channel";
    private static final String HIGH_CHANNEL_ID = "pixelhouse_high_channel";

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if ("prepareDefaultNotificationChannel".equals(action)) {
            prepareDefaultNotificationChannel(callbackContext);
            return true;
        }

        if ("openDefaultNotificationChannel".equals(action)) {
            openNotificationChannel(DEFAULT_CHANNEL_ID, callbackContext);
            return true;
        }

        if ("openNotificationChannel".equals(action)) {
            String channelId = args.optString(0, DEFAULT_CHANNEL_ID);
            openNotificationChannel(channelId, callbackContext);
            return true;
        }

        if ("scheduleNotification".equals(action)) {
            String title = args.optString(0, "PixelHouse");
            String message = args.optString(1, "Test notification");
            int seconds = args.optInt(2, 5);

            scheduleNotification(title, message, seconds, callbackContext);
            return true;
        }

        return false;
    }

    private void prepareDefaultNotificationChannel(CallbackContext callbackContext) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationManager notificationManager =
                        (NotificationManager) cordova.getActivity()
                                .getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel channel = new NotificationChannel(
                        DEFAULT_CHANNEL_ID,
                        "Default channel",
                        NotificationManager.IMPORTANCE_HIGH
                );

                channel.setDescription("Benachrichtigungen");
                channel.enableVibration(true);
                channel.enableLights(true);

                notificationManager.createNotificationChannel(channel);

                callbackContext.success("Default channel vorbereitet");

            } else {
                callbackContext.success("Android-Version braucht keinen Notification Channel");
            }

        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    private void openNotificationChannel(String channelId, CallbackContext callbackContext) {

        try {
            String packageName = cordova.getActivity().getPackageName();
            Intent intent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
            } else {
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
            }

            cordova.getActivity().startActivity(intent);
            callbackContext.success("Opened notification channel settings: " + channelId);

        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    private void scheduleNotification(String title, String message, int seconds, CallbackContext callbackContext) {

        try {
            Context context = cordova.getActivity().getApplicationContext();

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

            callbackContext.success("Native notification shown immediately");

        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }
}