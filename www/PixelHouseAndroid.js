var exec = require('cordova/exec');

module.exports = {
  openDefaultNotificationChannel: function (success, error) {
    exec(success, error, 'PixelHouseAndroid', 'openDefaultNotificationChannel', []);
  },
  openNotificationChannel: function (channelId, success, error) {
    exec(success, error, 'PixelHouseAndroid', 'openNotificationChannel', [channelId]);
  }
};
