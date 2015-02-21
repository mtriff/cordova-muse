package com.mtriff.cordova.muse.plugin;

import java.util.TimeZone;
import java.util.List;
import java.util.Arrays;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;
import android.util.Log;

import com.interaxon.libmuse.*;

public class Plugin extends CordovaPlugin {
    public static final String TAG = "MUSE";

    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";

    private Muse connectedMuse;
    private List<Muse> pairedMuses;

    private ConnectionListener connectionListener;


    class ConnectionListener extends MuseConnectionListener {

        CallbackContext callbackContext;

        ConnectionListener() {
        }
        
        public void setCallbackContext(CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

        @Override
        public void receiveMuseConnectionPacket(MuseConnectionPacket p) {
            final ConnectionState current = p.getCurrentConnectionState();
            final String status = p.getPreviousConnectionState().toString() +
                         " -> " + current;
            final String full = "Muse " + p.getSource().getMacAddress() +
                                " " + status;
            Log.i(TAG, full);
            if (current == ConnectionState.CONNECTED) {
                Log.i(TAG, "Muse connected");
                callbackContext.success("Connected to " + p.getSource().getMacAddress());
            }
                // return "Not connected, connection status is: " + muse.getConnectionState();
        }
    }




    /**
     * Constructor.
     */
    public Plugin() {
        connectionListener = new ConnectionListener();
        Log.i(TAG, "libmuse version=" + LibMuseVersion.SDK_VERSION);
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Plugin.uuid = getUuid();

        getMuseList();
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action            The action to execute.
     * @param args              JSONArry of arguments for the plugin.
     * @param callbackContext   The callback id used when calling back into JavaScript.
     * @return                  True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.i(TAG, "Action received: " + action);
        if (action.equals("getDeviceInfo")) {
            JSONObject r = new JSONObject();
            r.put("uuid", Plugin.uuid);
            r.put("version", this.getOSVersion());
            r.put("platform", this.getPlatform());
            r.put("model", this.getModel());
            if (pairedMuses.size() != 0) {
                r.put("muse", pairedMuses.get(0).getMacAddress());
            } else {
                r.put("muse", "No muse attached");
            }
            callbackContext.success(r);
        } else if (action.equals("getMuseList")) {
            String[] museList = getMuseList();
            if (museList != null) {
                JSONArray jsonMuseList = new JSONArray(Arrays.asList(museList));
                callbackContext.success(jsonMuseList);
            }
        } else if (action.equals("connectToMuse")) {
            // if (args.size() > 0) {
                // call connectToMuse(args[0])
            // } else {
            connectToMuse(callbackContext);
            // if ()
            // }
            // callbackContext.success(returnMessage);
        } else if (action.equals("disconnectMuse")) {
            disconnectMuse();
            callbackContext.success("Disconnected.");
        }
        else {
            return false;
        }
        return true;
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------
    /**
      * Returns an array of the MAC addresses of all the Muses attached to the device
      */
    private String[] getMuseList() {
        MuseManager.refreshPairedMuses();
        pairedMuses = MuseManager.getPairedMuses();
        String[] museMacs = new String[pairedMuses.size()];
        for (int i = 0; i < pairedMuses.size(); i++) {
            museMacs[i] = pairedMuses.get(i).getMacAddress();
        }
        Log.i(TAG, "Found " + pairedMuses.size() + " paired muse devices.");
        return museMacs;
    }

    private String connectToMuse(String macAddress, CallbackContext callbackContext) {
        for (Muse muse : pairedMuses) {
            if (muse.getMacAddress().equals(macAddress)) {
                try {
                    ConnectionState state = muse.getConnectionState();
                    Log.i(TAG, "ConnectionState: " + state.toString());
                    if (true || state != ConnectionState.CONNECTED && state != ConnectionState.CONNECTING) {
                        connectionListener.setCallbackContext(callbackContext);
                        final Muse museToConnect = muse;
                        museToConnect.setPreset(MusePreset.PRESET_14);
                        museToConnect.registerConnectionListener(connectionListener);
                        museToConnect.enableDataTransmission(true);
                        Log.i(TAG, "Connecting...");
                        
                        try {
                            cordova.getThreadPool().execute(new Runnable() {
                                public void run() {
                                    museToConnect.runAsynchronously();
                                    // museToConnect.connect();
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        connectedMuse = muse;                        
                    } else {
                        Log.i(TAG, "Device is already connected or is connecting." + state);
                        callbackContext.success(state.toString());
                    }
                } catch(Exception ex) {
                    return ex.getMessage();
                }
            }
        }
        return "Unable to find " + macAddress + " device.  Check to make sure this device is connected.";
    }

    private String connectToMuse(CallbackContext callbackContext) {
        getMuseList();
        return connectToMuse(pairedMuses.get(0).getMacAddress(), callbackContext);
    }
    
    private void disconnectMuse() {
        if (connectedMuse != null) {
            connectedMuse.disconnect(true);
        }
    }
    // private String executeMuse

    // Device

    /**
     * Get the OS name.
     * 
     * @return
     */
    public String getPlatform() {
        String platform;
        if (isAmazonDevice()) {
            platform = AMAZON_PLATFORM;
        } else {
            platform = ANDROID_PLATFORM;
        }
        return platform;
    }

    /**
     * Get the device's Universally Unique Identifier (UUID).
     *
     * @return
     */
    public String getUuid() {
        String uuid = Settings.Secure.getString(this.cordova.getActivity().getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return uuid;
    }

    public String getModel() {
        String model = android.os.Build.MODEL;
        return model;
    }

    public String getProductName() {
        String productname = android.os.Build.PRODUCT;
        return productname;
    }

    /**
     * Get the OS version.
     *
     * @return
     */
    public String getOSVersion() {
        String osversion = android.os.Build.VERSION.RELEASE;
        return osversion;
    }

    public String getSDKVersion() {
        @SuppressWarnings("deprecation")
        String sdkversion = android.os.Build.VERSION.SDK;
        return sdkversion;
    }

    public String getTimeZoneID() {
        TimeZone tz = TimeZone.getDefault();
        return (tz.getID());
    }

    /**
     * Function to check if the device is manufactured by Amazon
     * 
     * @return
     */
    public boolean isAmazonDevice() {
        if (android.os.Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
            return true;
        }
        return false;
    }

}
