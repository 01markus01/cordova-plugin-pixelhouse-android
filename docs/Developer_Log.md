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