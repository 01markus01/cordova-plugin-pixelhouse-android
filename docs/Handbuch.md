# PixelHouseMobile Handbuch

## Projektziel

PixelHouseMobile ist eine GDevelop-Erweiterung für Android-Funktionen.

Die eigentliche Android-Funktionalität wird über ein eigenes Cordova-Plugin bereitgestellt.

## Architektur

GDevelop

↓

PixelHouseMobile (Extension)

↓

cordova-plugin-pixelhouse-android

↓

Android Java

Dieses Handbuch wächst während der Entwicklung mit und dokumentiert alle Funktionen und deren Verwendung.

# Notifications

## Request Notification Permission

Fragt den Benutzer nach der Android-Berechtigung für Benachrichtigungen.
Diese Funktion sollte einmal beim Start der App aufgerufen werden.

---

## Schedule Notification

Parameter:

- Title
- Message
- Seconds

Beispiele:

Seconds = 0
→ Sofort anzeigen

Seconds = 5
→ Nach 5 Sekunden

Seconds = 60
→ Nach einer Minute

# Notifications

## Request Android notification permission

Requests the Android runtime notification permission if required.

---

## Schedule Notification

Schedules a local Android notification.

Parameters:
- Title
- Message
- Seconds (0 = immediately)

---

## Refresh Notification Status

Refreshes the current Android notification permission status.

---

## Notifications Enabled

Returns TRUE if notifications are currently enabled.

# Flashlight Module

## Description

The Flashlight module allows you to control the device flashlight with a very simple API.

All Android-specific implementation is handled internally by PixelHouse Mobile.

Devices without a flashlight are automatically detected. No additional checks are required.

---

## Actions

### Flashlight_On

Turns the device flashlight on.

If the device does not provide a flashlight, nothing happens.

---

### Flashlight_Off

Turns the device flashlight off.

If the device does not provide a flashlight, nothing happens.

---

### Refresh_Flashlight_Status

Refreshes the internally managed flashlight status.

Call this action before using the **Flashlight_Is_On** condition if the latest flashlight state is required.

---

## Conditions

### Flashlight_Is_On

Returns **true** if the flashlight is currently enabled by PixelHouse Mobile.

Returns **false** if:

- the flashlight is off
- the device has no flashlight
- the flashlight could not be enabled

---

## Design Philosophy

The Flashlight module intentionally exposes only a very small public API.

Android-specific complexity such as CameraManager, device compatibility and flashlight detection is handled internally by PixelHouse Mobile.