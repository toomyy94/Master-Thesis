package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Answer voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class AnswerVoiceCallTaskData extends TaskData {

    private static final String CALL_TYPE = "call_type";

    private static final String RINGING_TIME = "ringing_time";

    private static final String CALL_DURATION_SECONDS = "call_duration_seconds";

    private static final String AUDIO_RECORDING_FILENAME = "audio_recording_filename";

    private static final String AUDIO_RECORDING_TIME = "audio_recording_time";

    private static final String AUDIO_TYPE = "audio_type";

    @SerializedName(CALL_TYPE)
    private String callType;

    @SerializedName(RINGING_TIME)
    private int ringingTime;

    @SerializedName(CALL_DURATION_SECONDS)
    private int callDurationInSeconds;

    @SerializedName(AUDIO_RECORDING_FILENAME)
    private String audioRecordingFileName;

    @SerializedName(AUDIO_RECORDING_TIME)
    private Integer audioRecordingTime;

    @SerializedName(AUDIO_TYPE)
    private Integer audioType;

    public AnswerVoiceCallTaskData(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public int getRingingTime() {
        return ringingTime;
    }

    public void setRingingTime(int ringingTime) {
        this.ringingTime = ringingTime;
    }

    public int getCallDurationInSeconds() {
        return callDurationInSeconds;
    }

    public void setCallDurationInSeconds(int callDurationInSeconds) {
        this.callDurationInSeconds = callDurationInSeconds;
    }

    public String getAudioRecordingFileName() {
        return audioRecordingFileName;
    }

    public void setAudioRecordingFileName(String audioRecordingFileName) {
        this.audioRecordingFileName = audioRecordingFileName;
    }

    public Integer getAudioRecordingTime() {
        return audioRecordingTime;
    }

    public void setAudioRecordingTime(Integer audioRecordingTime) {
        this.audioRecordingTime = audioRecordingTime;
    }

    public Integer getAudioType() {
        return audioType;
    }

    public void setAudioType(Integer audioType) {
        this.audioType = audioType;
    }
}
