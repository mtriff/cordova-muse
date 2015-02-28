# Muse Plugin Example App

To recreate this app:

```
cordova create exampleApp com.example.muse MuseExampleApp
cd exampleApp
cordova platform add android
cordova plugin add https://github.com/mtriff/cordova-muse

```

Add the following permissions to `platforms/android/AndroidManifest.xml`.

```
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
```

Copy in `index.html`, `js/index.js` and `css/index.css` files.

```
cordova run android
```