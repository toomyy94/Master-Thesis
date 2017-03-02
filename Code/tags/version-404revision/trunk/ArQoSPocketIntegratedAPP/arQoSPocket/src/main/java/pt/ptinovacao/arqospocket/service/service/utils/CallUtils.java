package pt.ptinovacao.arqospocket.service.service.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;

import com.android.internal.telephony.ITelephony;


import java.io.IOException;
import java.lang.reflect.Method;

import pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver;

/**
 * Created by x00881 on 27-12-2016.
 */

public class CallUtils {
    private static final String TAG = VoiceCallReceiver.class.getSimpleName();

    private CallUtils() {
    }

    public static void answerCall(Context context){
        answerCallAIDL(context);
    }

    public static void endCall(Context context){
        endCallAIDL(context);
    }


    private static void answerCallAIDL(Context context){
        Log.d(TAG, "answerCallAIDL");
        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
            //telephonyService.silenceRinger();
            telephonyService.answerRingingCall();
        }catch (Exception e) {
            Log.d(TAG, "Exception: ");
            e.printStackTrace();
            sendKeyToCall(context);
        }
    }

    private static void endCallAIDL(Context context){
        Log.d(TAG, "endCallAIDL");
        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            Class clazz = Class.forName(telephonyManager.getClass().getName());
            Method method = clazz.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
            telephonyService.endCall();
        }catch (Exception e) {
            Log.d(TAG, "Exception: ");
            e.printStackTrace();
            sendKeyToCall(context);
        }
    }

    private static void sendKeyToCall(Context context) {
        try {
            Log.d(TAG, "sendKeyToCall input keyevent");
            Runtime.getRuntime().exec("input keyevent " +
                    Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
        } catch (IOException e) {
            Log.d(TAG, "input MEDIA BUTTON");

            // Runtime.exec(String) had an I/O problem, try to fall back
            String enforcedPerm = "android.permission.CALL_PRIVILEGED";
            Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                            KeyEvent.KEYCODE_HEADSETHOOK));
            Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                            KeyEvent.KEYCODE_HEADSETHOOK));

            context.sendOrderedBroadcast(btnDown, enforcedPerm);
            context.sendOrderedBroadcast(btnUp, enforcedPerm);
        }
    }

    public static String getCallDurationFromLog(final Context context, String number, int callType) {
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

            cursor = context.getContentResolver().query(callog, new String[] {CallLog.Calls.DURATION},
                    queryString, null, CallLog.Calls.DATE);
            final int durationCol = cursor
                    .getColumnIndex(CallLog.Calls.DURATION);

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

        return output;
    }
}
