package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Answer voice call task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class AnswerVoiceCallTaskResult extends TaskResult {

    public static final String CALLED_NUMBER = "called_number";

    private static final String FIXED_VALUE = "fixed_value";

    private static final String AUDIO_RECORDING_FILE_NAME = "audio_recording_file_name";

    public static final String TIME_WAITING_FOR_RINGING = "time_waiting_for_ringing";

    @SerializedName(CALLED_NUMBER)
    private String calledNumber;

    @SerializedName(FIXED_VALUE)
    private int fixedValue;

    @SerializedName(AUDIO_RECORDING_FILE_NAME)
    private String audioRecordingFileName;

    @SerializedName(TIME_WAITING_FOR_RINGING)
    private int timeWaitingForRinging;

    public AnswerVoiceCallTaskResult(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getCalledNumber() {
        return calledNumber;
    }

    public void setCalledNumber(String calledNumber) {
        this.calledNumber = calledNumber;
    }

    public int getFixedValue() {
        return fixedValue;
    }

    public void setFixedValue(int fixedValue) {
        this.fixedValue = fixedValue;
    }

    public String getAudioRecordingFileName() {
        return audioRecordingFileName;
    }

    public void setAudioRecordingFileName(String audioRecordingFileName) {
        this.audioRecordingFileName = audioRecordingFileName;
    }

    public int getTimeWaitingForRinging() {
        return timeWaitingForRinging;
    }

    public void setTimeWaitingForRinging(int timeWaitingForRinging) {
        this.timeWaitingForRinging = timeWaitingForRinging;
        
    }
}
