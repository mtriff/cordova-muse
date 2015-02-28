cordova.define("com.mtriff.cordova.muse.plugin.muse", function(require, exports, module) { var argscheck = require('cordova/argscheck'),
    channel = require('cordova/channel'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec'),
    cordova = require('cordova');

function Muse() {}

Muse.prototype.getMuseList = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getMuseList", []);
}

Muse.prototype.connectToMuse = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "connectToMuse", []);
}

Muse.prototype.disconnectMuse = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "disconnectMuse", []);
}

Muse.prototype.startRecording = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "startRecording", []);
}

Muse.prototype.stopRecording = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "stopRecording", []);
}

Muse.prototype.getAccLeftRight = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAccLeftRight", []);
}

Muse.prototype.getAccForwardBackward = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAccForwardBackward", []);
}

Muse.prototype.getAccUpDown = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAccUpDown", []);
}

Muse.prototype.getEegLeftEar = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getEegLeftEar", []);
}

Muse.prototype.getEegRightEar = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getEegRightEar", []);
}

Muse.prototype.getEegLeftForehead = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getEegLeftForehead", []);
}

Muse.prototype.getEegRightForehead = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getEegRightForehead", []);
}

Muse.prototype.getAlphaRelativeChannel1 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAlphaRelativeChannel1", []);
}

Muse.prototype.getAlphaRelativeChannel2 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAlphaRelativeChannel2", []);
}

Muse.prototype.getAlphaRelativeChannel3 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAlphaRelativeChannel3", []);
}

Muse.prototype.getAlphaRelativeChannel4 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAlphaRelativeChannel4", []);
}

Muse.prototype.getBetaRelativeChannel1 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getBetaRelativeChannel1", []);
}

Muse.prototype.getBetaRelativeChannel2 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getBetaRelativeChannel2", []);
}

Muse.prototype.getBetaRelativeChannel3 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getBetaRelativeChannel3", []);
}

Muse.prototype.getBetaRelativeChannel4 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getBetaRelativeChannel4", []);
}

Muse.prototype.getThetaRelativeChannel1 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getThetaRelativeChannel1", []);
}

Muse.prototype.getThetaRelativeChannel2 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getThetaRelativeChannel2", []);
}

Muse.prototype.getThetaRelativeChannel3 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getThetaRelativeChannel3", []);
}

Muse.prototype.getThetaRelativeChannel4 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getThetaRelativeChannel4", []);
}

Muse.prototype.getGammaRelativeChannel1 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getGammaRelativeChannel1", []);
}

Muse.prototype.getGammaRelativeChannel2 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getGammaRelativeChannel2", []);
}

Muse.prototype.getGammaRelativeChannel3 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getGammaRelativeChannel3", []);
}

Muse.prototype.getGammaRelativeChannel4 = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getGammaRelativeChannel4", []);
}

Muse.prototype.getBlink = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getBlink", []);
}

Muse.prototype.testConnection = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "testConnection", []);
}

Muse.prototype.getRecordingData = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getRecordingData", []);
}



module.exports = new Muse();

});
