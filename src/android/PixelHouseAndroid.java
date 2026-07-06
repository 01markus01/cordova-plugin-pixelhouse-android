// PixelHouseAndroid.java

package com.pixelhouse.android;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class PixelHouseAndroid extends CordovaPlugin {

    private static final String DEFAULT_CHANNEL_ID = "default_channel";
    private static final String HIGH_CHANNEL_ID = "pixelhouse_high_channel";
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 5001;

    // Notification
    private CallbackContext notificationPermissionCallback;

    // Flashlight
    private CameraManager cameraManager;
    private String flashlightCameraId;
    private boolean flashlightIsOn = false;

    // Battery
    private boolean batteryIsCharging = false;
    private int batteryLevel = 0;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        // -------------------------
        // Notifications
        // -------------------------

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

        if ("areNotificationsEnabled".equals(action)) {
            areNotificationsEnabled(callbackContext);
            return true;
        }

        // -------------------------
        // Flashlight
        // -------------------------

        if ("flashlightOn".equals(action)) {
            flashlightOn(callbackContext);
            return true;
        }

        if ("flashlightOff".equals(action)) {
            flashlightOff(callbackContext);
            return true;
        }

        if ("isFlashlightOn".equals(action)) {
            isFlashlightOn(callbackContext);
            return true;
        }

        // -------------------------
        // Battery
        // -------------------------

        if ("refreshBatteryStatus".equals(action)) {
            refreshBatteryStatus(callbackContext);
            return true;
        }

        if ("isBatteryCharging".equals(action)) {
            isBatteryCharging(callbackContext);
            return true;
        }

        if ("getBatteryLevel".equals(action)) {
            getBatteryLevel(callbackContext);
            return true;
        }

        return false;
    }

    // -------------------------
    // Notification Methods
    // -------------------------

    private void prepareDefaultNotificationChannel(CallbackContext callbackContext) {
        try {
            createHighNotificationChannel();
            callbackContext.success("Notification channel prepared");
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

    private void areNotificationsEnabled(CallbackContext callbackContext) {
        try {
            Context context = cordova.getActivity().getApplicationContext();

            boolean enabled;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                enabled = context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    enabled = notificationManager.areNotificationsEnabled();
                } else {
                    enabled = true;
                }
            }

            callbackContext.success(enabled ? "true" : "false");

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

            createHighNotificationChannel();

            if (safeSeconds == 0) {
                showNotificationNow(title, message);
                callbackContext.success("Native notification shown immediately");
                return;
            }

            Context context = cordova.getActivity().getApplicationContext();

            Intent intent = new Intent(context, PixelHouseNotificationReceiver.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);

            int notificationId = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    context,
                    notificationId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            long triggerAtMillis = System.currentTimeMillis() + (safeSeconds * 1000L);

            AlarmManager alarmManager =
                    (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
            );

            callbackContext.success("Native notification scheduled after " + safeSeconds + " seconds");

        } catch (Exception e) {
            callbackContext.error(e.toString());
        }
    }

    private void showNotificationNow(String title, String message) {
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

    // -------------------------
    // Flashlight Methods
    // -------------------------

    private void flashlightOn(CallbackContext callbackContext) {
        try {
            String cameraId = getFlashlightCameraId();

            if (cameraId == null) {
                flashlightIsOn = false;
                callbackContext.success("Flashlight not available");
                return;
            }

            cameraManager.setTorchMode(cameraId, true);
            flashlightIsOn = true;

            callbackContext.success("Flashlight on");

        } catch (Exception e) {
            flashlightIsOn = false;
            callbackContext.success("Flashlight not available");
        }
    }

    private void flashlightOff(CallbackContext callbackContext) {
        try {
            String cameraId = getFlashlightCameraId();

            if (cameraId == null) {
                flashlightIsOn = false;
                callbackContext.success("Flashlight not available");
                return;
            }

            cameraManager.setTorchMode(cameraId, false);
            flashlightIsOn = false;

            callbackContext.success("Flashlight off");

        } catch (Exception e) {
            flashlightIsOn = false;
            callbackContext.success("Flashlight off");
        }
    }

    private void isFlashlightOn(CallbackContext callbackContext) {
        try {
            callbackContext.success(flashlightIsOn ? "true" : "false");
        } catch (Exception e) {
            callbackContext.success("false");
        }
    }

    private String getFlashlightCameraId() {
        try {
            Context context = cordova.getActivity().getApplicationContext();

            if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                return null;
            }

            if (cameraManager == null) {
                cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            }

            if (flashlightCameraId != null) {
                return flashlightCameraId;
            }

            String[] cameraIds = cameraManager.getCameraIdList();

            for (String cameraId : cameraIds) {
                CameraCharacteristics characteristics =
                        cameraManager.getCameraCharacteristics(cameraId);

                Boolean flashAvailable =
                        characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                Integer lensFacing =
                        characteristics.get(CameraCharacteristics.LENS_FACING);

                if (flashAvailable != null
                        && flashAvailable
                        && lensFacing != null
                        && lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                    flashlightCameraId = cameraId;
                    return flashlightCameraId;
                }
            }

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    // -------------------------
    // Battery Methods
    // -------------------------

    private void refreshBatteryStatus(CallbackContext callbackContext) {
        try {
            updateBatteryStatus();
            callbackContext.success("Battery status refreshed");
        } catch (Exception e) {
            batteryIsCharging = false;
            batteryLevel = 0;
            callbackContext.success("Battery status not available");
        }
    }

    private void isBatteryCharging(CallbackContext callbackContext) {
        try {
            callbackContext.success(batteryIsCharging ? "true" : "false");
        } catch (Exception e) {
            callbackContext.success("false");
        }
    }

    private void getBatteryLevel(CallbackContext callbackContext) {
        try {
            callbackContext.success(String.valueOf(batteryLevel));
        } catch (Exception e) {
            callbackContext.success("0");
        }
    }

    private void updateBatteryStatus() {
        Context context = cordova.getActivity().getApplicationContext();

        IntentFilter batteryFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, batteryFilter);

        if (batteryStatus == null) {
            batteryIsCharging = false;
            batteryLevel = 0;
            return;
        }

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        batteryIsCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING
                        || status == BatteryManager.BATTERY_STATUS_FULL;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level >= 0 && scale > 0) {
            batteryLevel = Math.round((level * 100f) / scale);
        } else {
            batteryLevel = 0;
        }
    }
}