package pt.ptinovacao.arqospocket.core.tests.data.tasks;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskData;

/**
 * Record audio task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class RecordAudioTaskData extends TaskData {

    public static final String AUDIO_RECORDING_NAME = "audio_recording_name";

    public static final String AUDIO_RECORDING_TIME = "audio_recording_time";

    private static final String AUDIO_TYPE = "audio_type";

    @SerializedName(AUDIO_RECORDING_NAME)
    private String audioRecordingFileName;

    @SerializedName(AUDIO_RECORDING_TIME)
    private int audioRecordingTime;

    @SerializedName(AUDIO_TYPE)
    private String audioType;

    public RecordAudioTaskData(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getAudioRecordingFileName() {
        return audioRecordingFileName;
    }

    public void setAudioRecordingFileName(String audioRecordingFileName) {
        this.audioRecordingFileName = audioRecordingFileName;
    }

    public int getAudioRecordingTime() {
        return audioRecordingTime;
    }

    public void setAudioRecordingTime(int audioRecordingTime) {
        this.audioRecordingTime = audioRecordingTime;
    }

    public String getAudioType() {
        return audioType;
    }

    public void setAudioType(String audioType) {
        this.audioType = audioType;
    }
}
