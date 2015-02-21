var argscheck = require('cordova/argscheck'),
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

Muse.prototype.getBlink = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getBlink", []);
}

module.exports = new Muse();
