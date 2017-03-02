package pt.ptinovacao.arqospocket.service.tasks;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Date;

import pt.ptinovacao.arqospocket.service.audio.AudioRecorderHelper;
import pt.ptinovacao.arqospocket.service.enums.ECallType;
import pt.ptinovacao.arqospocket.service.interfaces.IRunTaskWorker;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.HangUpVoiceCallTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonparser.taskparser.RecordAudioTaskStruct;
import pt.ptinovacao.arqospocket.service.jsonresult.HangUpCallTaskJsonResult;
import pt.ptinovacao.arqospocket.service.jsonresult.RecordAudioTaskJsonResult;
import pt.ptinovacao.arqospocket.service.service.GPSInformation;
import pt.ptinovacao.arqospocket.service.structs.MyLocation;
import pt.ptinovacao.arqospocket.service.tasks.structs.MobileBasicInfoStruct;
import pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver;
import pt.ptinovacao.arqospocket.service.utils.ConnectionInfo;
import pt.ptinovacao.arqospocket.service.utils.Utils;

import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderHelper.RECORDING_STATUS_OK;
import static pt.ptinovacao.arqospocket.service.audio.AudioRecorderHelper.RECORDING_STATUS_UNSUPPORTED_AUDIO_TYPE;
import static pt.ptinovacao.arqospocket.service.service.utils.CallUtils.endCall;
import static pt.ptinovacao.arqospocket.service.tasks.utils.VoiceCallReceiver.ACTION_PHONE_STATE;
import static pt.ptinovacao.arqospocket.service.utils.Constants.FILENAMES_DATE_FORMAT;
import static pt.ptinovacao.arqospocket.service.utils.Constants.RESULTS_FILE_NAME_SEPARATOR;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_CALL_NOT_ACTIVE;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_NOK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_OK;
import static pt.ptinovacao.arqospocket.service.utils.TaskResultMessages.STATUS_UNSUPPORTED_AUDIO_TYPE;


public class RecordAudio implements AudioRecorderHelper.AudioRecorderHelperListener {
    private static final String TAG = RecordAudio.class.getSimpleName();

    private static final int START_DELAY_MS = 5000;

    private Context context;

    private VoiceCallReceiver voiceCallReceiver;
    private RecordAudioTaskStruct recordAudioTaskStruct;
    private IRunTaskWorker callbackRef;
    private GPSInformation gpsInformation;
    private MobileBasicInfoStruct mobileBasicInfoStruct;
    private Mobile mobile;
    private Date startDate, endDate;
    private String recordedFilename;
    private boolean resultPublished = false;

    private AudioRecorderHelper audioRecorder;

    public RecordAudio(Context context, RecordAudioTaskStruct recordAudioTaskStruct, GPSInformation gpsInformation, Mobile mobileRef, IRunTaskWorker callbackRef) {
        this.context = context;
        this.recordAudioTaskStruct = recordAudioTaskStruct;
        this.callbackRef = callbackRef;
        this.gpsInformation = gpsInformation;
        this.mobile = mobileRef;
    }

    public boolean record(){
        Log.i(TAG, "startRecording");
        startDate = new Date();

        int audioRecordingTime = 0;
        int audioType = 0;
        int timeout = 0;

        audioRecordingTime = recordAudioTaskStruct.getAudioRecordingTime();
        timeout = Utils.parseIntParameter(recordAudioTaskStruct.get_timeout());
        audioType = Integer.parseInt(recordAudioTaskStruct.getAudioType());

        recordedFilename = recordAudioTaskStruct.getAudioRecordingFileName()
                + RESULTS_FILE_NAME_SEPARATOR
                + System.currentTimeMillis();

        audioRecorder = new AudioRecorderHelper(
                context,
                recordedFilename,
                audioRecordingTime,
                timeout,
                audioType,
                this);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "Sleeping for: " + START_DELAY_MS);
                    sleep(START_DELAY_MS);
                    if(!mobile.isAnyCallActive()){
                        Log.i(TAG, "No Call(s) active!");
                        publishResult(STATUS_NOK + STATUS_CALL_NOT_ACTIVE);
                        //return true;
                    } else {
                        Log.i(TAG, "Starting the recording!");
                        boolean result = audioRecorder.startRecording();
                        if (!resultPublished){
                            publishResult(result ? STATUS_OK : STATUS_NOK);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        return true;
    }

    private void publishResult(String status){
        Log.i(TAG, "publishResult :: status: " + status);
        resultPublished = true;

        endDate = new Date();

        callbackRef.async_result(
                new RecordAudioTaskJsonResult(
                        recordAudioTaskStruct.get_task_id(),
                        recordAudioTaskStruct.get_task_name(),
                        recordAudioTaskStruct.get_macro_id(),
                        recordAudioTaskStruct.get_task_number(),
                        mobile.getSimIccid(),
                        mobile.getNetworkInfo(),
                        gpsInformation.getFormattedLocation(),
                        status,
                        startDate,
                        endDate,
                        recordedFilename,
                        new MyLocation(gpsInformation),
                        ConnectionInfo.get_active_connection(context)));
    }


    @Override
    public void onRecordingEnded(int status, String filename) {
        Log.i(TAG, "onRecordingEnded :: status: " + status + " filename: " + filename);
        if (!resultPublished) {
            switch (status) {
                case RECORDING_STATUS_OK:
                    publishResult(STATUS_OK);
                    break;
                case RECORDING_STATUS_UNSUPPORTED_AUDIO_TYPE:
                    publishResult(STATUS_NOK + STATUS_UNSUPPORTED_AUDIO_TYPE);
                default:
                    publishResult(STATUS_NOK);
            }
        }

    }
}
