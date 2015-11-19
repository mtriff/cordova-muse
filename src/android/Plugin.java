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
    private List<String> acc;
    private List<String> eeg;

    private List<String> alpha_abs;
    private List<String> beta_abs;
    private List<String> delta_abs;
    private List<String> theta_abs;
    private List<String> gamma_abs;

    private List<String> alpha_rel;
    private List<String> beta_rel;
    private List<String> delta_rel;
    private List<String> theta_rel;
    private List<String> gamma_rel;

    private List<String> blink;

    private List<Double> horseshoe;

  /*
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
  private List<String> beta_rel_1;
  private List<String> beta_rel_2;
  private List<String> beta_rel_3;
  private List<String> beta_rel_4;
  private List<String> theta_rel_1;
  private List<String> theta_rel_2;
  private List<String> theta_rel_3;
  private List<String> theta_rel_4;
  private List<String> gamma_rel_1;
  private List<String> gamma_rel_2;
  private List<String> gamma_rel_3;
  private List<String> gamma_rel_4;*/

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
            } else if (current == ConnectionState.DISCONNECTED) {
                horseshoe = null;
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
        CallbackContext callbackContext;

        DataListener() {
        }

        public void setCallbackContext(CallbackContext callbackContext) {
            this.callbackContext = callbackContext;
        }

        @Override
        public void receiveMuseDataPacket(MuseDataPacket p) {
            Log.v(TAG, "Received data packet of type " + p.getPacketType() + " recordData set to " + recordData);
            MuseDataPacketType packetType = p.getPacketType();
            if (recordData || packetType == MuseDataPacketType.HORSESHOE) {
                switch (packetType) {
                    case EEG:
                        updateEeg(p.getValues());
                        break;
                    case ACCELEROMETER:
                        updateAccelerometer(p.getValues());
                        break;
                    case ALPHA_ABSOLUTE:
                        updateAbsolute(alpha_abs, p.getValues());
                        break;
                    case BETA_ABSOLUTE:
                        updateAbsolute(beta_abs, p.getValues());
                        break;
                    case DELTA_ABSOLUTE:
                        updateAbsolute(delta_abs, p.getValues());
                        break;
                    case THETA_ABSOLUTE:
                        updateAbsolute(theta_abs, p.getValues());
                        break;
                    case GAMMA_ABSOLUTE:
                        updateAbsolute(gamma_abs, p.getValues());
                        break;
                    case ALPHA_RELATIVE:
                        updateRelative(alpha_rel, p.getValues());
                        break;
                    case BETA_RELATIVE:
                        updateRelative(beta_rel, p.getValues());
                        break;
                    case DELTA_RELATIVE:
                        updateRelative(delta_rel, p.getValues());
                        break;
                    case THETA_RELATIVE:
                        updateRelative(theta_rel, p.getValues());
                        break;
                    case GAMMA_RELATIVE:
                        updateRelative(gamma_rel, p.getValues());
                        break;
                    case HORSESHOE:
                        updateHorseshoe(p.getValues());
                        break;
                    default:
                        break;
                }
                if (callbackContext != null) {
                    //Log.v(TAG, "callbackContext JSONObject");
                    JSONObject obj = null;
                    PluginResult pluginResult = null;
                    try {
                        obj = new JSONObject();
                        obj.put("ACC", new JSONArray(acc));
                        obj.put("EEG", new JSONArray(eeg));
                        //CHANNEL_ABS
                        obj.put("ALPHA_ABS", new JSONArray(alpha_abs));
                        obj.put("BETA_ABS", new JSONArray(beta_abs));
                        obj.put("DELTA_ABS", new JSONArray(delta_abs));
                        obj.put("THETA_ABS", new JSONArray(theta_abs));
                        obj.put("GAMMA_ABS", new JSONArray(gamma_abs));
                        //CHANNEL_REL
                        obj.put("ALPHA_REL", new JSONArray(alpha_rel));
                        obj.put("BETA_REL", new JSONArray(beta_rel));
                        obj.put("DELTA_REL", new JSONArray(delta_rel));
                        obj.put("THETA_REL", new JSONArray(theta_rel));
                        obj.put("GAMMA_REL", new JSONArray(gamma_rel));
                        //obj.put("BLINK", new JSONArray(blink));
                        obj.put("TIMESTAMP", System.currentTimeMillis());
                        pluginResult = new PluginResult(PluginResult.Status.OK, obj);
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                        pluginResult = new PluginResult(PluginResult.Status.JSON_EXCEPTION, "Json exception at receiveMuseDataPacket");
                    } finally {
                        pluginResult.setKeepCallback(true);
                        callbackContext.sendPluginResult(pluginResult);
                    }
                }
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket p) {
            if (p.getHeadbandOn() && p.getBlink()) {
                Log.i(TAG, "blink");
                //Calendar cal = Calendar.getInstance();cal.getTimeInMillis()
                blink.add(String.valueOf(System.currentTimeMillis()));
            }
        }

        private void updateAccelerometer(final ArrayList<Double> data) {
            acc.clear();
            acc.add(String.format(
                    "%6.2f", data.get(Accelerometer.FORWARD_BACKWARD.ordinal())));
            acc.add(String.format(
                    "%6.2f", data.get(Accelerometer.UP_DOWN.ordinal())));
            acc.add(String.format(
                    "%6.2f", data.get(Accelerometer.LEFT_RIGHT.ordinal())));

            /*acc_forward_backward.add(String.format(
                    "%6.2f", data.get(Accelerometer.FORWARD_BACKWARD.ordinal())));
            acc_up_down.add(String.format(
                    "%6.2f", data.get(Accelerometer.UP_DOWN.ordinal())));
            acc_left_right.add(String.format(
                    "%6.2f", data.get(Accelerometer.LEFT_RIGHT.ordinal())));*/
        }

        private void updateEeg(final ArrayList<Double> data) {
            eeg.clear();
            eeg.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            eeg.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            eeg.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            eeg.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));

            /*eeg_left_ear.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            eeg_left_forehead.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            eeg_right_forehead.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            eeg_right_ear.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));*/
        }

        private void updateAbsolute(List<String> abs, final ArrayList<Double> data) {
            abs.clear();
            abs.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            abs.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            abs.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            abs.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));
        }

        private void updateRelative(List<String> rel, final ArrayList<Double> data) {
            rel.clear();
            rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));
        }

       /* private void updateAlphaRelative(final ArrayList<Double> data) {
            alpha_rel.clear();
            alpha_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            alpha_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            alpha_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            alpha_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));

            alpha_rel_1.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            alpha_rel_2.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            alpha_rel_3.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            alpha_rel_4.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));
        }

        private void updateBetaRelative(final ArrayList<Double> data) {
            beta_rel.clear();
            beta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            beta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            beta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            beta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));

            beta_rel_1.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            beta_rel_2.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            beta_rel_3.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            beta_rel_4.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));
        }

        private void updateThetaRelative(final ArrayList<Double> data) {
            theta_rel.clear();
            theta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            theta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            theta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            theta_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));

            theta_rel_1.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            theta_rel_2.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            theta_rel_3.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            theta_rel_4.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));
        }

        private void updateGammaRelative(final ArrayList<Double> data) {
            gamma_rel.clear();
            gamma_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            gamma_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            gamma_rel.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            gamma_rel.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));

            gamma_rel_1.add(String.format(
                    "%6.2f", data.get(Eeg.TP9.ordinal())));
            gamma_rel_2.add(String.format(
                    "%6.2f", data.get(Eeg.FP1.ordinal())));
            gamma_rel_3.add(String.format(
                    "%6.2f", data.get(Eeg.FP2.ordinal())));
            gamma_rel_4.add(String.format(
                    "%6.2f", data.get(Eeg.TP10.ordinal())));
        }*/

        private void updateHorseshoe(final ArrayList<Double> data) {
            horseshoe = data;
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
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into JavaScript.
     * @return True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.i(TAG, "Action received: " + action);
        if (action.equals("getMuseList")) {
            String[] museList = getMuseList();
            if (museList != null) {
                JSONArray jsonMuseList = new JSONArray(Arrays.asList(museList));
                callbackContext.success(jsonMuseList);
            } else {
                callbackContext.error("MuseList empty.");
            }
            /*PluginResult result = new PluginResult(PluginResult.Status.OK, "YOUR_success_MESSAGE");
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);*/
        } else if (action.equals("connectToMuse")) {
            connectToMuse(callbackContext);
        } else if (action.equals("disconnectMuse")) {
            disconnectMuse();
            callbackContext.success("Disconnected.");
        } else if (action.equals("getConnectionState")) {
            getConnectionState(callbackContext);
        } else if (action.equals("startRecording")) {
            startRecording(callbackContext);
            toastShort("Started Recording");
            callbackContext.success("Started Recording");
        } else if (action.equals("setWatch")) {
            setWatch(callbackContext);
        } else if (action.equals("stopRecording")) {
            stopRecording();
            toastShort("Stopped Recording");
            callbackContext.success("Stopped Recording");
        } else if (action.equals("getBlink")) {
            if (blink == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + blink.size());
            Log.v(TAG, "First value is " + blink.get(0));
            callbackContext.success(new JSONArray(blink));
        } else if (action.equals("testConnection")) {
            if (horseshoe == null) {
                callbackContext.error("No connection data received.");
            }
            Log.v(TAG, "Connection data is " + horseshoe.toString());
            String status = "Left Ear - ";
            Double value = horseshoe.get(0);
            if (value == 1.0) {
                status += "Good Connection";
            } else if (value < 4.0) {
                status += "Weak Connection";
            } else {
                status += "No Connection";
            }
            status += "\nLeft Forehead - ";
            value = horseshoe.get(1);
            if (value == 1.0) {
                status += "Good Connection";
            } else if (value < 4.0) {
                status += "Weak Connection";
            } else {
                status += "No Connection";
            }
            status += "\nRight Forehead - ";
            value = horseshoe.get(2);
            if (value == 1.0) {
                status += "Good Connection";
            } else if (value < 4.0) {
                status += "Weak Connection";
            } else {
                status += "No Connection";
            }
            status += "\nRight Ear - ";
            value = horseshoe.get(3);
            if (value == 1.0) {
                status += "Good Connection";
            } else if (value < 4.0) {
                status += "Weak Connection";
            } else {
                status += "No Connection";
            }
            toastLong(status);
            callbackContext.success(status);
        }
          /*else if (action.equals("getAccForwardBackward")) {
            if (acc_forward_backward == null) {
                callbackContext.error("No data has been recorded.");
            }
            callbackContext.success(new JSONArray(acc_forward_backward));
        } else if (action.equals("getAccLeftRight")) {
            if (acc_left_right == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + acc_left_right.size());
            Log.v(TAG, "First value is " + acc_left_right.get(0));
            callbackContext.success(new JSONArray(acc_left_right));
        } else if (action.equals("getAccUpDown")) {
            if (acc_up_down == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + acc_up_down.size());
            Log.v(TAG, "First value is " + acc_up_down.get(0));
            callbackContext.success(new JSONArray(acc_up_down));
        } else if (action.equals("getEegLeftEar")) {
            if (eeg_left_ear == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + eeg_left_ear.size());
            Log.v(TAG, "First value is " + eeg_left_ear.get(0));
            callbackContext.success(new JSONArray(eeg_left_ear));
        } else if (action.equals("getEegRightEar")) {
            if (eeg_right_ear == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + eeg_right_ear.size());
            Log.v(TAG, "First value is " + eeg_right_ear.get(0));
            callbackContext.success(new JSONArray(eeg_right_ear));
        } else if (action.equals("getEegLeftForehead")) {
            if (eeg_left_forehead == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + eeg_left_forehead.size());
            Log.v(TAG, "First value is " + eeg_left_forehead.get(0));
            callbackContext.success(new JSONArray(eeg_left_forehead));
        } else if (action.equals("getEegRightForehead")) {
            if (eeg_right_forehead == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + eeg_right_forehead.size());
            Log.v(TAG, "First value is " + eeg_right_forehead.get(0));
            callbackContext.success(new JSONArray(eeg_right_forehead));
        } else if (action.equals("getAlphaRelativeChannel1")) {
            if (alpha_rel_1 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + alpha_rel_1.size());
            Log.v(TAG, "First value is " + alpha_rel_1.get(0));
            callbackContext.success(new JSONArray(alpha_rel_1));
        } else if (action.equals("getAlphaRelativeChannel2")) {
            if (alpha_rel_2 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + alpha_rel_2.size());
            Log.v(TAG, "First value is " + alpha_rel_2.get(0));
            callbackContext.success(new JSONArray(alpha_rel_2));
        } else if (action.equals("getAlphaRelativeChannel3")) {
            if (alpha_rel_3 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + alpha_rel_3.size());
            Log.v(TAG, "First value is " + alpha_rel_3.get(0));
            callbackContext.success(new JSONArray(alpha_rel_3));
        } else if (action.equals("getAlphaRelativeChannel4")) {
            if (alpha_rel_4 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + alpha_rel_4.size());
            Log.v(TAG, "First value is " + alpha_rel_4.get(0));
            callbackContext.success(new JSONArray(alpha_rel_4));
        } else if (action.equals("getBetaRelativeChannel1")) {
            if (beta_rel_1 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + beta_rel_1.size());
            Log.v(TAG, "First value is " + beta_rel_1.get(0));
            callbackContext.success(new JSONArray(beta_rel_1));
        } else if (action.equals("getBetaRelativeChannel2")) {
            if (beta_rel_2 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + beta_rel_2.size());
            Log.v(TAG, "First value is " + beta_rel_2.get(0));
            callbackContext.success(new JSONArray(beta_rel_2));
        } else if (action.equals("getBetaRelativeChannel3")) {
            if (beta_rel_3 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + beta_rel_3.size());
            Log.v(TAG, "First value is " + beta_rel_3.get(0));
            callbackContext.success(new JSONArray(beta_rel_3));
        } else if (action.equals("getBetaRelativeChannel4")) {
            if (beta_rel_4 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + beta_rel_4.size());
            Log.v(TAG, "First value is " + beta_rel_4.get(0));
            callbackContext.success(new JSONArray(beta_rel_4));
        } else if (action.equals("getThetaRelativeChannel1")) {
            if (theta_rel_1 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + theta_rel_1.size());
            Log.v(TAG, "First value is " + theta_rel_1.get(0));
            callbackContext.success(new JSONArray(theta_rel_1));
        } else if (action.equals("getThetaRelativeChannel2")) {
            if (theta_rel_2 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + theta_rel_2.size());
            Log.v(TAG, "First value is " + theta_rel_2.get(0));
            callbackContext.success(new JSONArray(theta_rel_2));
        } else if (action.equals("getThetaRelativeChannel3")) {
            if (theta_rel_3 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + theta_rel_3.size());
            Log.v(TAG, "First value is " + theta_rel_3.get(0));
            callbackContext.success(new JSONArray(theta_rel_3));
        } else if (action.equals("getThetaRelativeChannel4")) {
            if (theta_rel_4 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + theta_rel_4.size());
            Log.v(TAG, "First value is " + theta_rel_4.get(0));
            callbackContext.success(new JSONArray(theta_rel_4));
        } else if (action.equals("getGammaRelativeChannel1")) {
            if (gamma_rel_1 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + gamma_rel_1.size());
            Log.v(TAG, "First value is " + gamma_rel_1.get(0));
            callbackContext.success(new JSONArray(gamma_rel_1));
        } else if (action.equals("getGammaRelativeChannel2")) {
            if (gamma_rel_2 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + gamma_rel_2.size());
            Log.v(TAG, "First value is " + gamma_rel_2.get(0));
            callbackContext.success(new JSONArray(gamma_rel_2));
        } else if (action.equals("getGammaRelativeChannel3")) {
            if (gamma_rel_3 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + gamma_rel_3.size());
            Log.v(TAG, "First value is " + gamma_rel_3.get(0));
            callbackContext.success(new JSONArray(gamma_rel_3));
        } else if (action.equals("getGammaRelativeChannel4")) {
            if (gamma_rel_4 == null) {
                callbackContext.error("No data has been recorded.");
            }
            Log.v(TAG, "Returning array of length " + gamma_rel_4.size());
            Log.v(TAG, "First value is " + gamma_rel_4.get(0));
            callbackContext.success(new JSONArray(gamma_rel_4));
        } */
        /*else if (action.equals("getRecordingData")) {
            JSONObject recordingData = new JSONObject();
            recordingData.put("ACC_LEFT_RIGHT", new JSONArray(acc_left_right));
            recordingData.put("ACC_FORWARD_BACKWARD", new JSONArray(acc_forward_backward));
            recordingData.put("ACC_UP_DOWN", new JSONArray(acc_up_down));

            recordingData.put("EEG_LEFT_EAR", new JSONArray(eeg_left_ear));
            recordingData.put("EEG_RIGHT_EAR", new JSONArray(eeg_right_ear));
            recordingData.put("EEG_LEFT_FOREHEAD", new JSONArray(eeg_left_forehead));
            recordingData.put("EEG_RIGHT_FOREHEAD", new JSONArray(eeg_right_forehead));

            recordingData.put("ALPHA_REL_1", new JSONArray(alpha_rel_1));
            recordingData.put("ALPHA_REL_2", new JSONArray(alpha_rel_2));
            recordingData.put("ALPHA_REL_3", new JSONArray(alpha_rel_3));
            recordingData.put("ALPHA_REL_4", new JSONArray(alpha_rel_4));

            recordingData.put("BETA_REL_1", new JSONArray(beta_rel_1));
            recordingData.put("BETA_REL_2", new JSONArray(beta_rel_2));
            recordingData.put("BETA_REL_3", new JSONArray(beta_rel_3));
            recordingData.put("BETA_REL_4", new JSONArray(beta_rel_4));

            recordingData.put("THETA_REL_1", new JSONArray(theta_rel_1));
            recordingData.put("THETA_REL_2", new JSONArray(theta_rel_2));
            recordingData.put("THETA_REL_3", new JSONArray(theta_rel_3));
            recordingData.put("THETA_REL_4", new JSONArray(theta_rel_4));

            recordingData.put("GAMMA_REL_1", new JSONArray(gamma_rel_1));
            recordingData.put("GAMMA_REL_2", new JSONArray(gamma_rel_2));
            recordingData.put("GAMMA_REL_3", new JSONArray(gamma_rel_3));
            recordingData.put("GAMMA_REL_4", new JSONArray(gamma_rel_4));

            recordingData.put("BLINK", new JSONArray(blink));

            Log.v(TAG, "Returning object with " + recordingData.length() + " keys.");
            callbackContext.error("Not supported any more");
        }*/
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
                    toastShort("ConnectionState: " + state.toString());
                    if (state != ConnectionState.CONNECTED && state != ConnectionState.CONNECTING) {
                        connectionListener.setCallbackContext(callbackContext);
                        final Muse museToConnect = muse;
                        registerDataListeners(museToConnect);
                        Log.i(TAG, "Connecting...");
                        toastShort("Connecting...");
                        try {
                            cordova.getThreadPool().execute(new Runnable() {
                                public void run() {
                                    museToConnect.runAsynchronously();
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        connectedMuse = muse;

                    } else {
                        Log.i(TAG, "Device is already connected or is connecting." + state);
                    }
                    callbackContext.success(state.toString());
                } catch (Exception ex) {
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

    private void getConnectionState(CallbackContext callbackContext) {
        callbackContext.success(pairedMuses.get(0).getConnectionState().toString());
    }

    private void registerDataListeners(Muse muse) {
        muse.setPreset(MusePreset.PRESET_14);
        muse.registerConnectionListener(connectionListener);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.ACCELEROMETER);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.EEG);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.ALPHA_ABSOLUTE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.BETA_ABSOLUTE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.DELTA_ABSOLUTE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.THETA_ABSOLUTE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.GAMMA_ABSOLUTE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.ALPHA_RELATIVE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.BETA_RELATIVE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.DELTA_RELATIVE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.THETA_RELATIVE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.GAMMA_RELATIVE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.HORSESHOE);
        muse.registerDataListener(dataListener,
                MuseDataPacketType.ARTIFACTS);
        muse.enableDataTransmission(true);
    }

    private void disconnectMuse() {
        if (connectedMuse != null) {
            toastShort("Disconnected " + connectedMuse.getMacAddress());
            connectedMuse.disconnect(true);
            connectedMuse = null;
        }
    }

    private void startRecording(CallbackContext callbackContext) {
        resetDataPacketLists();
        recordData = true;
    }

    private void setWatch(CallbackContext callbackContext) {
        dataListener.setCallbackContext(callbackContext);
    }

    private void stopRecording() {
        clearWatch();
        recordData = false;
    }

    private void clearWatch() {
        dataListener.setCallbackContext(null);
    }


    private void resetDataPacketLists() {
        acc = new LinkedList<String>();
        /*acc_forward_backward = new LinkedList<String>();
        acc_up_down = new LinkedList<String>();
        acc_left_right = new LinkedList<String>();*/

        eeg = new LinkedList<String>();
        /*eeg_left_ear = new LinkedList<String>();
        eeg_left_forehead = new LinkedList<String>();
        eeg_right_forehead = new LinkedList<String>();
        eeg_right_ear = new LinkedList<String>();*/

        alpha_abs = new LinkedList<String>();
        beta_abs = new LinkedList<String>();
        delta_abs = new LinkedList<String>();
        theta_abs = new LinkedList<String>();
        gamma_abs = new LinkedList<String>();

        alpha_rel = new LinkedList<String>();
        beta_rel = new LinkedList<String>();
        delta_rel = new LinkedList<String>();
        theta_rel = new LinkedList<String>();
        gamma_rel = new LinkedList<String>();

        /*alpha_rel_1 = new LinkedList<String>();
        alpha_rel_2 = new LinkedList<String>();
        alpha_rel_3 = new LinkedList<String>();
        alpha_rel_4 = new LinkedList<String>();

        beta_rel_1 = new LinkedList<String>();
        beta_rel_2 = new LinkedList<String>();
        beta_rel_3 = new LinkedList<String>();
        beta_rel_4 = new LinkedList<String>();

        theta_rel_1 = new LinkedList<String>();
        theta_rel_2 = new LinkedList<String>();
        theta_rel_3 = new LinkedList<String>();
        theta_rel_4 = new LinkedList<String>();

        gamma_rel_1 = new LinkedList<String>();
        gamma_rel_2 = new LinkedList<String>();
        gamma_rel_3 = new LinkedList<String>();
        gamma_rel_4 = new LinkedList<String>();*/

        blink = new LinkedList<String>();
    }

    private void toastLong(String message) {
        Toast.makeText(cordova.getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void toastShort(String message) {
        Toast.makeText(cordova.getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
