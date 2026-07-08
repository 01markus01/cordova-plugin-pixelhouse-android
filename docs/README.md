# PixelHouse Mobile

**Native Android Extension for GDevelop**

**Simple • Native • Beginner-Friendly**

---

## Overview

PixelHouse Mobile is a native Android extension for GDevelop that provides easy access to common Android features without requiring Java or Android development knowledge.

Instead of exposing Android APIs directly, PixelHouse Mobile offers a simple set of Actions, Conditions and Expressions that integrate naturally into GDevelop's event system.

All Android-specific implementation details such as permissions, callbacks, compatibility handling and native APIs are managed internally by the extension.

The Android functionality is implemented through a custom Cordova plugin.

---

# Features

| Module | Status |
|---------|:------:|
| Notifications | ✅ |
| Flashlight | ✅ |
| Battery | ✅ |
| Device Information | ✅ |
| Camera | ✅ |
| Keep Screen On | ✅ |

---

# Design Philosophy

PixelHouse Mobile follows one simple principle:

> **Keep the public API as small and beginner-friendly as possible while hiding Android complexity.**

Most Android APIs require permissions, callbacks, compatibility checks and platform-specific code.

PixelHouse Mobile hides these implementation details and provides a clean and intuitive API designed specifically for GDevelop developers.

---

# Architecture

```
GDevelop
      │
      ▼
PixelHouse Mobile Extension
      │
      ▼
cordova-plugin-pixelhouse-android
      │
      ▼
Native Android (Java)
```

The communication between GDevelop and Android is fully managed by the extension.

Developers never need to write Java code.

---

# Notification Module

## Description

The Notification module allows applications to create native Android notifications.

Notifications can be displayed immediately or after a specified delay.

Notifications continue to work even after the application has been closed.

PixelHouse Mobile automatically selects the most appropriate Android notification implementation.

---

## API Overview

### Actions

- Prepare Default Notification Channel
- Open Default Notification Channel
- Open Notification Channel
- Request Notification Permission
- Schedule Notification
- Open Exact Alarm Settings

### Conditions

- Notifications Enabled

### Expressions

None

---

## Request Notification Permission

Requests the Android notification permission.

Android 13 and newer require this permission before notifications can be displayed.

This action should normally be called once during application startup.

---

## Schedule Notification

Creates a local Android notification.

### Parameters

| Parameter | Description |
|-----------|-------------|
| Title | Notification title |
| Message | Notification message |
| Seconds | Delay before showing the notification |

### Examples

| Seconds | Result |
|---------:|--------|
| 0 | Show immediately |
| 5 | Show after 5 seconds |
| 60 | Show after 1 minute |
| 180 | Show after 3 minutes |

---

## Open Default Notification Channel

Opens the Android settings for the default PixelHouse notification channel.

Developers can instruct users to customize notification behavior such as sound, vibration and importance.

---

## Open Notification Channel

Opens the settings of a specific notification channel.

The channel identifier is provided by the developer.

---

## Open Exact Alarm Settings

Opens the Android system settings where users can enable **Exact Alarms** for the application.

Exact Alarms improve notification timing accuracy.

PixelHouse Mobile automatically falls back to standard Android alarms when Exact Alarms are unavailable.

Developers only need to use this action when highly accurate notification timing is required.

---

## Notifications Enabled

Returns **true** if Android notifications are currently enabled for the application.

Returns **false** otherwise.

---

## Design

The Notification module automatically chooses the best available Android implementation.

Features include:

- Native Android notifications
- Background notifications
- Automatic notification channels
- Exact Alarm support
- Automatic fallback when Exact Alarms are unavailable
- Full Android compatibility handling

The entire Android implementation remains hidden from the GDevelop developer.


---

# Flashlight Module

## Description

The Flashlight module provides a simple interface for controlling the device flashlight.

PixelHouse Mobile automatically detects whether the device supports a flashlight and hides all Android-specific implementation details.

