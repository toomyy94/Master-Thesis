package pt.ptinovacao.arqospocket.core.tests.data.results;

import com.google.gson.annotations.SerializedName;

import pt.ptinovacao.arqospocket.core.serialization.ResolverInfo;
import pt.ptinovacao.arqospocket.core.tests.data.TaskResult;

/**
 * Record audio task data.
 * <p>
 * Created by Emílio Simões on 10-04-2017.
 */
public class RecordAudioTaskResult extends TaskResult {

    public static final String AUDIO_RECORD_FILE_NAME = "audio_record_file_name";

    @SerializedName(AUDIO_RECORD_FILE_NAME)
    private String audioRecordingFileName;

    public RecordAudioTaskResult(ResolverInfo resolverInfo, String name) {
        super(resolverInfo, name);
    }

    public String getAudioRecordingFileName() {
        return audioRecordingFileName;
    }

    public void setAudioRecordingFileName(String audioRecordingFileName) {
        this.audioRecordingFileName = audioRecordingFileName;
    }
}
