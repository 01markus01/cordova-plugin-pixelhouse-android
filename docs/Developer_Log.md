// Developer_Log.md
# PixelHouseMobile Developer Log

## 2026-07-05

### Meilenstein 001 – Projektstart
- GitHub-Repository erstellt.
- Eigenes Cordova-Plugin erfolgreich in GDevelop eingebunden.

### Meilenstein 002 – Notification Channel
- prepareDefaultNotificationChannel() implementiert.
- openDefaultNotificationChannel() implementiert.
- Notification Channel kann geöffnet werden.

### Meilenstein 003 – End-to-End Kommunikation
Architektur erfolgreich getestet:

GDevelop
→ PixelHouseMobile
→ PixelHouseAndroid.js
→ PixelHouseAndroid.java
→ Callback zurück zu GDevelop

scheduleNotification(title, message, seconds) erfolgreich bis Java getestet.
Die Erfolgsmeldung wird wieder in GDevelop angezeigt.

### Meilenstein 004 – Erste native Android-Benachrichtigung

Die erste vollständig native Android-Benachrichtigung wurde erfolgreich über das eigene Cordova-Plugin ausgelöst.

Architektur:

GDevelop
→ PixelHouseMobile
→ PixelHouseAndroid.js
→ PixelHouseAndroid.java
→ Android NotificationManager

Ergebnis:
- Native Benachrichtigung erscheint sofort.
- Heads-up-/Pop-up-Benachrichtigung funktioniert.
- Eigener Notification Channel "PixelHouse High Notifications" wird automatisch erstellt.

Erkenntnis:
Android 13+ benötigt vor dem Senden die Benachrichtigungsberechtigung (POST_NOTIFICATIONS). Ohne diese Berechtigung erscheint keine Benachrichtigung.

Nächster Schritt:
Native Funktion zum Anfordern der Benachrichtigungsberechtigung implementieren.

### Meilenstein 007 – Background Notifications

- Notification-System auf AlarmManager umgestellt.
- BroadcastReceiver (PixelHouseNotificationReceiver) hinzugefügt.
- plugin.xml erweitert.
- Grundlage geschaffen, damit Benachrichtigungen auch bei geschlossener App funktionieren.

## Version 0.5

### Android Notification API completed

Implemented:

- Request Android notification permission
- Schedule local notifications
- Notification support while app is closed
- Refresh notification permission status
- Notifications Enabled condition for GDevelop
- Improved function names and documentation
- Cleaned up extension interface

# Version 0.6

## Flashlight Module

### Added

- Flashlight_On
- Flashlight_Off
- Refresh_Flashlight_Status
- Flashlight_Is_On

---

## Android

Implemented using:

- CameraManager
- CameraCharacteristics
- CameraManager.setTorchMode()

Automatic detection of devices without flashlight support.

No crashes on unsupported devices.

---

## JavaScript

Added bridge functions:

- flashlightOn()
- flashlightOff()
- isFlashlightOn()

---

## GDevelop

New category:

Flashlight

Added:

- Action: Flashlight_On
- Action: Flashlight_Off
- Action: Refresh_Flashlight_Status
- Condition: Flashlight_Is_On

---

## Design Decisions

- Keep the public API as small as possible.
- Hide Android complexity from the developer.
- No separate "Flashlight Off" condition.
- Devices without flashlight fail silently.
- Flashlight_Is_On reports the internally managed flashlight state.


# Version 0.7

## Battery Module

### Added

- Refresh_Battery_Status
- Battery_Is_Charging
- Battery_Level()

---

## Android

Implemented using:

- BatteryManager
- Intent.ACTION_BATTERY_CHANGED

Battery information is refreshed on demand.

---

## JavaScript

Added bridge functions:

- refreshBatteryStatus()
- isBatteryCharging()
- getBatteryLevel()

---

## GDevelop

New category:

Battery

Added:

- Action: Refresh_Battery_Status
- Condition: Battery_Is_Charging
- Expression: Battery_Level()

---

## Design Decisions

- Keep the public API as small as possible.
- Hide Android complexity from the developer.
- Battery status is refreshed only when requested.
- Battery level is returned as a percentage (0-100).

# Device Module

## Added

### Action

- Refresh_Device_Info

### Expressions

- Device_Manufacturer()
- Device_Model()
- Android_Version()

---

## Android

Implemented native device information support using:

- Build.MANUFACTURER
- Build.MODEL
- Build.VERSION.RELEASE

Device information is cached after calling:

- Refresh_Device_Info

---

## JavaScript

Added Cordova bridge methods:

- refreshDeviceInfo()
- getDeviceManufacturer()
- getDeviceModel()
- getAndroidVersion()

---

## GDevelop

Added:

Action

- Refresh_Device_Info

Expressions

- Device_Manufacturer()
- Device_Model()
- Android_Version()

---

## Design Decisions

- Device information is refreshed only on demand.
- Expressions never access Android APIs directly.
- Expressions return cached values only.
- API intentionally kept minimal for Version 1.0.

# Camera Module

## Added

- Native Android camera support
- Runtime camera permission
- Camera permission condition
- Picture taken condition
- Native camera launch
- Camera result handling
- Automatic picture saving
- Camera module completed

---

## Android

Implemented:

- requestCameraPermission()
- isCameraPermissionGranted()
- takePicture()
- onActivityResult()

The native Android camera is opened using the Camera Intent.

After the user confirms the captured picture, Android stores the image and returns the result to GDevelop.

---

## JavaScript

Added:

- requestCameraPermission()
- isCameraPermissionGranted()
- takePicture()
- isPictureTaken()

Added internal status variable:

window.pixelHousePictureTaken

---

## GDevelop

Added Actions:

- Request Camera Permission
- Take Picture

Added Conditions:

- Camera Permission Granted
- Picture Taken

---

## Design Decisions

The Camera module follows the same architecture as all other PixelHouse Mobile modules.

Action

↓

Android

↓

Internal Status

↓

Condition

This keeps the API simple, consistent and beginner-friendly while hiding all Android-specific complexity.

## Keep Screen On

### Added

New Android module:

- Keep Screen On
- Allow Screen Off

---

### Android

Implemented native support using:

```
WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
```

The flag is added and removed directly on the current Android window.

The implementation executes on the Android UI thread for maximum compatibility.

---

### JavaScript

Added new bridge functions:

- keepScreenOn()
- allowScreenOff()

---

### GDevelop

New Actions:

- Keep Screen On
- Allow Screen Off

No Conditions or Expressions are required.

---

### Design Decisions

This module intentionally keeps the API extremely small.

Instead of exposing Android window flags or configuration options, PixelHouse Mobile provides two beginner-friendly actions.

This follows the overall design philosophy of PixelHouse Mobile:

- Simple API
- Native Android implementation
- Android complexity hidden from the developer