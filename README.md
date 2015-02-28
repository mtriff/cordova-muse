# cordova-muse

- Plugin to interface between a Cordova app and Muse device
- Currently only supports Android (libraries are currently unavailable for other platforms)
- Uses libmuse v1.0.1

# Install/Uninstall

To install, from the root of your Cordova app:
```
cordova plugin add https://github.com/mtriff/cordova-muse
```

To uninstall, from the root of your Cordova app:
```
cordova plugin remove com.mtriff.cordova.muse.plugin
```

# Documentation

All get_____ functions return an array of values, unless specified otherwise.  Details on the different values can be found in the [Muse SDK documentation](https://sites.google.com/a/interaxon.ca/muse-developer-site/home). The length of the array will vary based on how long the recording session was.

Each function takes two parameters, successCallback and errorCallback.  These should be functions that take one argument (usually an array) that will be called by the plugin once it completes the requested action.

getMuseList(successCallback, errorCallback)
-----------
Returns a list of Muse devices attached to the phone by Bluetooth.  Listed by MAC addresses.

connectToMuse(successCallback, errorCallback)
-----------
Connects to the first Muse device it finds attached to the phone by Bluetooth.

disconnectMuse(successCallback, errorCallback)
-----------
Disconnects from the Muse device if one is connected.

testConnection(successCallback, errorCallback)
-----------
Returns the status of the connection between the Muse headband and the users forehead.

Possible values are: Good Connection, Weak Connection, and No Connection
```
    Left Ear - Good Connection
    Right Ear - Good Connection
    Left Forehead - Good Connection
    Right Forehead - Good Connection
```

startRecording(successCallback, errorCallback)
-----------
Triggers the data lists to be refreshed and data begins to be collected.

stopRecording(successCallback, errorCallback)
-----------
Stops recording data.  Any events and data received by the app from the Muse device will be ignored.

getRecordingData(successCallback, errorCallback)
-----------
Returns a JSON object structured as follows containing the data for all of the variables recorded.

```
{
      ACC_LEFT_RIGHT: [1.0, 2.0, 3.0],
      ACC_FORWARD_BACKWARD: [1.0, 2.0, 3.0],
      ACC_UP_DOWN: [1.0, 2.0, 3.0],
      EEG_LEFT_EAR: [1.0, 2.0, 3.0],
      EEG_RIGHT_EAR: [1.0, 2.0, 3.0],
      EEG_LEFT_FOREHEAD: [1.0, 2.0, 3.0],
      EEG_RIGHT_FOREHEAD: [1.0, 2.0, 3.0],
      ALPHA_REL_1: [1.0, 2.0, 3.0],
      ALPHA_REL_2: [1.0, 2.0, 3.0],
      ALPHA_REL_3: [1.0, 2.0, 3.0],
      ALPHA_REL_4: [1.0, 2.0, 3.0],
      BETA_REL_1: [1.0, 2.0, 3.0],
      BETA_REL_2: [1.0, 2.0, 3.0],
      BETA_REL_3: [1.0, 2.0, 3.0],
      BETA_REL_4: [1.0, 2.0, 3.0],
      THETA_REL_1: [1.0, 2.0, 3.0],
      THETA_REL_2: [1.0, 2.0, 3.0],
      THETA_REL_3: [1.0, 2.0, 3.0],
      THETA_REL_4: [1.0, 2.0, 3.0],
      GAMMA_REL_1: [1.0, 2.0, 3.0],
      GAMMA_REL_2: [1.0, 2.0, 3.0],
      GAMMA_REL_3: [1.0, 2.0, 3.0],
      GAMMA_REL_4: [1.0, 2.0, 3.0],
      BLINK: [123456, 123456, 123456]
}
```

getAccLeftRight(successCallback, errorCallback)
-----------
Retrieves the X-axis accelerometer data from the previous recording.

getAccForwardBackward(successCallback, errorCallback)
-----------
Retrieves the Y-axis accelerometer data from the previous recording.

getAccUpDown(successCallback, errorCallback)
-----------
Retrieves the Z-axis accelerometer data from the previous recording.

getEegLeftEar(successCallback, errorCallback)
-----------
Retrieves the left ear sensor's EEG data from the previous recording.

getEegRightEar(successCallback, errorCallback)
-----------
Retrieves the right ear sensor's EEG data from the previous recording.

getEegLeftForehead(successCallback, errorCallback)
-----------
Retrieves the left forehead sensor's EEG data from the previous recording.

getEegRightForehead(successCallback, errorCallback)
-----------
Retrieves the right forehead sensor's EEG data from the previous recording.

getAlphaRelativeChannel1(successCallback, errorCallback)
-----------
Retrieves the Alpha Relative channel 1 data from the previous recording.

getAlphaRelativeChannel2(successCallback, errorCallback)
-----------
Retrieves the Alpha Relative channel 2 data from the previous recording.

getAlphaRelativeChannel3(successCallback, errorCallback)
-----------
Retrieves the Alpha Relative channel 3 data from the previous recording.

getAlphaRelativeChannel4(successCallback, errorCallback)
-----------
Retrieves the Alpha Relative channel 4 data from the previous recording.

getBetaRelativeChannel1(successCallback, errorCallback)
-----------
Retrieves the Beta Relative channel 1 data from the previous recording.

getBetaRelativeChannel2(successCallback, errorCallback)
-----------
Retrieves the Beta Relative channel 2 data from the previous recording.

getBetaRelativeChannel3(successCallback, errorCallback)
-----------
Retrieves the Beta Relative channel 3 data from the previous recording.

getBetaRelativeChannel4(successCallback, errorCallback)
-----------
Retrieves the Beta Relative channel 4 data from the previous recording.

getThetaRelativeChannel1(successCallback, errorCallback)
-----------
Retrieves the Theta Relative channel 1 data from the previous recording.

getThetaRelativeChannel2(successCallback, errorCallback)
-----------
Retrieves the Theta Relative channel 2 data from the previous recording.

getThetaRelativeChannel3(successCallback, errorCallback)
-----------
Retrieves the Theta Relative channel 3 data from the previous recording.

getThetaRelativeChannel4(successCallback, errorCallback)
-----------
Retrieves the Theta Relative channel 4 data from the previous recording.

getGammaRelativeChannel1(successCallback, errorCallback)
-----------
Retrieves the Gamma Relative channel 1 data from the previous recording.

getGammaRelativeChannel2(successCallback, errorCallback)
-----------
Retrieves the Gamma Relative channel 2 data from the previous recording.

getGammaRelativeChannel3(successCallback, errorCallback)
-----------
Retrieves the Gamma Relative channel 3 data from the previous recording.

getGammaRelativeChannel4(successCallback, errorCallback)
-----------
Retrieves the Gamma Relative channel 4 data from the previous recording.

getBlink(successCallback, errorCallback)
-----------
Retrieves the timestamps (in milliseconds) that it was recorded that the user blinked.