No additional compatibility checks are required.

---

## API Overview

### Actions

- Flashlight_On
- Flashlight_Off
- Refresh_Flashlight_Status

### Conditions

- Flashlight_Is_On

### Expressions

None

---

## Flashlight_On

Turns the device flashlight on.

If the device does not provide a flashlight, nothing happens.

---

## Flashlight_Off

Turns the device flashlight off.

If the flashlight is unavailable, the action safely returns without causing an error.

---

## Refresh_Flashlight_Status

Refreshes the internally managed flashlight state.

Call this action before checking **Flashlight_Is_On** if the latest state is required.

---

## Flashlight_Is_On

Returns **true** if the flashlight is currently enabled by PixelHouse Mobile.

Returns **false** if:

- The flashlight is turned off.
- The device has no flashlight.
- Android could not enable the flashlight.

---

## Android Implementation

Implemented using:

- CameraManager
- CameraCharacteristics
- CameraManager.setTorchMode()

PixelHouse Mobile automatically selects the correct rear camera and detects flashlight support.

---

## Design

The Flashlight module intentionally exposes only a very small public API.

Android camera handling, compatibility detection and flashlight management remain completely hidden from the developer.

---

# Battery Module

## Description

The Battery module provides quick access to the device battery information.

Battery information is refreshed only when requested, reducing unnecessary Android API calls.

---

## API Overview

### Actions

- Refresh_Battery_Status

### Conditions

- Battery_Is_Charging

### Expressions

- Battery_Level()

---

## Refresh_Battery_Status

Refreshes all internally stored battery information.

This action should be executed before reading battery values if the latest information is required.

---

## Battery_Is_Charging

Returns **true** if the device is currently charging.

Returns **false** if the device is running on battery power.

---

## Battery_Level()

Returns the current battery level as an integer percentage.

Example:

```
87
```

Range:

```
0 - 100
```

---

## Android Implementation

Implemented using:

- BatteryManager
- Intent.ACTION_BATTERY_CHANGED

Battery values are cached internally after refreshing.

---

## Design

Battery information is retrieved only when requested.

Expressions simply return cached values without accessing Android APIs directly.

Advantages:

- Fast execution
- Consistent API
- Better performance
- Beginner-friendly usage

---

# Device Module

## Description

The Device module provides basic information about the current Android device.

Device information is refreshed once and stored internally.

Expressions always return cached values.

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

## Refresh_Device_Info

Refreshes all available device information.

After calling this action, all expressions return the latest available values.

---

## Device_Manufacturer()

Returns the device manufacturer.

Example:

```
OUKITEL
```

---

## Device_Model()

Returns the current device model.

Example:

```
WP55 Pro
```

---

## Android_Version()

Returns the installed Android version.

Example:

```
15
```

---

## Android Implementation

Implemented using:

- Build.MANUFACTURER
- Build.MODEL
- Build.VERSION.RELEASE

Values are cached internally after refreshing.

---

## Design

The Device module follows the same architecture as all PixelHouse Mobile modules.

Android APIs are accessed only during refresh.

Advantages:

- Fast expression evaluation
- Cached values
- Consistent architecture
- Reduced Android API calls

---

# Camera Module

## Description

The Camera module provides simple access to the native Android camera.

PixelHouse Mobile completely hides the Android camera implementation.

Captured pictures are automatically stored by Android in the device gallery.

---

## API Overview

### Actions

- Request Camera Permission
- Take Picture

### Conditions

- Camera Permission Granted
- Picture Taken

### Expressions

None

---

## Request Camera Permission

Requests the Android runtime camera permission.

This action should normally be called before opening the camera.

---

## Take Picture

Launches the native Android camera application.

After the user confirms the captured picture, Android automatically saves the image.

No manual file handling is required.

---

## Camera Permission Granted

Returns **true** if the application currently has camera permission.

