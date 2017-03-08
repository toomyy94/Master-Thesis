package pt.ptinovacao.arqospocket.service.tasks;

import android.content.Context;
import android.content.IntentFilter;
import android.service.wallpaper.WallpaperService;
import android.util.Log;

import java.util.Date;

import pt.ptinovacao.arqospocket.MyApplication;
import pt.ptinovacao.arqospocket.service.audio.AudioRecorderHelper;
import pt.ptinovacao.arqospocket.service.enums.ECallType;
import pt.ptinovacao.arqospocket.service.enums.EEvent;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorker;
import pt.ptinovacao.arqospocket.service.interfaces.IService;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.AnswerVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonresult.AnswerVoiceCallTaskJsonResult;
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
import static pt.ptinovacao.arqospocket.service.utils.Constants.FILENAMES_DATE_FORMAT;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RESULTS_FILE_NAME_SEPARATOR;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_NOK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_NOT_REGISTERED_IN_NETWORK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_BLOCKED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_NOT_INSERTED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_PIN_REQUIRED;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_SIM_PUK_REQUIRED;


public class AnswerVoiceCall implements VoiceCallReceiver.VoiceCallReceiverListener {
    private static final String TAG = AnswerVoiceCall.class.getSimpleName();



    private Context context;

    private VoiceCallReceiver voiceCallReceiver;
    private AnswerVoiceCallTaskStruct answerVoiceCallTaskStruct;
    private IRunTaskWorker callbackRef;
    private GPSInformation gpsInformation;
    private AudioRecorderHelper audioRecorder;
    private MobileBasicInfoStruct mobileBasicInfoStruct;
    private Mobile mobile;
    private Date startDate, endDate, callAnswerTime;
    private String callNumber;
    private long callSetupTime;
    private long timeWaitingForRinging;
    private boolean callInitiated = false;
    private boolean callRinging = false;
    private boolean callAnswered = false;
    private String recordedFilename;

    private MyApplication MyApplicationRef;
    private IService iService;


    public AnswerVoiceCall(Context context, AnswerVoiceCallTaskStruct answerVoiceCallTaskStruct, GPSInformation gpsInformation, Mobile mobileRef, IRunTaskWorker callbackRef) {
        this.context = context;
        this.answerVoiceCallTaskStruct = answerVoiceCallTaskStruct;
        this.callbackRef = callbackRef;
        this.gpsInformation = gpsInformation;
        this.mobile = mobileRef;

        MyApplicationRef = (MyApplication) MyApplication.getContext();
        iService  = MyApplicationRef.getEngineServiceRef();
    }

    public boolean answerCall(){
        Log.i(TAG, "AnswerCall");
        startDate = new Date();

        if (isSimAndNetworkReady()) {

            Log.i(TAG, "Pre-conditions ok! Let's start the receiver!");

            //TODO ver a questao de ter varios listeners
            startCallReceiver();
        }

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
                new AnswerVoiceCallTaskJsonResult(
                        answerVoiceCallTaskStruct.get_task_id(),
                        answerVoiceCallTaskStruct.get_task_name(),
                        answerVoiceCallTaskStruct.get_macro_id(),
                        answerVoiceCallTaskStruct.get_task_number(),
                        mobile.getSimIccid(),
                        mobile.getNetworkInfo(),
                        gpsInformation.getFormattedLocation(),
                        status,
                        startDate,
                        endDate,
                        callNumber,
                        String.valueOf(timeWaitingForRinging),
                        recordedFilename,
                        new MyLocation(gpsInformation),
                        ConnectionInfo.get_active_connection(context)));
    }

    @Override
    public void onCallInitiated(ECallType callType) {
        Log.i(TAG, "onCallInitiated :: callType: " + callType);
        callSetupTime = System.currentTimeMillis() - startDate.getTime();
        callInitiated = true;
        iService.generate_radiolog(EEvent.CALL_ESTABLISHED, "connect time: "+callSetupTime);
    }

    @Override
    public void onCallRinging(ECallType callType, String number) {
        Log.i(TAG, "onCallRinging :: callType: " + callType + " number: " +number);
        callRinging = true;
        callNumber = number;
        if (callType == ECallType.INCOMING) {
            timeWaitingForRinging = System.currentTimeMillis() - startDate.getTime();

            int ringingTime = 0;
            try {
                ringingTime = Integer.parseInt(answerVoiceCallTaskStruct.getRingingTime());
            } catch (Exception e) {

            }

            final int delay = ringingTime * 1000;
            //TODO ver se da para nao usar thread com sleep
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        if (delay > 0) {
                            sleep(delay);
                        }
                        CallUtils.answerCall(context);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }

    }

    @Override
    public void onCallAnswered(ECallType callType) {
        Log.i(TAG, "onCallAnswered :: callType: " + callType);
        callAnswered = true;
        callAnswerTime = new Date();
        int callDurationSeconds = 0;
        int audioRecordingTime = 0;
        int timeout = 0;
        int audioType = 0;

        callDurationSeconds = answerVoiceCallTaskStruct.getCallDurationSeconds();
        audioRecordingTime = answerVoiceCallTaskStruct.getAudioRecordingTime();
        audioType = answerVoiceCallTaskStruct.getAudioType();
        try {

            timeout = Utils.parseIntParameter(answerVoiceCallTaskStruct.get_timeout());

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

        if (audioRecordingTime != 0 && audioRecordingTime >= -1){
            recordedFilename = answerVoiceCallTaskStruct.getAudioRecordingFileName()
                    + RESULTS_FILE_NAME_SEPARATOR
                    + System.currentTimeMillis();

            audioRecorder = new AudioRecorderHelper(
                    context,
                    answerVoiceCallTaskStruct.getAudioRecordingFileName(),
                    audioRecordingTime,
                    timeout,
                    audioType,
                    null);
            audioRecorder.startRecording();
        }

    }

    @Override
    public void onCallDisconnected(ECallType callType) {
        Log.i(TAG, "onCallDisconnected :: callType: " + callType);
        //TODO ver onde chamar isto
        if (callType != null) {
            if (audioRecorder != null && audioRecorder.isRecording()) {
                audioRecorder.stopRecording();
            }
            stopCallReceiver();

            if (callAnswered){
                publishResult(STATUS_OK);
            } else if (callRinging){
                //TODO ver como distinguir o reject
                //TODO ver erros para estas situacoes
                publishResult(STATUS_NOK);
            } else if (callInitiated) {
                //TODO ver erros para estas situacoes
                publishResult(STATUS_NOK);
            } else {
                publishResult(STATUS_NOK);
            }
        }
    }


}
