// Handbuch.md

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


# Battery Module

## Description

The Battery module provides simple access to the device battery information.

All Android-specific implementation is handled internally by PixelHouse Mobile.

---

## Actions

### Refresh_Battery_Status

Refreshes the current battery information.

Call this action before using Battery_Is_Charging or Battery_Level() if the latest battery status is required.

---

## Conditions

### Battery_Is_Charging

Returns **true** if the device is currently charging.

Returns **false** if the device is running on battery power.

---

## Expressions

### Battery_Level()

Returns the current battery level as a percentage (0-100).

---

## Design Philosophy

The Battery module intentionally provides only the most commonly used battery information.

Android-specific implementation details are completely hidden from the developer.

# Device Module

## Description

The Device module provides basic information about the current Android device.

Device information is refreshed using a single action and can then be accessed through expressions.

This architecture ensures that Android APIs are only accessed when refreshing the data. Expressions simply return the cached values, resulting in better performance and a consistent API design.

---

## API Overview

### Actions

- Refresh_Device_Info

### Conditions

None

### Expressions

- Device_Manufacturer()
- Device_Model()
- Android_Version()

---

## Actions

### Refresh_Device_Info

Refreshes the current device information.

After calling this action, all Device expressions return the latest available values.

---

## Expressions

### Device_Manufacturer()

Returns the manufacturer of the current Android device.

Example:

```
OUKITEL
```

---

### Device_Model()

Returns the model name of the current Android device.

Example:

```
WP55 Pro
```

---

### Android_Version()

Returns the Android operating system version.

Example:

```
15
```

---

## Design Philosophy

The Device module follows the same architecture as the other PixelHouse Mobile modules.

Device information is retrieved only when the Refresh_Device_Info action is executed.

After refreshing, the expressions simply return cached values stored by the plugin.

Advantages:

- Fast expression evaluation
- No unnecessary Android API calls
- Consistent architecture across all modules
- Easy to understand and use within GDevelop

---

## Implementation

Android APIs used:

- Build.MANUFACTURER
- Build.MODEL
- Build.VERSION.RELEASE

The retrieved values are cached internally by the plugin and exposed through the corresponding expressions.