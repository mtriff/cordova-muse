var argscheck = require('cordova/argscheck'),
    channel = require('cordova/channel'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec'),
    cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

/**
 * This represents the mobile device, and provides properties for inspecting the model, version, UUID of the
 * phone, etc.
 * @constructor
 */
function Muse() {
    this.available = false;
    this.platform = null;
    this.version = null;
    this.uuid = null;
    this.cordova = null;
    this.model = null;

    var me = this;

    channel.onCordovaReady.subscribe(function() {
        me.getInfo(function(info) {
            //ignoring info.cordova returning from native, we should use value from cordova.version defined in cordova.js
            var buildLabel = cordova.version;
            me.available = true;
            me.platform = info.platform;
            me.version = info.version;
            me.uuid = info.uuid;
            me.cordova = buildLabel;
            me.model = info.model;
            // me.muse = info.muse;
            // channel.onCordovaInfoReady.fire();
        },function(e) {
            me.available = false;
            utils.alert("[ERROR] Error initializing Cordova: " + e);
        });

        me.getMuseList(function(museList) {
            me.muse = museList[0];
            channel.onCordovaInfoReady.fire();
        },function(e) {
            me.available = false;
            utils.alert("[ERROR] Error initializing Cordova: " + e);
        });
    });
}

/**
 * Get Muse info
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Muse.prototype.getInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Plugin.getInfo', arguments);
    exec(successCallback, errorCallback, "Muse", "getDeviceInfo", []);
};

Muse.prototype.getMuseList = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "getMuseList", []);
}

Muse.prototype.connectToMuse = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "connectToMuse", []);
}

Muse.prototype.disconnectMuse = function(successCallback, errorCallback) {
    exec(successCallback, errorCallback, "Muse", "disconnectMuse", []);
}

module.exports = new Muse();
