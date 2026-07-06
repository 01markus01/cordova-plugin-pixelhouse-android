// www/PixelHouseAndroid.js

var exec = require('cordova/exec');

module.exports = {
  openDefaultNotificationChannel: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'openDefaultNotificationChannel', []);
  },

  openNotificationChannel: function (channelId, success, error) {
    exec(success, error, 'PixelHouseAndroid', 'openNotificationChannel', [channelId]);
  },

  prepareDefaultNotificationChannel: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'prepareDefaultNotificationChannel', []);
  },

  scheduleNotification: function (title, message, seconds, success, error) {
    exec(success, error, 'PixelHouseAndroid', 'scheduleNotification', [title, message, seconds]);
  },

  requestNotificationPermission: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'requestNotificationPermission', []);
  },

  areNotificationsEnabled: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'areNotificationsEnabled', []);
  },

  flashlightOn: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'flashlightOn', []);
  },

  flashlightOff: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'flashlightOff', []);
  },

  isFlashlightOn: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'isFlashlightOn', []);
  },

  refreshBatteryStatus: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'refreshBatteryStatus', []);
  },

  isBatteryCharging: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'isBatteryCharging', []);
  },

  getBatteryLevel: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'getBatteryLevel', []);
  }
};