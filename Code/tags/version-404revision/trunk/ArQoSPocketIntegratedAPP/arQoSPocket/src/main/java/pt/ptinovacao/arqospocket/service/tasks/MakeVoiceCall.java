package pt.ptinovacao.arqospocket.service.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pt.ptinovacao.arqospocket.service.audio.AudioRecorderHelper;
import pt.ptinovacao.arqospocket.service.enums.ECallType;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorker;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.MakeVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonresult.MakeVoiceCallTaskJsonResult;
import pt.ptinovacao.arqospocket.service.service.GPSInformation;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver;
import pt.ptinovacao.arqospocket.service.utils.ConnectionInfo;
import pt.ptinovacao.arqospocket.service.utils.Utils;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.telephony.TelephonyManager.SIM_STATE_ABSENT;
import static android.telephony.TelephonyManager.SIM_STATE_NETWORK_LOCKED;
import static android.telephony.TelephonyManager.SIM_STATE_PIN_REQUIRED;
import static android.telephony.TelephonyManager.SIM_STATE_PUK_REQUIRED;
import static android.telephony.TelephonyManager.SIM_STATE_READY;
import static android.telephony.TelephonyManager.SIM_STATE_UNKNOWN;
import static pt.ptinovacao.arqospocket.service.service.utils.CallUtils.endCall;
import static pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver.ACTION_OUTGOING_CALL;
import static pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver.ACTION_PHONE_STATE;
import static pt.ptinovacao.arqospocket.service.utils.Constants.FILENAMES_DATE_FORMAT;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RESULTS_FILE_NAME_SEPARATOR;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_CALL_ALREADY_ACTIVE;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_CALL_DISCONNECTED_OR_REJECTED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_CALL_NOT_ANSWERED_OR_REJECTED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_INVALID_PARAMETERS;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_INVALID_PHONE_NUMBER;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_NOK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_NOT_REGISTERED_IN_NETWORK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_BLOCKED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_NOT_INSERTED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_PIN_REQUIRED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_PUK_REQUIRED;


public class MakeVoiceCall implements VoiceCallReceiver.VoiceCallReceiverListener {
    private static final String TAG = MakeVoiceCall.class.getSimpleName();

    private Context context;

    private VoiceCallReceiver voiceCallReceiver;
    private MakeVoiceCallTaskStruct makeVoiceCallTaskStruct;
    private IRunTaskWorker callbackRef;
    private GPSInformation gpsInformation;
    private AudioRecorderHelper audioRecorder;
    private MobileBasicInfoStruct mobileBasicInfoStruct;
    private Mobile mobile;
    private Date startDate, endDate, callAnswerTime;
    private long callSetupTime;
    private Uri destinationNumberUri;
    private boolean callInitiated = false;
    private boolean callRinging = false;
    private boolean callAnswered = false;
    private boolean resultPublished = false;
    private String recordedFilename;

    public MakeVoiceCall(Context context, MakeVoiceCallTaskStruct makeVoiceCallTaskStruct, GPSInformation gpsInformation, Mobile mobileRef, IRunTaskWorker callbackRef) {
        this.context = context;
        this.makeVoiceCallTaskStruct = makeVoiceCallTaskStruct;
        this.callbackRef = callbackRef;
        this.gpsInformation = gpsInformation;
        this.mobile = mobileRef;
    }

    public boolean makeCall(){
        Log.i(TAG, "makeCall");
        startDate = new Date();

        //validate parameters
        if (makeVoiceCallTaskStruct.getEnable3PartyConference() != 0
                || makeVoiceCallTaskStruct.getEstablishedCallDetection() != 2){
            publishResult(STATUS_NOK + STATUS_INVALID_PARAMETERS);
            return true;
        }

        if (isSimAndNetworkReady()
                && isPhoneNumberValid(makeVoiceCallTaskStruct.getDestinationNumber())) {

            if(mobile.isAnyCallActive()){
                Log.i(TAG, "Call(s) already active!");
                publishResult(STATUS_NOK + STATUS_CALL_ALREADY_ACTIVE);
                return true;
            }

            Log.i(TAG, "Pre-conditions ok! Let's make the call!");

            startCallReceiver();

            Intent makeCallIntent = new Intent(Intent.ACTION_CALL)
                    .addFlags(FLAG_ACTIVITY_NEW_TASK)
                    .setData(destinationNumberUri);

            context.startActivity(makeCallIntent);

        }

        return true;
    }

    private boolean isPhoneNumberValid(String phoneNumber){
        Log.i(TAG, "isPhoneNumberValid :: phoneNumber: " + phoneNumber);
        final String phoneNumberPattern = "^(?:(?:00|\\+)\\d{1,3})?(?:\\d{5,9})$";
        boolean valid;
        try{
            Pattern pattern = Pattern.compile(phoneNumberPattern);
            Matcher matcher = pattern.matcher(phoneNumber);
            valid = matcher.matches();
            if (valid){
                destinationNumberUri = Uri.parse("tel:" + phoneNumber);
            }
        } catch (Exception e) {
            valid = false;
        }

        if (!valid){
            Log.i(TAG, "isPhoneNumberValid :: false");
            publishResult(STATUS_NOK + STATUS_INVALID_PHONE_NUMBER);
            return false;
        }

        Log.i(TAG, "isPhoneNumberValid :: true");
        return true;

    }

