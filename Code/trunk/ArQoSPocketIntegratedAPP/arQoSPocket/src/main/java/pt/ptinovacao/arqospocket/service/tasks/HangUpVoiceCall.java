package pt.ptinovacao.arqospocket.service.tasks;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.CallLog;
import android.util.Log;

import java.util.Date;

import pt.ptinovacao.arqospocket.service.enums.ECallType;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorker;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.AnswerVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HangUpVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonresult.AnswerVoiceCallTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.HangUpCallTaskJsonResult;
import pt.ptinovacao.arqospocket.service.service.GPSInformation;
import pt.ptinovacao.arqospocket.service.service.utils.CallUtils;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver;
import pt.ptinovacao.arqospocket.service.utils.ConnectionInfo;
import pt.ptinovacao.arqospocket.service.utils.Utils;

import static android.telephony.TelephonyManager.SIM_STATE_ABSENT;
import static android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED;
import static android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED;
import static android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED;
import static android.telephony.TelephonyManager.SIM_STATE_READY;
import static android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
import static pt.ptinovacao.arqospocket.service.service.utils.CallUtils.endCall;
import static pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver.ACTION_PHONE_STATE;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_CALL_NOT_ACTIVE;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_INVALID_PARAMETERS;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_NOK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;


public class HangUpVoiceCall implements VoiceCallReceiver.VoiceCallReceiverListener {
    private static final String TAG = HangUpVoiceCall.class.getSimpleName();

    private Context context;

    private VoiceCallReceiver voiceCallReceiver;
    private HangUpVoiceCallTaskStruct hangUpVoiceCallTaskStruct;
    private IRunTaskWorker callbackRef;
    private GPSInformation gpsInformation;
    private MobileBasicInfoStruct mobileBasicInfoStruct;
    private Mobile mobile;
    private Date startDate, endDate, callAnswerTime;
    private String callNumber;
    private long callSetupTime, callDuration;
    private long timeWaitingForRinging;
    private boolean callInitiated = false;
    private boolean callRinging = false;
    private boolean callAnswered = false;

    public HangUpVoiceCall(Context context, HangUpVoiceCallTaskStruct hangUpVoiceCallTaskStruct, GPSInformation gpsInformation, Mobile mobileRef, IRunTaskWorker callbackRef) {
        this.context = context;
        this.hangUpVoiceCallTaskStruct = hangUpVoiceCallTaskStruct;
        this.callbackRef = callbackRef;
        this.gpsInformation = gpsInformation;
        this.mobile = mobileRef;
    }

    public boolean hangupCall(){
        Log.i(TAG, "hangupCall");
        startDate = new Date();

        //validate parameters
        if (hangUpVoiceCallTaskStruct.getCallsToBeTerminated() == 2){
            publishResult(STATUS_NOK + STATUS_INVALID_PARAMETERS);
            return true;
        }

        if(!mobile.isAnyCallActive()){
            Log.i(TAG, "No Call(s) active!");
            publishResult(STATUS_NOK + STATUS_CALL_NOT_ACTIVE);
            return true;
        }
        Log.i(TAG, "Starting the receiver!");
        startCallReceiver();
        Log.i(TAG, "Ending call!");
        endCall(context);


        return true;
    }



    private void startCallReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PHONE_STATE);
        //intentFilter.addAction(ACTION_OUTGOING_CALL);

        voiceCallReceiver = new VoiceCallReceiver(context, this);

        context.registerReceiver(voiceCallReceiver, intentFilter);
    }


    private void stopCallReceiver() {
        context.unregisterReceiver(voiceCallReceiver);
    }

    private void publishResult(String status){
        Log.i(TAG, "publishResult :: status: " + status);
        endDate = new Date();

        callbackRef.async_result(
                new HangUpCallTaskJsonResult(
                        hangUpVoiceCallTaskStruct.get_task_id(),
                        hangUpVoiceCallTaskStruct.get_task_name(),
                        hangUpVoiceCallTaskStruct.get_macro_id(),
                        hangUpVoiceCallTaskStruct.get_task_number(),
                        mobile.getSimIccid(),
                        mobile.getNetworkInfo(),
                        gpsInformation.getFormattedLocation(),
                        status,
                        startDate,
                        endDate,
                        String.valueOf(callDuration * 1000),
                        new MyLocation(gpsInformation),
                        ConnectionInfo.get_active_connection(context)));
    }

    @Override
    public void onCallInitiated(ECallType callType) {
        Log.i(TAG, "onCallInitiated :: callType: " + callType);
        callSetupTime = System.currentTimeMillis() - startDate.getTime();
        callInitiated = true;
    }

    @Override
    public void onCallRinging(ECallType callType, String number) {
        Log.i(TAG, "onCallRinging :: callType: " + callType + " number: " +number);
        callRinging = true;
        callNumber = number;
    }

    @Override
    public void onCallAnswered(ECallType callType) {
        Log.i(TAG, "onCallAnswered :: callType: " + callType);
        callAnswered = true;
        callAnswerTime = new Date();
    }

    @Override
    public void onCallDisconnected(ECallType callType) {
        Log.i(TAG, "onCallDisconnected :: callType: " + callType);
        //TODO ver onde chamar isto
        stopCallReceiver();

        int cType = (callType != null ?
                callType == ECallType.INCOMING
                        ? CallLog.Calls.INCOMING_TYPE
                        : CallLog.Calls.OUTGOING_TYPE
                : 0);
        Log.i(TAG, "onCallDisconnected :: cType: " + cType);
        callDuration = Utils.parseIntParameter(CallUtils.getCallDurationFromLog(context, callNumber, cType));

        Log.i(TAG, "onCallDisconnected :: callDuration: " + callDuration);

        publishResult(STATUS_OK);
    }
}
