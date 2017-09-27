package pt.ptinovacao.arqospocket.core.serialization.entities.results.type2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.telephony.TelephonyManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import pt.ptinovacao.arqospocket.core.CoreApplication;
import pt.ptinovacao.arqospocket.core.cdrs.CDRUtils;
import pt.ptinovacao.arqospocket.core.cdrs.CDRsManager;
import pt.ptinovacao.arqospocket.persistence.ExecutingEventDao;

/**
 * Created by pedro on 07/06/2017.
 */

public class PhoneStateReceiver extends BroadcastReceiver {

    private final static Logger LOGGER = LoggerFactory.getLogger(PhoneStateReceiver.class);

    private String current_state, previus_state;
    private Context context;
    private SharedPreferences sp, sp1;
    private SharedPreferences.Editor spEditor, spEditor1;

    private static boolean isIncoming = false;
    private String state, incoming_number;
    private Date ringing, active, end, callDayTime;
    private String phNumber, callType, callDate,  callDuration, dir;

    @Override
    public void onReceive(Context context, Intent intent) {
        final CoreApplication coreApplication = ((CoreApplication) context.getApplicationContext());
        this.context = context;
        TelephonyStateManager telephonyStateManager = TelephonyStateManager.getInstance(coreApplication);
        ExecutingEventDao executingEventDao = coreApplication.getDatabaseHelper().createExecutingEventDao();

        if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
            state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            incoming_number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            LOGGER.debug("State : "+state+", incoming_number : " + incoming_number);

            previus_state = getCallState(context);
            current_state = "IDLE";
            if(incoming_number!=null){
                updateIncomingNumber(incoming_number,context);
            }else {
                incoming_number = getIncomingNumber(context);
            }

            if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
                current_state="IDLE";
                telephonyStateManager.setCallStatus(TelephonyStateManager.CallStatus.IDLE);
                telephonyStateManager.notificationState();

                if (isIncoming == true) CDRsManager.getInstance(coreApplication).generateCDRReceivedCall(incoming_number);
                else CDRsManager.getInstance(coreApplication).generateCDRMakeCall(incoming_number);

                isIncoming = false;
                updateIncomingNumber("no_number",context);
            } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                if((previus_state.equals("IDLE")) || (previus_state.equals("FIRST_CALL_RINGING"))){
                    current_state ="FIRST_CALL_RINGING";
                }
                if((previus_state.equals("OFFHOOK"))||(previus_state.equals("SECOND_CALL_RINGING"))){
                    current_state = "SECOND_CALL_RINGING";
                    CDRUtils.getInstance(coreApplication).tempInitCallCdr.setSecondCall(1);
                }
                telephonyStateManager.setCallStatus(TelephonyStateManager.CallStatus.WAITING);
                telephonyStateManager.setCallInOrOut(TelephonyStateManager.CallInOrOut.Callee);
                telephonyStateManager.setIncomingNumber(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
                isIncoming = true;
                CDRUtils.getInstance(coreApplication).fillTempInitCallCdr();
                telephonyStateManager.notificationState();

            } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
                current_state = "OFFHOOK";
                if (isIncoming) {
                    CDRUtils.getInstance(coreApplication).fillTempInitCallConversation();
                    telephonyStateManager.setCallInOrOut(TelephonyStateManager.CallInOrOut.Callee);
                } else {
                    telephonyStateManager.setCallInOrOut(TelephonyStateManager.CallInOrOut.Caller);
                    CDRUtils.getInstance(coreApplication).fillTempInitCallCdr();
                }
                telephonyStateManager.setCallStatus(TelephonyStateManager.CallStatus.ACTIVE);
                telephonyStateManager.notificationState();
            }
        }

        if(!current_state.equals(previus_state)){
            updateCallState(current_state,context);

        }
    }

    private void getCallDetails(CoreApplication coreApplication) {

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = coreApplication.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Details :");
//        while (managedCursor.moveToNext()) {
        managedCursor.moveToNext();
            phNumber = managedCursor.getString(number);
            callType = managedCursor.getString(type);
            callDate = managedCursor.getString(date);
            callDayTime = new Date(Long.valueOf(callDate));
            callDuration = managedCursor.getString(duration);
            dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                    + dir + " \nCall Date:--- " + callDayTime
                    + " \nCall duration in sec :--- " + callDuration);
            sb.append("\n----------------------------------");
//        }
        managedCursor.close();
    }

    public void updateCallState(String state,Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = sp.edit();
        spEditor.putString("call_state", state);
        spEditor.commit();
    }
    public void updateIncomingNumber(String inc_num,Context context){
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = sp.edit();
        spEditor.putString("inc_num", inc_num);
        spEditor.commit();
    }
    public String getCallState(Context context){
        sp1 = PreferenceManager.getDefaultSharedPreferences(context);
        String st = sp1.getString("call_state", "IDLE");
        return st;
    }
    public String getIncomingNumber(Context context){
        sp1 = PreferenceManager.getDefaultSharedPreferences(context);
        String st =sp1.getString("inc_num", "no_num");
        return st;
    }
}
