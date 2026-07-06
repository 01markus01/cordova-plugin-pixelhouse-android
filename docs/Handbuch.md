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