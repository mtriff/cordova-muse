package com.mtriff.cordova.muse.plugin;

import java.util.TimeZone;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.interaxon.libmuse.*;

public class Plugin extends CordovaPlugin {
    public static final String TAG = "MUSE";

    private Muse connectedMuse;
    private List<Muse> pairedMuses;

    private ConnectionListener connectionListener;
    private DataListener dataListener;
    private boolean recordData;

    // Data Packets
    private List<String> acc_forward_backward;
    private List<String> acc_up_down;
    private List<String> acc_left_right;

    private List<String> eeg_left_ear;
    private List<String> eeg_left_forehead;
    private List<String> eeg_right_forehead;
    private List<String> eeg_right_ear;

    private List<String> alpha_rel_1;
    private List<String> alpha_rel_2;
    private List<String> alpha_rel_3;
    private List<String> alpha_rel_4;

    private List<String> blink;

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
                toastShort("Connected to " + p.getSource().getMacAddress());
                callbackContext.success("Connected to " + p.getSource().getMacAddress());
            }
            // return "Not connected, connection status is: " + muse.getConnectionState();
        }

        private void toastShort(final String message) {
            try {
                cordova.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(cordova.getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
    }

    class DataListener extends MuseDataListener {
        DataListener() {
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket p) {
            Log.v(TAG, "Received data packet of type " + p.getPacketType() + " recordData set to " + recordData);
            if(recordData) {
                switch (p.getPacketType()) {
                    case EEG:
                        updateEeg(p.getValues());
                        break;
                    case ACCELEROMETER:
                        updateAccelerometer(p.getValues());
                        break;
                    case ALPHA_RELATIVE:
                        updateAlphaRelative(p.getValues());
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
            if (p.getHeadbandOn() && p.getBlink()) {
                Log.i(TAG, "blink");
                Calendar cal = Calendar.getInstance();
                blink.add("" + cal.getTimeInMillis());
            }
        }

        private void updateAccelerometer(final ArrayList<Double> data) {
            acc_forward_backward.add(String.format(
                "%6.2f", data.get(Accelerometer.FORWARD_BACKWARD.ordinal())));
            acc_up_down.add(String.format(
                "%6.2f", data.get(Accelerometer.UP_DOWN.ordinal())));
            acc_left_right.add(String.format(
                "%6.2f", data.get(Accelerometer.LEFT_RIGHT.ordinal())));
        }

        private void updateEeg(final ArrayList<Double> data) {
            eeg_left_ear.add(String.format(
                "%6.2f", data.get(Eeg.TP9.ordinal())));
            eeg_left_forehead.add(String.format(
                "%6.2f", data.get(Eeg.FP1.ordinal())));
            eeg_right_forehead.add(String.format(
                "%6.2f", data.get(Eeg.FP2.ordinal())));
            eeg_right_ear.add(String.format(
                "%6.2f", data.get(Eeg.TP10.ordinal())));
        }

        private void updateAlphaRelative(final ArrayList<Double> data) {
            alpha_rel_1.add(String.format(
                "%6.2f", data.get(Eeg.TP9.ordinal())));
            alpha_rel_2.add(String.format(
                "%6.2f", data.get(Eeg.FP1.ordinal())));
            alpha_rel_3.add(String.format(
                "%6.2f", data.get(Eeg.FP2.ordinal())));
            alpha_rel_4.add(String.format(
                "%6.2f", data.get(Eeg.TP10.ordinal())));
        }
    }


    /**
     * Constructor.
     */
    public Plugin() {
        connectionListener = new ConnectionListener();
        dataListener = new DataListener();
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
        if (action.equals("getMuseList")) {
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
        } else if (action.equals("startRecording")) {
            startRecording();
            toastShort("Started Recording");
            callbackContext.success("Started Recording");
        } else if (action.equals("stopRecording")) {
            stopRecording();
            toastShort("Stopped Recording");
            callbackContext.success("Stopped Recording");
        } else if (action.equals("getAccForwardBackward")) {
            if (acc_forward_backward == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.i(TAG, "Returning array of length " + acc_forward_backward.size());
            Log.i(TAG, "First value is " + acc_forward_backward.get(0));
            callbackContext.success(new JSONArray(acc_forward_backward));
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
                        muse.registerDataListener(dataListener,
                                                  MuseDataPacketType.ACCELEROMETER);
                        muse.registerDataListener(dataListener,
                                                  MuseDataPacketType.EEG);
                        muse.registerDataListener(dataListener,
                                                  MuseDataPacketType.ALPHA_RELATIVE);
                        muse.registerDataListener(dataListener,
                                                  MuseDataPacketType.ARTIFACTS);
                        museToConnect.enableDataTransmission(true);
                        Log.i(TAG, "Connecting...");
                        toastShort("Connecting...");
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

    private void startRecording() {
        resetDataPacketLists();
        recordData = true;
    }

    private void stopRecording() {
        recordData = false;
    }

    private void resetDataPacketLists() {
        acc_forward_backward = new LinkedList<String>();
        acc_up_down = new LinkedList<String>();
        acc_left_right = new LinkedList<String>();

        eeg_left_ear = new LinkedList<String>();
        eeg_left_forehead = new LinkedList<String>();
        eeg_right_forehead = new LinkedList<String>();
        eeg_right_ear = new LinkedList<String>();

        alpha_rel_1 = new LinkedList<String>();
        alpha_rel_2 = new LinkedList<String>();
        alpha_rel_3 = new LinkedList<String>();
        alpha_rel_4 = new LinkedList<String>();

        blink = new LinkedList<String>();
    }

    private void toastLong(String message) {
        Toast.makeText(cordova.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void toastShort(String message) {
        Toast.makeText(cordova.getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
