// Add scheduleNotification test method
package com.pixelhouse.android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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

            callbackContext.success(
                    "scheduleNotification erreicht: "
                            + title
                            + " / "
                            + message
                            + " / "
                            + seconds
            );

        } catch (Exception e) {

            callbackContext.error(e.toString());

        }
    }
}
