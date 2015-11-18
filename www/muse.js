var argscheck = require('cordova/argscheck'),
    channel = require('cordova/channel'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec'),
    cordova = require('cordova');

var PLUGIN_NAME = "Muse";

function Muse() {}

Muse.prototype.getMuseList = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "getMuseList", []);
}
Muse.prototype.connectToMuse = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "connectToMuse", []);
}
Muse.prototype.getConnectionState = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "getConnectionState", []);
}
Muse.prototype.disconnectMuse = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "disconnectMuse", []);
}
Muse.prototype.startRecording = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "startRecording", []);
}
Muse.prototype.setWatch = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "setWatch", []);
}
Muse.prototype.stopRecording = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "stopRecording", []);
}
Muse.prototype.getBlink = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "getBlink", []);
}
Muse.prototype.testConnection = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, PLUGIN_NAME, "testConnection", []);
}

/*Muse.prototype.getAccLeftRight = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAccLeftRight", []);
}

Muse.prototype.getAccForwardBackward = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAccForwardBackward", []);
}

Muse.prototype.getAccUpDown = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAccUpDown", []);
}

Muse.prototype.getEegLeftEar = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getEegLeftEar", []);
}

Muse.prototype.getEegRightEar = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getEegRightEar", []);
}

Muse.prototype.getEegLeftForehead = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getEegLeftForehead", []);
}

Muse.prototype.getEegRightForehead = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getEegRightForehead", []);
}

Muse.prototype.getAlphaRelativeChannel1 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAlphaRelativeChannel1", []);
}

Muse.prototype.getAlphaRelativeChannel2 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAlphaRelativeChannel2", []);
}

Muse.prototype.getAlphaRelativeChannel3 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAlphaRelativeChannel3", []);
}

Muse.prototype.getAlphaRelativeChannel4 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getAlphaRelativeChannel4", []);
}

Muse.prototype.getBetaRelativeChannel1 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getBetaRelativeChannel1", []);
}

Muse.prototype.getBetaRelativeChannel2 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getBetaRelativeChannel2", []);
}

Muse.prototype.getBetaRelativeChannel3 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getBetaRelativeChannel3", []);
}

Muse.prototype.getBetaRelativeChannel4 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getBetaRelativeChannel4", []);
}

Muse.prototype.getThetaRelativeChannel1 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getThetaRelativeChannel1", []);
}

Muse.prototype.getThetaRelativeChannel2 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getThetaRelativeChannel2", []);
}

Muse.prototype.getThetaRelativeChannel3 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getThetaRelativeChannel3", []);
}

Muse.prototype.getThetaRelativeChannel4 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getThetaRelativeChannel4", []);
}

Muse.prototype.getGammaRelativeChannel1 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getGammaRelativeChannel1", []);
}

Muse.prototype.getGammaRelativeChannel2 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getGammaRelativeChannel2", []);
}

Muse.prototype.getGammaRelativeChannel3 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getGammaRelativeChannel3", []);
}

Muse.prototype.getGammaRelativeChannel4 = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getGammaRelativeChannel4", []);
}

Muse.prototype.getRecordingData = function(successCallback, errorCallback) {
  exec(successCallback, errorCallback, PLUGIN_NAME, "getRecordingData", []);
}*/

module.exports = new Muse();
