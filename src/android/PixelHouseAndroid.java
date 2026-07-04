package com.pixelhouse.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class PixelHouseAndroid extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if ("openDefaultNotificationChannel".equals(action)) {
            openNotificationChannel("default_channel", callbackContext);
            return true;
        }

        if ("openNotificationChannel".equals(action)) {
            String channelId = args.optString(0, "default_channel");
            openNotificationChannel(channelId, callbackContext);
            return true;
        }

        return false;
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
}
