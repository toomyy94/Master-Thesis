package pt.ptinovacao.arqospocket.core.voicecall;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.lang.reflect.Method;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.serialization.entities.results.type2.TelephonyStateManager;
import pt.ptinovacao.arqospocket.core.utils.NumberUtils;
import pt.ptinovacao.arqospocket.core.utils.SystemUtils;

/**
 * Created by pedro on 13/06/2017.
 */

public class VoiceCallManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceCallManager.class);

    private static VoiceCallManager voiceCallManager;

    private CoreApplication coreApplication;

    public static VoiceCallManager getInstance(CoreApplication coreApplication) {
        if (voiceCallManager == null) {
            voiceCallManager = new VoiceCallManager(coreApplication);
        }
        return voiceCallManager;
    }

    public VoiceCallManager(CoreApplication coreApplication) {
        this.coreApplication = coreApplication;
    }

    public boolean makeCallIntent(String destinationNumber) {


        Uri phoneNumberValid = NumberUtils.isPhoneNumberValid(destinationNumber);

        if (phoneNumberValid == null) {
            return false;
        }

        Intent makeCallIntent = new Intent(Intent.ACTION_CALL).addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                .setData(phoneNumberValid);

        coreApplication.startActivity(makeCallIntent);

        return true;
    }

    public boolean acceptVoiceCall() {

        try {
            Thread.sleep(3500);

            String cmd = StringUtils.EMPTY;
            Process process = SystemUtils.requestPermission();
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            KeyguardManager myKM = (KeyguardManager) coreApplication.getSystemService(Context.KEYGUARD_SERVICE);
            if (myKM.inKeyguardRestrictedInputMode()) {
                cmd = "/system/bin/input swipe 100 1650 600 1650\n";
            } else {
                cmd = "/system/bin/input tap 100 400\n";
            }
            os.writeBytes(cmd);
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();
            LOGGER.debug("Accept with success");

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean hangUpVoiceCall() {

        try {
            Thread.sleep(1000);

            String cmd = StringUtils.EMPTY;
            Process process = SystemUtils.requestPermission();
            DataOutputStream os = new DataOutputStream(process.getOutputStream());

            if (!openCloseNotificationBar()) {
                return false;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            cmd = "/system/bin/input tap 927 432\n";

            os.writeBytes(cmd);
            os.writeBytes("exit\n");
            os.flush();
            os.close();
            process.waitFor();

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            coreApplication.sendBroadcast(it);
            LOGGER.debug("hang up with success");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            e.printStackTrace();
            return false;
        }

        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(coreApplication);
        return telephonyStateManager.getCallStatus() == TelephonyStateManager.CallStatus.IDLE;
    }

    private boolean openCloseNotificationBar() {
        try {
            Object o = coreApplication.getSystemService("statusbar");
            Class<?> statusBarManager = Class.forName("android.app.StatusBarManager");
            Method method;
            if (Build.VERSION.SDK_INT >= 17) {
                method = statusBarManager.getMethod("expandNotificationsPanel");
            } else {
                method = statusBarManager.getMethod("expand");
            }
            method.invoke(o);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean sendKeyToCall() {
      /*  try {
            LOGGER.debug("sendKeyToCall input keyevent");
            Runtime.getRuntime().exec("input keyevent " + Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));

        } catch (IOException e) {
            LOGGER.debug("input MEDIA BUTTON");

            // Runtime.exec(String) had an I/O problem, try to fall back
            String enforcedPerm = "android.permission.CALL_PRIVILEGED";
            Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON)
                    .putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_HEADSETHOOK));
            Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON)
                    .putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));

            coreApplication.sendOrderedBroadcast(btnDown, enforcedPerm);
            coreApplication.sendOrderedBroadcast(btnUp, enforcedPerm);
        }*/
        return true;
    }

    public String getCallDurationFromLog(String number, int callType) {
        String output = null;

        final Uri callog = CallLog.Calls.CONTENT_URI;
        Cursor cursor = null;

        try {
            // Query all the columns of the records that matches "type=callType"
            // and number = number
            // (outgoing) and orders the results by "date"
            String queryString = null;

            if (callType > 0) {
                queryString = CallLog.Calls.TYPE + "=" + callType;
            }

            if (number != null) {
                queryString += (callType > 0 ? " AND " : "") + CallLog.Calls.NUMBER + "=" + number;
            }

            cursor = coreApplication.getContentResolver()
                    .query(callog, new String[] { CallLog.Calls.DURATION }, queryString, null, CallLog.Calls.DATE);
            final int durationCol = cursor.getColumnIndex(CallLog.Calls.DURATION);

            // Retrieve only the last record to get the last outgoing call
            if (cursor.moveToLast()) {
                // Retrieve only the duration column
                output = cursor.getString(durationCol);
            }
        } finally {
            // Close the resources
            if (cursor != null) {
                cursor.close();
            }
        }
        LOGGER.debug(output);
//        radiologsManager.generateRadiolog(EEvent.CALL_END, "duration: "+output);

        return output;
    }
}