Returns **false** otherwise.

---

## Picture Taken

Returns **true** if the last picture was successfully captured and confirmed.

Returns **false** if:

- The user cancelled the camera.
- Permission was denied.
- No picture was taken.

---

## Android Implementation

Implemented using:

- Android Camera Intent
- Runtime Camera Permission
- MediaStore
- Activity Result API

Captured images are automatically saved by Android.

---

## Design

Like every PixelHouse Mobile module, the Camera module follows a simple workflow.

```
Request Camera Permission
            │
            ▼
Camera Permission Granted
            │
            ▼
Take Picture
            │
            ▼
Picture Taken
```

The complete Android camera implementation remains hidden from the developer.


---

# Keep Screen On Module

## Description

The Keep Screen On module prevents Android from turning off the display while your game or application is running.

This is especially useful for:

- Games
- Visual novels
- Idle games
- Kiosk applications
- Dashboards
- Navigation apps
- Media players
- Presentation software

The normal Android screen timeout can be restored at any time.

---

## API Overview

### Actions

- Keep Screen On
- Allow Screen Off

### Conditions

None

### Expressions

None

---

## Keep Screen On

Prevents Android from turning off the display.

The screen remains active until the developer explicitly restores the default timeout or the application closes.

---

## Allow Screen Off

Restores Android's normal screen timeout behavior.

After calling this action, the operating system manages the display timeout as usual.

---

## Android Implementation

Implemented using:

- WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON

The implementation automatically runs on the Android UI thread to ensure maximum compatibility.

---

## Design

The Keep Screen On module intentionally exposes only two simple actions.

No additional settings, flags or Android knowledge are required.

This follows the overall PixelHouse Mobile philosophy:

- Simple API
- Native Android implementation
- Android complexity hidden from the developer

---

# Why PixelHouse Mobile?

Android development often requires:

- Runtime permissions
- Native Java code
- Android Studio knowledge
- Callbacks
- Compatibility handling
- Device-specific checks

PixelHouse Mobile hides all of this.

Developers work exclusively with familiar GDevelop Actions, Conditions and Expressions while the extension handles the native Android implementation internally.

This allows beginners to use advanced Android features without writing Java code.

---

# Current Modules

| Module | Status |
|---------|:------:|
| Notifications | ✅ Complete |
| Flashlight | ✅ Complete |
| Battery | ✅ Complete |
| Device Information | ✅ Complete |
| Camera | ✅ Complete |
| Keep Screen On | ✅ Complete |

---

# Roadmap

The following modules are currently planned for future releases:

### Android

- QR Scanner
- NFC
- Biometric Authentication
- Volume Button Events
- Notch / Display Cutout Information
- Bluetooth Utilities
- App Launcher
- Screen Brightness Control
- Live Wallpaper Support

### Cross Platform

- Additional utility functions
- More device information
- Developer helper tools
- Quality-of-life improvements

The roadmap may change depending on community feedback and future Android updates.

---

# Contributing

Suggestions, feature requests and bug reports are always welcome.

If you discover a bug or have an idea for a new feature, please open an issue on GitHub.

Community feedback helps improve PixelHouse Mobile for all GDevelop developers.

---

# License

MIT License

Copyright (c) PixelHouse Apps

---

# Credits

Developed by **PixelHouse Apps**

Designed for the **GDevelop** community.

Special focus:

- Beginner-friendly API
- Native Android integration
- Clean architecture
- Long-term maintainability

---

# Final Notes

PixelHouse Mobile was created with one clear goal:

> **Make native Android features accessible to every GDevelop developer.**

Instead of forcing developers to learn Android APIs, Java or Cordova, PixelHouse Mobile provides a clean, consistent and easy-to-use interface that feels like a natural part of GDevelop.

Every module follows the same architecture and design principles, making the extension easy to learn, easy to maintain and easy to extend in future releases.

Happy developing! 🚀