    private boolean isSimAndNetworkReady(){
        Log.i(TAG, "isSimAndNetworkReady");

        String status = STATUS_NOK;

        int simState = mobile.getSimState();
        Log.i(TAG, "isSimAndNetworkReady :: simState: " + simState);

        if (simState != SIM_STATE_READY){
            Log.i(TAG, "isSimAndNetworkReady :: false : sim not ready");
            switch (simState) {
                case SIM_STATE_ABSENT:
                    status += STATUS_SIM_NOT_INSERTED;
                    break;
                case SIM_STATE_PIN_REQUIRED:
                    status += STATUS_SIM_PIN_REQUIRED;
                    break;
                case SIM_STATE_PUK_REQUIRED:
                    status += STATUS_SIM_PUK_REQUIRED;
                    break;
                case SIM_STATE_NETWORK_LOCKED:
                    status += STATUS_SIM_BLOCKED;
                    break;
                case SIM_STATE_UNKNOWN:
                    //TODO arranjar mensagem para este
                    status = STATUS_NOK;
                    break;
            }
            publishResult(status);
            return false;
        }

        if (!mobile.isMobileAvailable()){
            Log.i(TAG, "isSimAndNetworkReady :: false :: Not registered in network");
            status += STATUS_NOT_REGISTERED_IN_NETWORK;
            publishResult(status);
            return false;
        }

        Log.i(TAG, "isSimAndNetworkReady :: true");
        return true;

    }

    private void startCallReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_PHONE_STATE);
        intentFilter.addAction(ACTION_OUTGOING_CALL);

        voiceCallReceiver = new VoiceCallReceiver(context, this);

        context.registerReceiver(voiceCallReceiver, intentFilter);
    }


    private void stopCallReceiver() {
        context.unregisterReceiver(voiceCallReceiver);
    }

    private void publishResult(String status){
        Log.i(TAG, "publishResult :: status: " + status);
        resultPublished = true;
        endDate = new Date();
        callbackRef.async_result(
                new MakeVoiceCallTaskJsonResult(
                        makeVoiceCallTaskStruct.get_task_id(),
                        makeVoiceCallTaskStruct.get_task_name(),
                        makeVoiceCallTaskStruct.get_macro_id(),
                        makeVoiceCallTaskStruct.get_task_number(),
                        mobile.getSimIccid(),
                        mobile.getNetworkInfo(),
                        gpsInformation.getFormattedLocation(),
                        status,
                        startDate,
                        endDate,
                        makeVoiceCallTaskStruct.getDestinationNumber(),
                        String.valueOf(callSetupTime),
                        recordedFilename,
                        new MyLocation(gpsInformation),
                        ConnectionInfo.get_active_connection(context)));
    }

    @Override
    public void onCallInitiated(ECallType callType) {
        Log.i(TAG, "onCallInitiated :: callType: " + callType);
        callInitiated = true;
    }

    @Override
    public void onCallRinging(ECallType callType, String number) {
        Log.i(TAG, "onCallRinging :: callType: " + callType);
        callRinging = true;
    }

    @Override
    public void onCallAnswered(ECallType callType) {
        Log.i(TAG, "onCallAnswered :: callType: " + callType);
        callAnswered = true;
        callAnswerTime = new Date();
        callSetupTime = System.currentTimeMillis() - startDate.getTime();

        int callDurationSeconds = makeVoiceCallTaskStruct.getCallDurationSeconds();
        int audioRecordingTime = makeVoiceCallTaskStruct.getAudioRecordingTime();
        int timeout = 0;

        try {
            timeout = Utils.parseIntParameter(makeVoiceCallTaskStruct.get_timeout());
        } catch (Exception e) {

        }

        if (callDurationSeconds > 0){
            final int delay  = callDurationSeconds * 1000;

            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(delay);
                        endCall(context);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }

        //todo handle audio type gracefully
        if (audioRecordingTime != 0 && audioRecordingTime >= -1){
            recordedFilename = makeVoiceCallTaskStruct.getAudioRecordingFileName()
                    + RESULTS_FILE_NAME_SEPARATOR
                    + System.currentTimeMillis();

            audioRecorder = new AudioRecorderHelper(
                    context,
                    recordedFilename,
                    audioRecordingTime,
                    timeout,
                    makeVoiceCallTaskStruct.getAudioType(),
                    null);
            audioRecorder.startRecording();
        }
        publishResult(makeVoiceCallTaskStruct.shouldCallBeAnswered() ? STATUS_OK : STATUS_NOK);

    }

    @Override
    public void onCallDisconnected(ECallType callType) {
        Log.i(TAG, "onCallDisconnected :: callType: " + callType);
        if (callType != null) {
            if (audioRecorder != null && audioRecorder.isRecording()) {
                audioRecorder.stopRecording();
            }
            stopCallReceiver();

            Log.d(TAG, "callAnswered:" + callAnswered + " shouldCallBeAnswered(): " + makeVoiceCallTaskStruct.shouldCallBeAnswered());

            if (!resultPublished) {
                if (callRinging) {
                    publishResult((makeVoiceCallTaskStruct.shouldCallBeAnswered() ? STATUS_NOK : STATUS_OK)
                            + STATUS_CALL_NOT_ANSWERED_OR_REJECTED);
                } else if (callInitiated) {
                    publishResult((makeVoiceCallTaskStruct.shouldCallBeAnswered() ? STATUS_NOK : STATUS_OK)
                            + STATUS_CALL_DISCONNECTED_OR_REJECTED);
                } else {
                    publishResult(STATUS_NOK);
                }
            }
        }
    }


}
