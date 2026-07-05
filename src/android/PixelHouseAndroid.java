package com.pixelhouse.android;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class PixelHouseAndroid extends CordovaPlugin {

    private static final String DEFAULT_CHANNEL_ID = "default_channel";
    private static final String HIGH_CHANNEL_ID = "pixelhouse_high_channel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 5001;

    private CallbackContext notificationPermissionCallback;

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
            int seconds = args.optInt(2, 0);

            scheduleNotification(title, message, seconds, callbackContext);
            return true;
        }

        if ("requestNotificationPermission".equals(action)) {
            requestNotificationPermission(callbackContext);
            return true;
        }

        return false;
    }

    private void prepareDefaultNotificationChannel(CallbackContext callbackContext) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createHighNotificationChannel();
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

    private void requestNotificationPermission(CallbackContext callbackContext) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                callbackContext.success("Notification permission not needed on this Android version");
                return;
            }

            Context context = cordova.getActivity().getApplicationContext();

            if (context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                callbackContext.success("Notification permission already granted");
                return;
            }

            notificationPermissionCallback = callbackContext;

            cordova.requestPermission(
                    this,
                    REQUEST_CODE_POST_NOTIFICATIONS,
                    Manifest.permission.POST_NOTIFICATIONS
            );

        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    @Override
    public void onRequestPermissionResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) throws JSONException {

        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {

            if (notificationPermissionCallback == null) {
                return;
            }

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                notificationPermissionCallback.success("Notification permission granted");
            } else {
                notificationPermissionCallback.error("Notification permission denied");
            }

            notificationPermissionCallback = null;
            return;
        }

        super.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    private void scheduleNotification(String title, String message, int seconds, CallbackContext callbackContext) {
        try {
            int safeSeconds = Math.max(0, seconds);

            if (safeSeconds == 0) {
                showNotification(title, message);
                callbackContext.success("Native notification shown immediately");
                return;
            }

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    showNotification(title, message);
                }
            }, safeSeconds * 1000L);

            callbackContext.success("Native notification scheduled after " + safeSeconds + " seconds");

        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    private void showNotification(String title, String message) {
        Context context = cordova.getActivity().getApplicationContext();

        createHighNotificationChannel();

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

    private void createHighNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context context = cordova.getActivity().getApplicationContext();

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