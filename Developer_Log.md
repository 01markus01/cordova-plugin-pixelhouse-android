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