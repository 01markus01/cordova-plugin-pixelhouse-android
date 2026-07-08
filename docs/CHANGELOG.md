# PixelHouse Mobile – Developer Log

# Changelog

## Version 0.8

### Added

#### Notification Module
- Exact alarm support for scheduled notifications.
- Open Exact Alarm Settings action.
- Automatic fallback for devices where exact alarms are not allowed.

### Changed
- Improved notification scheduling accuracy.
- Improved Android compatibility.
- Notifications now use exact alarms whenever available.

### Fixed
- Prevented `SecurityException` when exact alarms are not permitted.
- Improved scheduled notification timing.

---

## Version 0.7

### Added

#### Battery Module
- Refresh_Battery_Status
- Battery_Is_Charging
- Battery_Level()

#### Device Module
- Refresh_Device_Info
- Device_Manufacturer()
- Device_Model()
- Android_Version()

#### Camera Module
- Request Camera Permission
- Camera Permission Granted
- Take Picture
- Picture Taken

#### Keep Screen On Module
- Keep Screen On
- Allow Screen Off

### Android

Implemented using:

- BatteryManager
- Intent.ACTION_BATTERY_CHANGED
- Build.MANUFACTURER
- Build.MODEL
- Build.VERSION.RELEASE
- Native Camera Intent
- WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

### JavaScript

Added Cordova bridge functions for all Battery, Device, Camera and Keep Screen On features.

### GDevelop

Added:

#### Battery
- Refresh_Battery_Status
- Battery_Is_Charging
- Battery_Level()

#### Device
- Refresh_Device_Info
- Device_Manufacturer()
- Device_Model()
- Android_Version()

#### Camera
- Request Camera Permission
- Camera Permission Granted
- Take Picture
- Picture Taken

#### Keep Screen On
- Keep Screen On
- Allow Screen Off

### Design Decisions

- Refresh values only on demand.
- Cache values for expressions.
- Keep the public API small and beginner-friendly.
- Hide Android-specific implementation details.

---

## Version 0.6

### Flashlight Module

### Added

- Flashlight_On
- Flashlight_Off
- Refresh_Flashlight_Status
- Flashlight_Is_On

### Android

Implemented using:

- CameraManager
- CameraCharacteristics
- CameraManager.setTorchMode()

Automatic detection of devices without flashlight support.

### JavaScript

Added:

- flashlightOn()
- flashlightOff()
- isFlashlightOn()

### GDevelop

Added:

- Flashlight_On
- Flashlight_Off
- Refresh_Flashlight_Status
- Flashlight_Is_On

### Design Decisions

- Small public API.
- Hide Android complexity.
- Silent handling on unsupported devices.
- Internally managed flashlight state.

---

## Version 0.5

### Notification Module

### Added

- Request Notification Permission
- Schedule Notification
- Background Notifications
- Refresh Notification Permission Status
- Notifications Enabled Condition
- Prepare Default Notification Channel
- Open Default Notification Channel
- Open Notification Channel
- Open Exact Alarm Settings

### Android

Implemented using:

- AlarmManager
- NotificationManager
- BroadcastReceiver
- Notification Channels
- Exact Alarm support with automatic fallback

### JavaScript

Added bridge functions for all notification features.

### GDevelop

Added:

#### Actions
- Request Notification Permission
- Schedule Notification
- Prepare Default Notification Channel
- Open Default Notification Channel
- Open Notification Channel
- Open Exact Alarm Settings

#### Conditions
- Notifications Enabled

### Design Decisions

- Background notifications work while the app is closed.
- Automatic fallback if exact alarms are unavailable.
- Exact Alarm Settings remain optional for developers who require maximum timing accuracy.
- Android complexity is hidden behind a simple API.

---

# Project History

## Milestone 001

- GitHub repository created.
- Custom Cordova plugin successfully integrated into GDevelop.

## Milestone 002

- Notification Channel support implemented.
- Default Notification Channel can be opened from GDevelop.

## Milestone 003

End-to-end communication successfully verified.

```
GDevelop
        ↓
PixelHouseAndroid.js
        ↓
PixelHouseAndroid.java
        ↓
Android
        ↓
Callback to GDevelop
```

The complete communication chain was successfully tested.

## Milestone 004

The first fully native Android notification was successfully delivered.

Architecture:

```
GDevelop
        ↓
PixelHouseAndroid.js
        ↓
PixelHouseAndroid.java
        ↓
Android NotificationManager
```

Implemented:

- Native Android notifications
- Heads-up notifications
- Automatic Notification Channel creation

## Milestone 005

Background notification system completed.

Implemented:

- AlarmManager
- BroadcastReceiver
- Notification scheduling while the application is closed
- Foundation for reliable local notifications