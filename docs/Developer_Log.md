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