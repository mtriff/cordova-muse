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

Muse.prototype.getAccForwardBackward = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getAccForwardBackward", []);
}

module.exports = new Muse();
